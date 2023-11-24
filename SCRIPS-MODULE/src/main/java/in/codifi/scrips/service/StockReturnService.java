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

import in.codifi.cache.model.StockReturnModel;
import in.codifi.scrips.config.ApplicationProperties;
import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.entity.primary.StockReturnEntity;
import in.codifi.scrips.model.response.GenericResponse;
import in.codifi.scrips.repository.StockReturnRepository;
import in.codifi.scrips.service.spec.StockReturnServiceSpec;
import in.codifi.scrips.utility.AppConstants;
import in.codifi.scrips.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class StockReturnService implements StockReturnServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	ApplicationProperties props;

	@Inject
	StockReturnRepository stockReturnRepo;

	/**
	 * method to reload stock return file
	 * 
	 * @author sowmiya
	 * @return
	 */
	public RestResponse<GenericResponse> reloadStockReturnFile() {
		boolean status = executeSqlFileFromServer();
		if (status) {
			return prepareResponse.prepareSuccessMessage(AppConstants.CONTRACT_LOAD_SUCESS);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.CONTRACT_LOAD_FAILED);
		}

	}

	/**
	 * method to execute sql file from server
	 * 
	 * @author sowmiya
	 * @return
	 */
	private boolean executeSqlFileFromServer() {
		boolean status = false;
		try {
			String localFilePath = props.getLocalStockDir();

			Date today = new Date();
			String date = new SimpleDateFormat("ddMMYY").format(today);
			String fileName = AppConstants.STOCK_RETURN_FILE_NAME + date + AppConstants.SQL;

			String remoteDir = props.getRemoteStockDire() + fileName;
			boolean isFileMoved = getsqlFileFromServer(localFilePath.toString(), remoteDir);

			if (isFileMoved) {
				@SuppressWarnings("unused")
				boolean isInserted = executeSqlFile(localFilePath, fileName, date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return status;

	}

	/**
	 * method to execute sql file
	 * 
	 * @author sowmiya
	 * @param localFilePath
	 * @param fileName
	 * @param date
	 * @return
	 */
	private boolean executeSqlFile(String localFilePath, String fileName, String date) {
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
					Log.info("stock returns update is failed");
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
				Log.info("stock returns update is failed");
			}
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * method to get sql file from server
	 * 
	 * @author sowmiya
	 * @param localFilePath
	 * @param remotefilePath
	 * @return
	 */
	@SuppressWarnings("unused")
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
	 * method to load stock return
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<GenericResponse> loadStockReturn() {
		try {
			List<StockReturnEntity> stockReturn = stockReturnRepo.findAll();
			if (stockReturn != null && stockReturn.size() > 0) {
				HazelcastConfig.getInstance().getStockReturnDetails().clear();
				for (StockReturnEntity entity : stockReturn) {
					String key = entity.getExch() + "_" + entity.getToken();
					List<StockReturnEntity> stockReturnDetails = stockReturnRepo.findByExchAndToken(entity.getExch(),
							entity.getToken());
					if (stockReturnDetails != null && stockReturnDetails.size() > 0) {
						List<StockReturnModel> stockReturnModel = prepareStockModel(stockReturnDetails);
						HazelcastConfig.getInstance().getStockReturnDetails().put(key, stockReturnModel);
					}
				}
				return prepareResponse.prepareSuccessMessage(AppConstants.LOADED);

			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}

		} catch (Exception e) {
			Log.error("common - loadStockReturns -", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare stock model
	 * 
	 * @author sowmiya
	 * @param stockReturnDetails
	 * @return
	 */
	private List<StockReturnModel> prepareStockModel(List<StockReturnEntity> stockReturnDetails) {
		List<StockReturnModel> stockDetails = new ArrayList<>();
		try {
			for (StockReturnEntity entity : stockReturnDetails) {
				StockReturnModel model = new StockReturnModel();
				model.setChangePerc(entity.getChangePerc());
				model.setClose(entity.getClose());
				model.setDateOfClose(entity.getDateOfClose());
				model.setExch(entity.getExch());
				model.setPrevDayClose(entity.getPrevDayClose());
				model.setSymbol(entity.getSymbol());
				model.setTagOfPeriod(entity.getTagOfPeriod());
				model.setToken(entity.getToken());
				model.setId(entity.getId());
				stockDetails.add(model);
			}

		} catch (Exception e) {
			Log.error("scrip - prepareStockModel  -", e);
		}
		return stockDetails;
	}

}
