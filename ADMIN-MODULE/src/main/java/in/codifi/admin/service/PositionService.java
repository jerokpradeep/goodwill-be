package in.codifi.admin.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.config.ApplicationProperties;
import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.entity.PositionAvgPriceEntity;
import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.PositionReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.PositionCountRespModel;
import in.codifi.admin.model.response.PositionResultModel;
import in.codifi.admin.repository.PositionsDao;
import in.codifi.admin.repository.PositionsRepository;
import in.codifi.admin.service.spec.PositionServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.AppUtils;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PositionService implements PositionServiceSpec {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ApplicationProperties props;
	@Inject
	PositionsRepository positionsRepository;
	@Inject
	AppUtils appUtils;
	@Inject
	PositionsDao dao;

	/**
	 * Method to upload position file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @param exchange
	 * @return
	 */
	public RestResponse<GenericResponse> uploadPositionFile(FormDataModel file, String exchange) {

		try {
			/** check parameters **/
			if (file.getFile() == null)
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
			String filePath = props.getPositionFilePath() + exchange + AppConstants.EXCEL_FILE_FORMATS;
			File fileDir = new File(props.getPositionFilePath());
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			/** upload a file **/
			Path targetPath = Paths.get(filePath);
			Files.deleteIfExists(targetPath);
			Files.copy(file.getFile().filePath(), targetPath);

			return prepareResponse.prepareSuccessMessage(AppConstants.FILE_UPLOADED);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to insert position files
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertPositionFile() {
		try {
			String isInserted = insertAvgPriceDetails();
			if (isInserted.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
//				String deleteArchive = positionsEntityManager.deletePositionArchive();
				boolean deleteArchive = dao.deletePositionArchive();
				if (deleteArchive) {
//					String moved = positionsEntityManager.moveAvgPrice();
					boolean isMoved = dao.moveAvgPrice();
					if (isMoved) {
						HazelcastConfig.getInstance().getPositionsAvgPrice().clear();
						Log.info("All position files are inserted sucessfully");
						// Check and send mail to the Admin
						checkCountPositionAvgPrice();
						return prepareResponse.prepareSuccessMessage(AppConstants.INSERTED);
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
	 * Method to check the position average price
	 * 
	 * @author Gowrisankar
	 */
	private void checkCountPositionAvgPrice() {
		try {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy");
			String strDate = formatter.format(date).toUpperCase();
			File folder = new File(props.getPositionCompletedPath() + strDate + "//");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File[] listOfFiles = folder.listFiles();
			int countMCX = 1;
			int countCDS = 1;
			int countNFO = 1;
			for (int count = 0; count < listOfFiles.length; count++) {
				File file = listOfFiles[count];
				if (file.isFile() && (file.getName().endsWith(".xls") || file.getName().endsWith(".XLS"))) {
					String fileName = file.getName();
					String exch = fileName;
					if (exch.contains(".xls")) {
						exch = exch.replace(".xls", "");
					} else if (exch.contains(".XLS")) {
						exch = exch.replace(".XLS", "");
					}
					BufferedReader lineReader = new BufferedReader(new FileReader(file));
					lineReader.readLine(); // skip header line
					while ((lineReader.readLine()) != null) {
						if (exch.equalsIgnoreCase("MCX")) {
							countMCX++;
						}
						if (exch.equalsIgnoreCase("CDS")) {
							countCDS++;
						}
						if (exch.equalsIgnoreCase("NFO")) {
							countNFO++;
						}
					}
				}
			}
			countMCX = countMCX - 5;
			countCDS = countCDS - 5;
			countNFO = countNFO - 5;
			List<String> positionAvgPriceCount = dao.getPositionAvgPriceCount();
			List<String> exchSeg = new ArrayList<String>();
			List<String> segCount = new ArrayList<String>();
			int exchCount = 0;
			for (int i = 0; i < positionAvgPriceCount.size(); i++) {
				String a = positionAvgPriceCount.get(i);
				String[] a2 = a.split("-");
				String exch = a2[0];
				String count = a2[1];
				exchSeg.add(exch);
				segCount.add(count);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("<html>");
			sb.append("<head>");
			sb.append("</head>");
			sb.append("<style> table, th, td {" + "  border: 1px solid black;" + "  border-collapse: collapse;"
					+ "}</style>");
			sb.append("<table>");
			sb.append("<th> Segment </th>");
			sb.append("<th> Table Count </th>");
			sb.append("<th> Excel Count </th>");
			sb.append("<th> Difference </th>");
			String style = "style=\"color:red;\" ";
			for (int i = 0; i < positionAvgPriceCount.size(); i++) {
				exchCount = Integer.parseInt(segCount.get(i));
				sb.append("<tr>");
				sb.append("<td> " + exchSeg.get(i) + " </td>");
				sb.append("<td> " + segCount.get(i) + " </td>");
				if (exchSeg.get(i).equalsIgnoreCase("MCX")) {
					sb.append("<td> " + countMCX + " </td>");
				}
				if (exchSeg.get(i).equalsIgnoreCase("CDS")) {
					sb.append("<td> " + countCDS + " </td>");
				}
				if (exchSeg.get(i).equalsIgnoreCase("NFO")) {
					sb.append("<td> " + countNFO + " </td>");

				}
				if (exchSeg.get(i).equalsIgnoreCase("MCX")) {
					if (!(countMCX == exchCount)) {
						sb.append("<td " + style + "> " + (countMCX - exchCount) + " </td>");
					} else {
						sb.append("<td> " + (countMCX - exchCount) + " </td>");
					}
				}
				if (exchSeg.get(i).equalsIgnoreCase("CDS")) {
					if (!(countCDS == exchCount)) {
						sb.append("<td " + style + "> " + (countCDS - exchCount) + " </td>");
					} else {
						sb.append("<td > " + (countCDS - exchCount) + " </td>");
					}
				}
				if (exchSeg.get(i).equalsIgnoreCase("NFO")) {
					if (!(countNFO == exchCount)) {
						sb.append("<td " + style + "> " + (countNFO - exchCount) + " </td>");
					} else {
						sb.append("<td> " + (countNFO - exchCount) + " </td>");
					}
				}
				sb.append("</tr>");
			}
			sb.append("</table>");
			sb.append("</body>");
			sb.append("</html>");
			String html = sb.toString();
//			appUtils.preparepositionCountMail(html, "URGENT ATTENTION : Position Average Price Count Difference");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to insert position file
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	private String insertAvgPriceDetails() {
		String insert = AppConstants.FAILED_STATUS;
		try {
			File folder = new File(props.getPositionFilePath());
			File[] listOfFiles = folder.listFiles();
			for (int count = 0; count < listOfFiles.length; count++) {
				File file = listOfFiles[count];
				if (file.isFile() && file.getName().endsWith(".xls")) {
					List<PositionAvgPriceEntity> list = PreparePositionData(file);
					if (StringUtil.isListNotNullOrEmpty(list)) {
						/** method to insert position files into database **/
//						int inserted = positionsEntityManager.insertPositionFile(list);
						boolean inserted = dao.insertPositionFile(list);
						insert = AppConstants.SUCCESS_STATUS;
						Date date = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy");
						String strDate = formatter.format(date).toUpperCase();
						if (inserted) {
							int size = props.getPositionCompletedPath().lastIndexOf("/");
							String slash = "//";
							if (size > 0) {
								slash = "/";
							}
							File completed = new File(props.getPositionCompletedPath() + strDate);
							if (!completed.exists()) {
								completed.mkdirs();
							}
							if (file.renameTo(new File(completed.toString() + slash + file.getName()))) {
								file.delete();
								System.out.println("File Moved Successfully");

							}
							insert = AppConstants.SUCCESS_STATUS;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return insert;
	}

	/**
	 * Method to prepare position data
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 */
	private List<PositionAvgPriceEntity> PreparePositionData(File file) {
		List<PositionAvgPriceEntity> list = new ArrayList<PositionAvgPriceEntity>();
		try {

			if (HazelcastConfig.getInstance().getPositionContract().isEmpty()) {
				appUtils.loadTokenForPosition();
			}
			FileInputStream input = new FileInputStream(file);

			Workbook workbook = WorkbookFactory.create(input);
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			System.out.println(sheet.getLastRowNum());
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				String strikePrice = "";
				PositionAvgPriceEntity tempPriceDTO = new PositionAvgPriceEntity();
				row = sheet.getRow(i);
				if (row != null) {
					String clientCode = row.getCell(0).toString();
					String exchange = row.getCell(1).toString();
					String insType = row.getCell(2).toString();
					String symbol = row.getCell(3).toString();
					String expiry = row.getCell(4).toString();
					if (symbol.equalsIgnoreCase("SENSEX") && exchange.equalsIgnoreCase("NFO")) {
						exchange = "BFO";
					}
					if (exchange.equalsIgnoreCase("CDS")) {
						strikePrice = row.getCell(5).toString();
					} else if (exchange.equalsIgnoreCase("MCX")) {
						double Strike = (Double) row.getCell(5).getNumericCellValue();
						strikePrice = String.valueOf(Strike);
					} else {
						double Strike = (Double) row.getCell(5).getNumericCellValue();
						strikePrice = String.valueOf(Strike);
					}
					String optType = row.getCell(6).toString();
					String tradetype = row.getCell(7).toString();
					long qty = (long) row.getCell(8).getNumericCellValue();
					double price = (Double) row.getCell(9).getNumericCellValue();
					String scripName = "";
					expiry = expiry.replace("-", "").toUpperCase();
					if (insType.startsWith("OPT")) {
						String[] strPrcArr = strikePrice.split("\\.");
						String decimal = strPrcArr[1].replace("0", "");
						if (decimal != null && !decimal.isEmpty()) {
							strikePrice = strPrcArr[0] + "." + decimal;
						} else {
							strikePrice = strPrcArr[0];
						}
						scripName = symbol + expiry + strikePrice + optType;
					} else {
						scripName = symbol + expiry + "FUT";
					}

					if (tradetype.equalsIgnoreCase("B")) {
						tempPriceDTO.setNetQty(String.valueOf(qty));
					} else {
						long value = qty * -1;
						tempPriceDTO.setNetQty(String.valueOf(value));
					}
					String instrName = scripName;

					String tokenandexchange = HazelcastConfig.getInstance().getPositionContract().get(instrName);
					if (tokenandexchange != null) {
						String[] token = tokenandexchange.split("_");
						tempPriceDTO.setInstrumentName(scripName.toUpperCase());
						tempPriceDTO.setClientId(clientCode.toUpperCase());
						tempPriceDTO.setExchange(exchange.toUpperCase());
						tempPriceDTO.setInstrumentType(insType.toUpperCase());
						tempPriceDTO.setSymbol(symbol.toUpperCase());
						tempPriceDTO.setExpiry(expiry.toUpperCase());
						tempPriceDTO.setStrikePrice(strikePrice.toUpperCase());
						tempPriceDTO.setOptionType(optType.toUpperCase());
						tempPriceDTO.setToken(token[1]);
						tempPriceDTO.setNetRate(String.valueOf(price));
						list.add(tempPriceDTO);
					} else {
						Log.info(instrName
								+ " - Instrument does not exist in contract master to insert position avg data");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return list;

	}

	/**
	 * 
	 * Method to load position avg file into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	public String loadPositionAvgFileIntoCache() {
		String response = "";
		try {
			List<String> userIds = positionsRepository.getUserId();
			if (StringUtil.isListNotNullOrEmpty(userIds)) {
				HazelcastConfig.getInstance().getPositionsAvgPrice().clear();
				for (String clientId : userIds) {
					List<PositionAvgPriceEntity> avgPriceEntities = positionsRepository.findAllByClientId(clientId);
					HazelcastConfig.getInstance().getPositionsAvgPrice().put(clientId, avgPriceEntities);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;

	}

	/**
	 * method to get position avg user
	 * 
	 * @author SOWMIYA
	 * 
	 */
	@Override
	public RestResponse<GenericResponse> getPositionAvgUser(PositionReqModel reqModel) {
		try {
			if (StringUtil.isNullOrEmpty(reqModel.getClientId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<PositionAvgPriceEntity> positionEntity = positionsRepository.findAllByClientId(reqModel.getClientId());
			if (positionEntity == null || positionEntity.size() <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(positionEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get Position count by exchange
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getPositionCountByExch() {
		PositionCountRespModel responseModel = new PositionCountRespModel();
		List<PositionResultModel> resultModel = new ArrayList<>();
		try {
			List<String> exchanges = positionsRepository.getDistinctExch();
			for (String exch : exchanges) {
				long getPositionCountByExch = positionsRepository.getPositionCountByExch(exch);
				if (getPositionCountByExch > 0) {
					PositionResultModel model = new PositionResultModel();
					model.setCount(getPositionCountByExch);
					model.setExchange(exch);
					resultModel.add(model);
				}
			}
			responseModel.setResult(resultModel);
			return prepareResponse.prepareSuccessResponseObject(responseModel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

}