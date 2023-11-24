package in.codifi.admin.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.config.ApplicationProperties;
import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.entity.HoldingsEntity;
import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.HoldingsReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.HoldingsCountRespModel;
import in.codifi.admin.repository.HoldingRepository;
import in.codifi.admin.repository.HoldingsDao;
import in.codifi.admin.service.spec.HoldingsServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.AppUtils;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class HoldingsService implements HoldingsServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ApplicationProperties props;
	@Inject
	AppUtils appUtils;
	@Inject
	HoldingsDao dao;
	@Inject
	HoldingRepository holdingsRepository;

	/**
	 * method to upload holdings file
	 * 
	 * @author SOWMIYA
	 *
	 */
	@Override
	public RestResponse<GenericResponse> uploadHoldingsFile(FormDataModel file, String holdingsType) {
		try {
			/** check parameters **/
			if (file == null || file.getFile() == null)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			/** to get the file name **/
			String fileName = file.getFile().fileName();
			int dotIndex = fileName.lastIndexOf(".");
			String extension = "";
			if (dotIndex > 0) {
				extension = fileName.substring(dotIndex);
			}
			if (fileName == null || !fileName.endsWith(extension))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_FILE_TYPE);

			/** to make file path **/
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyyhhmm");
			String strDate = formatter.format(date).toUpperCase();
			String filePath = props.getHoldingsFilePath() + holdingsType + "_" + strDate + "_" + "Holdings"
					+ AppConstants.TEXT_FILE_FORMATS;
			File fileDir = new File(props.getHoldingsFilePath());
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			/** upload a file **/
			Path targetPath = Paths.get(filePath);

			/** Delete old file, if exist **/
			File folder = new File(props.getHoldingsFilePath());
			File[] listOfFiles = folder.listFiles();
			for (int count = 0; count < listOfFiles.length; count++) {
				File oldFile = listOfFiles[count];
				if (oldFile.isFile() && oldFile.getName().startsWith(holdingsType)) {
					oldFile.delete();
				}
			}
			Files.copy(file.getFile().filePath(), targetPath);

			return prepareResponse.prepareSuccessMessage(AppConstants.FILE_UPLOADED);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to insert holdings files
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertHoldingsFile() {
		try {
			String isInserted = insertHoldingsFiles();
			if (isInserted.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
				boolean isDeleted = dao.deleteHoldingsArchiveTable();
				if (isDeleted) {
					boolean isT1Inserted = dao.inserT1HoldingsDataIntoLatest();
					boolean isDpInserted = dao.inserDPHoldingsDataIntoLatest();
					if (isT1Inserted && isDpInserted) {
						boolean isMoved = dao.moveHodings();
						if (isMoved) {
							/** Clear cache **/
//							HazelcastConfig.getInstance().getPledgeDataKB().clear();
//							HazelcastConfig.getInstance().getPoaStatusKB().clear();
//							HazelcastConfig.getInstance().getEdisUpdateKB().clear();
							HazelcastConfig.getInstance().getUploadedHoldings().clear();
							return prepareResponse.prepareSuccessMessage(AppConstants.INSERTED);
						}
					}
				}
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to insert holdings file
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	public String insertHoldingsFiles() {
		String holdingsType = "";
		try {
			File folder = new File(props.getHoldingsFilePath());
			File[] listOfFiles = folder.listFiles();
			for (int count = 0; count < listOfFiles.length; count++) {
				File file = listOfFiles[count];
				if (file.isFile() && file.getName().endsWith(AppConstants.TEXT_FILE_FORMATS)
						|| file.getName().endsWith(AppConstants.HOLDINGS_FILE_FORMATS)) {
					if (file.getName().startsWith(AppConstants.T1TYPE)) {
						holdingsType = "T1";
					} else {
						holdingsType = "DP";
					}
					Log.info(holdingsType + " - holdings started to insert");
					List<HoldingsEntity> holdingsEntities = prepareHoldingsData(file, holdingsType);
					boolean inserted = false;
					if (StringUtil.isListNotNullOrEmpty(holdingsEntities)) {
						/** method to insert holdings files into data base **/

						if (holdingsType.equalsIgnoreCase("T1")) {
							inserted = dao.inserT1HoldingsData(holdingsEntities);
						} else {
							inserted = dao.inserDPHoldingsData(holdingsEntities);
						}
						Date date = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy");
						String strDate = formatter.format(date).toUpperCase();
						if (inserted) {
							int size = props.getHoldingsCompletedPath().lastIndexOf("/");
							String slash = "//";
							if (size > 0) {
								slash = "/";
							}
							File completed = new File(props.getHoldingsCompletedPath() + strDate);
							if (!completed.exists()) {
								completed.mkdirs();
							}
							if (file.renameTo(new File(completed.toString() + slash + file.getName()))) {
								file.delete();
								Log.info(holdingsType + " - File Moved Successfully");
							}
							Log.info(holdingsType + " - Holding data inserted Successfully");
						}
					}
				}
			}
			return AppConstants.SUCCESS_STATUS;
		} catch (

		Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return AppConstants.FAILED_STATUS;
	}

	/**
	 * Method to prepare holdings data
	 *
	 * @author SOWMIYA
	 * @param file
	 * @param holdingsType
	 *
	 * @return
	 * @throws FileNotFoundException
	 */
	private List<HoldingsEntity> prepareHoldingsData(File file, String holdingsType) throws FileNotFoundException {
		List<HoldingsEntity> list = new ArrayList<HoldingsEntity>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String strLine = "";
		String poaStatus = "";
		try {
			br.readLine();
			if (holdingsType.startsWith(AppConstants.T1TYPE)) {
				while ((strLine = br.readLine()) != null) {
					String[] values = strLine.trim().split("\\|");
					HoldingsEntity holdingsEntity = new HoldingsEntity();
					holdingsEntity.setHoldingsType("T1");
					if (values.length == 13) {
						holdingsEntity.setUserId(values[0]);
						holdingsEntity.setIsin(values[1]);
						String qtyValue = values[2];
						String[] qty = qtyValue.split("\\.");
						if (StringUtil.isNotNullOrEmpty(qty[0])) {
							holdingsEntity.setQty(Integer.valueOf(qty[0]));
						}

						if (StringUtil.isNotNullOrEmpty(String.valueOf(values[5]))) {
							holdingsEntity.setCollateralQty(Integer.valueOf(values[5]));
						}
						holdingsEntity.setHaircut(values[6]);
						holdingsEntity.setClosePrice(Double.parseDouble(values[7]));
						holdingsEntity.setProduct(values[8]);
						holdingsEntity.setActualPrice(Double.parseDouble(values[10]));
						poaStatus = values[12];
						holdingsEntity.setPoaStatus(values[12]);
						holdingsEntity.setAuthFlag(poaStatus.equalsIgnoreCase("Y") ? 1 : 0);
						list.add(holdingsEntity);
					} else {
						System.out.println("Size is less than 13 for line No -" + values);
					}
				}
				System.out.println("Total Size of uploaded T1 holdings" + list.size());
				br.close();
			} else {
				while ((strLine = br.readLine()) != null) {
					String[] values = strLine.trim().split("\\|");
					HoldingsEntity holdingsEntity = new HoldingsEntity();
					holdingsEntity.setHoldingsType("DP");
					if (values.length == 17) {
						holdingsEntity.setUserId(values[0]);
						holdingsEntity.setIsin(values[1]);
						holdingsEntity.setQty(Integer.valueOf(values[2]));
						holdingsEntity.setHaircut(values[6]);
						if (StringUtil.isNotNullOrEmpty(String.valueOf(values[7]))) {
							holdingsEntity.setBrokerCollQty(Integer.valueOf(values[7]));
						}
						if (StringUtil.isNotNullOrEmpty(String.valueOf(values[8]))) {
							holdingsEntity.setDpQty(Integer.valueOf(values[8]));
						}
						if (StringUtil.isNotNullOrEmpty(String.valueOf(values[10]))) {
							holdingsEntity.setBenQty(Integer.valueOf(values[10]));
						}
						if (StringUtil.isNotNullOrEmpty(String.valueOf(values[11]))) {
							holdingsEntity.setUnpledgeQy(Integer.valueOf(values[11]));
						}
						if (StringUtil.isNotNullOrEmpty(String.valueOf(values[5]))) {
							holdingsEntity.setCollateralQty(Integer.valueOf(values[5]));
						}

						holdingsEntity.setClosePrice(Double.parseDouble(values[9]));
						holdingsEntity.setProduct(values[12]);
						holdingsEntity.setActualPrice(Double.parseDouble(values[14]));
						poaStatus = values[16];
						holdingsEntity.setPoaStatus(values[16]);
						holdingsEntity.setAuthFlag(poaStatus.equalsIgnoreCase("Y") ? 1 : 0);
						list.add(holdingsEntity);
					} else {
						System.out.println("Size is less than 17 for line No -" + values[1] + "user -" + values[0]);
					}
				}
				System.out.println("Total Size of uploaded DP holdings" + list.size());
				br.close();
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * method to load holdings data into cache
	 * 
	 * @author SOWMIYA
	 * @return
	 */
//	public RestResponse<GenericResponse> loadHoldingsData() {
//		try {
//			List<HoldingsDataEntity> holdingsData = holdingDataRepo.findAll();
//			if (StringUtil.isListNotNullOrEmpty(holdingsData)) {
//				for (HoldingsDataEntity entity : holdingsData) {
//					HoldingsJSONEntity jsonEntity = new HoldingsJSONEntity();
//					String userId = entity.getUserId();
//					jsonEntity.setBuyAvg(entity.getBuyAvg());
//					jsonEntity.setBuyValue(entity.getBuyValue());
//					jsonEntity.setAuthQty(entity.getAuthQty());
//					jsonEntity.setHoldingsType(entity.getHoldingsType());
//					jsonEntity.setIsin(entity.getIsin());
//					jsonEntity.setBseCode(entity.getBseCode());
//					jsonEntity.setNseCode(entity.getNseCode());
//					jsonEntity.setPoaStatus(entity.getPoaStatus());
//					jsonEntity.setQty(entity.getQuantity());
//					jsonEntity.setReqId(entity.getReqId());
//					jsonEntity.setSymbol(entity.getSymbol());
//					jsonEntity.setTxnId(entity.getTxnId());
//					jsonEntity.setAuthFlag(entity.getAuthFlag());
//					HazelcastConfig.getInstance().getHoldingsData().clear();
//					HazelcastConfig.getInstance().getHoldingsData().put(userId, jsonEntity);
//				}
//				return prepareResponse.prepareSuccessMessage(AppConstants.LOADED);
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.error(e.getMessage());
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//
//	}

	/**
	 * method to get holdings data
	 * 
	 * @author SOWMIYA
	 * 
	 */
	@Override
	public RestResponse<GenericResponse> getHoldingsData(HoldingsReqModel reqModel) {
		try {
			if (StringUtil.isNullOrEmpty(reqModel.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			List<HoldingsEntity> holdingsEntity = holdingsRepository.getHoldingsData(reqModel.getUserId());
			if (holdingsEntity != null)
				return prepareResponse.prepareSuccessResponseObject(holdingsEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get holdings count
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> getHoldingsCount() {
		HoldingsCountRespModel responseModel = new HoldingsCountRespModel();
		try {
			long holdingsCount = holdingsRepository.count();
			if (holdingsCount > 0) {
				responseModel.setCount(holdingsCount);
				responseModel.setExchange("NSE");
				return prepareResponse.prepareSuccessResponseObject(responseModel);
			}
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
