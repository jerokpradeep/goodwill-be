package in.codifi.scrips.service;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import in.codifi.cache.model.ContractMasterModel;
import in.codifi.cache.model.MtfDataModel;
import in.codifi.cache.model.PnlLotModel;
import in.codifi.scrips.config.ApplicationProperties;
import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.entity.primary.ContractEntity;
import in.codifi.scrips.entity.primary.MTFEntity;
import in.codifi.scrips.entity.primary.PnlLotEntity;
import in.codifi.scrips.model.response.GenericResponse;
import in.codifi.scrips.repository.ContractEntityManager;
import in.codifi.scrips.repository.ContractRepository;
import in.codifi.scrips.repository.MtfRepository;
import in.codifi.scrips.repository.PnlLotRepository;
import in.codifi.scrips.repository.PromptDao;
import in.codifi.scrips.repository.ScripSearchEntityManager;
import in.codifi.scrips.service.spec.ContractServiceSpecs;
import in.codifi.scrips.utility.AppConstants;
import in.codifi.scrips.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ContractService implements ContractServiceSpecs {

	@Inject
	ContractRepository contractRepository;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	ContractEntityManager contractEntityManager;

	@Inject
	ScripSearchEntityManager entityManager;

	@Inject
	ApplicationProperties props;

	@Inject
	PromptDao promptDao;
	@Inject
	MtfRepository mtfRepository;

	@Inject
	PnlLotRepository pnlLotRepository;

	/**
	 * Method To load contract master into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadContractMaster() {
		try {
			List<ContractEntity> contractList = new ArrayList<>();
			contractList = contractRepository.findAll();
			if (contractList.size() > 0)
				HazelcastConfig.getInstance().getContractMaster().clear();
			for (ContractEntity contractEntity : contractList) {
				ContractMasterModel result = new ContractMasterModel();

				result.setExch(contractEntity.getExch());
				result.setSegment(contractEntity.getSegment());
				result.setSymbol(contractEntity.getSymbol());
				result.setIsin(contractEntity.getIsin());
				result.setFormattedInsName(contractEntity.getFormattedInsName());
				result.setToken(contractEntity.getToken());
				result.setTradingSymbol(contractEntity.getTradingSymbol());
				result.setGroupName(contractEntity.getGroupName());
				result.setInsType(contractEntity.getInsType());
				result.setOptionType(contractEntity.getOptionType());
				result.setStrikePrice(contractEntity.getStrikePrice());
				result.setExpiry(contractEntity.getExpiryDate());
				result.setLotSize(contractEntity.getLotSize());
				result.setTickSize(contractEntity.getTickSize());
				result.setPdc(contractEntity.getPdc());
				result.setWeekTag(contractEntity.getWeekTag());
				result.setFreezQty(contractEntity.getFreezeQty());
				result.setAlterToken(contractEntity.getAlterToken());
				result.setCompanyName(contractEntity.getCompanyName());
				String key = contractEntity.getExch() + "_" + contractEntity.getToken();
				HazelcastConfig.getInstance().getContractMaster().put(key, result);
			}
			System.out.println("Loaded SucessFully");
			System.out.println("Full Size " + HazelcastConfig.getInstance().getContractMaster().size());
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.CONTRACT_LOAD_FAILED);
		}
		return prepareResponse.prepareSuccessMessage(AppConstants.CONTRACT_LOAD_SUCESS);
	}

	/**
	 * Delete Expired contract manually
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteExpiredContract() {

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = format.format(date);
		return contractEntityManager.deleteExpiredContract(todayDate);
	}

	/**
	 * 
	 * Method to Delete BSE Contract
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteBSEContract() {
		return contractEntityManager.deleteBSEContract();
	}

	/**
	 * Method can be used for Update contract master
	 * 
	 * Desc : GET SQL file from server and update the same in DB
	 * 
	 * @author Nesan created on 22-03-23
	 * 
	 * @return
	 */
	public Boolean executeSqlFileFromServer() {
		boolean status = false;
		try {
			String localFilePath = props.getLocalcontractDir();

			Date today = new Date();
			String date = new SimpleDateFormat("ddMMYY").format(today);
			String fileName = AppConstants.CONTRACT_FILE_NMAE + date + AppConstants.SQL;

			String remoteDir = props.getRemoteContractDire() + fileName;
			System.out.println(remoteDir);
			boolean isFileMoved = getsqlFileFromServer(localFilePath.toString(), remoteDir);

			if (isFileMoved) {
				boolean isInserted = executeSqlFile(localFilePath, fileName, date);
//				deleteBSEContract();
				deleteExpiredContract();
				loadContractIntoCache();
				status = true;
			} else {
//				deleteBSEContract();
				deleteExpiredContract();
				Log.error("Failed to get contract file" + fileName + " from server");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return status;

	}

	/**
	 * Method to clear cache and load latest data into cache
	 * 
	 * @author DINESH KUMAR
	 *
	 */
	public void loadContractIntoCache() {
		Log.info("Started to clear cache");
		HazelcastConfig.getInstance().getFetchDataFromCache().clear();
		HazelcastConfig.getInstance().getDistinctSymbols().clear();
		HazelcastConfig.getInstance().getLoadedSearchData().clear();
		HazelcastConfig.getInstance().getFetchDataFromCache().clear();
		Log.info("Cache cleared and started to load new data");
		HazelcastConfig.getInstance().getFetchDataFromCache().put(AppConstants.FETCH_DATA_FROM_CACHE, true);
		entityManager.loadDistintValue(2);
		entityManager.loadDistintValue(3);
		loadContractMaster();
		Log.info("Cache loaded sucessfully");
	}

	/**
	 * Method to move file from server to local
	 * 
	 * @author Nesan
	 * 
	 * @param localFilePath
	 * @param remotefilePath
	 * @return
	 */
	private boolean getsqlFileFromServer(String localFilePath, String remotefilePath) {
		boolean status = false;

		String localHost = AppConstants.LOCALHOST;
		int localPort = AppConstants.PORT_3306;
		int forwardPort = AppConstants.PORT_3308;

		Session session = null;
		ChannelSftp channelSftp = null;
		try {
			JSch jsch = new JSch();

			session = jsch.getSession(props.getSshUserName(), props.getSshHost(), props.getSshPort());
//			session.setPortForwardingL(forwardPort, localHost, localPort);

			session.setPassword(props.getSshPassword());
			session.setConfig(AppConstants.STRICTHOSTKEYCHECKING, AppConstants.NO);
			session.connect();
			/* File movement from server to local */
			Channel sftp = session.openChannel(AppConstants.SFTP);
			// 5 seconds timeout
			sftp.connect(5000);
			channelSftp = (ChannelSftp) sftp;
			/* transfer file from remote server to local */
			channelSftp.stat(remotefilePath);
			channelSftp.get(remotefilePath, localFilePath);
			channelSftp.exit();
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.disconnect();
		}
		return status;
	}

	/**
	 * Method to execute sql file
	 * 
	 * @author Nesan
	 * 
	 * @param string
	 */
	private boolean executeSqlFile(String localFilePath, String fileName, String date) {
		boolean status = false;
		File directory = new File(localFilePath + fileName);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		int size = localFilePath.lastIndexOf("/");
		String slash = "//";
		if (size > 0) {
			slash = "/";
		}
		try {
			if (directory.isFile()) {
				/* This one can be finalized */
				String tCommand = "mysql -u " + props.getDbUserName() + " -p" + props.getDbpassword() + " "
						+ props.getDbSchema();
				System.out.println(tCommand);
				String sqlQueries = new String(Files.readAllBytes(Paths.get(directory.toURI())));
				Process tProcess = Runtime.getRuntime().exec(tCommand); // 20
				OutputStream tOutputStream = tProcess.getOutputStream();
				Writer w = new OutputStreamWriter(tOutputStream);
				w.write(sqlQueries);
				w.flush();
				w.close();
				status = true;
				File completed = new File(localFilePath + "completed");
				if (!completed.exists()) {
					completed.mkdirs();
				}
				if (directory.renameTo(new File(completed.toString() + slash + date))) {
					directory.delete();
					Log.info("File Moved Successfully");
				}
			} else {
				/* sent mail */
				File completed = new File(localFilePath + "failed");
				if (!completed.exists()) {
					completed.mkdirs();
				}
				if (directory.renameTo(new File(completed.toString() + slash + date))) {
					directory.delete();
					Log.info("Contract master update is failed");
				}
			}
		} catch (Exception e) {
			/* sent mail */
			File completed = new File(localFilePath + "failed");
			if (!completed.exists()) {
				completed.mkdirs();
			}
			if (directory.renameTo(new File(completed.toString() + slash + date))) {
				directory.delete();
				Log.info("Contract master update is failed");
			}
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Method to get reload contract master file from server
	 * 
	 * @author Nesan
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> reloadContractMasterFile() {

		boolean status = executeSqlFileFromServer();
		if (status) {
			return prepareResponse.prepareSuccessMessage(AppConstants.CONTRACT_LOAD_SUCESS);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.CONTRACT_LOAD_FAILED);
		}

	}

	/**
	 * 
	 * Method to load prompt
	 * 
	 * @author Dinesh Kumar
	 *
	 */
	public void loadPromptData() {
		promptDao.loadPromptData();
	}

	/**
	 * method to load MTF data
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> loadMTFData() {
		try {
			List<MTFEntity> mtfValues = mtfRepository.findAll();
			if (mtfValues.size() > 0) {
				HazelcastConfig.getInstance().getMtfDataModel().clear();
				for (MTFEntity entity : mtfValues) {
					MtfDataModel model = new MtfDataModel();
					model.setExch(entity.getExch());
					model.setCompanyName(entity.getCompanyName());
					model.setIsin(entity.getIsin());
					model.setMtfMargin(entity.getMtfMargin());
					model.setMultiplier(entity.getMultiplier());
					model.setStatus(entity.getStatus());
					model.setSymbol(entity.getSymbol());
					model.setToken(entity.getToken());
					String key = entity.getExch() + "_" + entity.getToken();
					HazelcastConfig.getInstance().getMtfDataModel().put(key, model);
				}
				return prepareResponse.prepareSuccessMessage(AppConstants.MTF_LOAD_SUCCESS);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_DATA);
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.MTF_LOAD_FAILED);
	}

	/**
	 * method to reload MTF approved file from server to local database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> reloadMtfApprovedFile() {
		boolean status = executeMTFSqlFileFromServer();
		if (status) {
			return prepareResponse.prepareSuccessMessage(AppConstants.MTF_LOAD_SUCCESS);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.MTF_LOAD_FAILED);
		}
	}

	/**
	 * method to execute MTF sql file from server
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	private boolean executeMTFSqlFileFromServer() {
		boolean status = false;
		try {
			String localFilePath = props.getLocalMtfDir();

			Date today = new Date();
			String date = new SimpleDateFormat("ddMMYY").format(today);
			String fileName = AppConstants.MTF_FILE_NAME + date + AppConstants.SQL;

			String remoteDir = props.getRemoteContractDire() + fileName;
			boolean isFileMoved = getMtfsqlFileFromServer(localFilePath.toString(), remoteDir);

			if (isFileMoved) {
				boolean isInserted = executeMtfSqlFile(localFilePath, fileName, date);
				if (isInserted) {
					deleteExpiredContract();
					loadContractIntoCache();
					status = true;
				} else {
					deleteExpiredContract();
					loadContractIntoCache();
				}
			}

		} catch (Exception e) {
			Log.error(e);
		} finally {

		}
		return status;
	}

	/**
	 * method to execute MTF sql file
	 * 
	 * @author SOWMIYA
	 * @param localFilePath
	 * @param fileName
	 * @param date
	 * @return
	 */
	private boolean executeMtfSqlFile(String localFilePath, String fileName, String date) {
		boolean status = false;
		File directory = new File(localFilePath + fileName);
		int size = localFilePath.lastIndexOf("/");
		String slash = "//";
		if (size > 0) {
			slash = "/";
		}
		try {
			if (directory.isFile()) {
				/* This one can be finalized */
				String tCommand = "mysql -u " + props.getDbUserName() + " -p" + props.getDbpassword() + " "
						+ props.getDbSchema();
				System.out.println(tCommand);
				String sqlQueries = new String(Files.readAllBytes(Paths.get(directory.toURI())));
				Process tProcess = Runtime.getRuntime().exec(tCommand); // 20
				OutputStream tOutputStream = tProcess.getOutputStream();
				Writer w = new OutputStreamWriter(tOutputStream);
				w.write(sqlQueries);
				w.flush();
				status = true;
				File completed = new File(localFilePath + "completed");
				if (!completed.exists()) {
					completed.mkdirs();
				}
				if (directory.renameTo(new File(completed.toString() + slash + date))) {
					directory.delete();
					Log.info("MTF File Moved Successfully");
				}
			} else {
				File completed = new File(localFilePath + "failed");
				if (!completed.exists()) {
					completed.mkdirs();
				}
				if (directory.renameTo(new File(completed.toString() + slash + date))) {
					directory.delete();
					Log.info("MTF Approved file update is failed");
				}
			}
		} catch (Exception e) {
			File completed = new File(localFilePath + "failed");
			if (!completed.exists()) {
				completed.mkdirs();
			}
			if (directory.renameTo(new File(completed.toString() + slash + date))) {
				directory.delete();
				Log.info("MTF Approved file update is failed");
			}
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * method to get mtf sql file from server
	 * 
	 * @author SOWMIYA
	 * @param string
	 * @param remoteDir
	 * @return
	 */
	private boolean getMtfsqlFileFromServer(String localFilePath, String remotefilePath) {
		boolean status = false;

//		String localHost = AppConstants.LOCALHOST;
//		int localPort = AppConstants.PORT_3306;
//		int forwardPort = AppConstants.PORT_3308;

		Session session = null;
		ChannelSftp channelSftp = null;
		try {
			JSch jsch = new JSch();

			session = jsch.getSession(props.getSshUserName(), props.getSshHost(), props.getSshPort());

			session.setPassword(props.getSshPassword());
			session.setConfig(AppConstants.STRICTHOSTKEYCHECKING, AppConstants.NO);
			session.connect();
			/* File movement from server to local */
			Channel sftp = session.openChannel(AppConstants.SFTP);
			// 5 seconds timeout
			sftp.connect(5000);
			channelSftp = (ChannelSftp) sftp;
			/* transfer file from remote server to local */
			channelSftp.stat(remotefilePath);
			// To check the directory available in server
			File directory = new File(localFilePath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			channelSftp.get(remotefilePath, localFilePath);
			channelSftp.exit();
			status = true;
		} catch (Exception e) {
			Log.error(e);
		} finally {
			if (session != null)
				session.disconnect();
		}
		return status;
	}

	/**
	 * Method to load PNL Lot
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadPnlLotSize() {
		try {
			List<PnlLotEntity> pnlLotEntities = new ArrayList<>();
			List<PnlLotModel> pnlLotModels = new ArrayList<>();
			pnlLotEntities = pnlLotRepository.findAll();
			if (pnlLotEntities.size() > 0)
				HazelcastConfig.getInstance().getPnlLot().clear();
			for (PnlLotEntity pnlLotEntity : pnlLotEntities) {
				PnlLotModel result = new PnlLotModel();
				result.setExch(pnlLotEntity.getExch());
				result.setSymbol(pnlLotEntity.getSymbol());
				result.setToken(pnlLotEntity.getToken());
				result.setTradingSymbol(pnlLotEntity.getTradingSymbol());
				result.setExpiry(pnlLotEntity.getExpiryDate());
				result.setLotSize(pnlLotEntity.getLotSize());
				pnlLotModels.add(result);
			}
			HazelcastConfig.getInstance().getPnlLot().put(AppConstants.PNL_LOT, pnlLotModels);
			System.out.println("Pnl Lot loaded sucessFully");
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.CONTRACT_LOAD_FAILED);
		}
		return prepareResponse.prepareSuccessMessage(AppConstants.CONTRACT_LOAD_SUCESS);
	}

}
