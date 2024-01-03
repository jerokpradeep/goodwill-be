package in.codifi.admin.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.AccessLogModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.repository.LogsDatabaseConnection;
import in.codifi.admin.req.model.AccessLogReqModel;
import in.codifi.admin.req.model.LogsRequestModel;
import in.codifi.admin.resp.model.AccessLogRespModel;
import in.codifi.admin.service.spec.LogsServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class LogsService implements LogsServiceSpec {
	@Inject
	LogsDatabaseConnection repository;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to check the access log table if exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> checkAccessLogTable() {
		try {
			/** to get total number of table names from specific database **/
			List<String> existingTable = repository.getExistingTables();
			List<String> tableToCreate = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			String tableName = "";
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.plusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
				String formattedDate = dateTimeFormatter.format(local);
				for (int j = 0; j <= 23; j++) {
					String jFormatted = String.format("%02d", j);
					tableName = "tbl_" + formattedDate + "_access_log_" + jFormatted;
					if (!existingTable.contains(tableName)) {
						tableToCreate.add(tableName);
					}
				}
			}
			/** if table not exist from database to create the tables **/
			repository.createTables(tableToCreate);
			return prepareResponse.prepareSuccessResponseObject(AppConstants.TABLES_CREATED);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to check rest access log table
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public RestResponse<GenericResponse> checkRestAccessLogTable() {
		try {
			/** to get total number of table names from specific database **/
			List<String> existingTable = repository.getExistingTables();
			List<String> tableToCreate = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			String tableName = "";
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.plusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_rest_access_log";
				if (!existingTable.contains(tableName)) {
					tableToCreate.add(tableName);
				}

			}
			/** if table not exist from database to create the tables **/
			repository.createRestAccessTables(tableToCreate);
			return prepareResponse.prepareSuccessResponseObject(AppConstants.TABLES_CREATED);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get access log in database
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> getAccessLogs(AccessLogReqModel reqModel) {
		try {
			if (!validateLogReq(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<String> tableNames = new ArrayList<>();
			String from = reqModel.getFromDate();
			String to = reqModel.getToDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			LocalDate fromDate = LocalDate.parse(from, formatter);
			LocalDate toDate = LocalDate.parse(to, formatter);
			for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {
				String tableName = "tbl_" + date.format(formatter) + "_rest_access_log";
				tableNames.add(tableName);
			}
			List<AccessLogRespModel> accessRespModel = repository.getRestAccessLogs(tableNames, reqModel.getUserId());
			if (StringUtil.isListNotNullOrEmpty(accessRespModel)) {
				return prepareResponse.prepareSuccessResponseObject(accessRespModel);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get access log in database
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> getAccessLogsInDB(AccessLogReqModel reqModel) {
		try {
			if (!validateLogReq(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<String> tableNames = new ArrayList<>();
			String from = reqModel.getFromDate();
			String to = reqModel.getToDate();
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			LocalDate fromDate = LocalDate.parse(from, inputFormatter);
			LocalDate toDate = LocalDate.parse(to, inputFormatter);
			for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {
				String tableName = "tbl_" + date.format(formatter) + "_rest_access_log";
				tableNames.add(tableName);
			}
			List<AccessLogRespModel> accessRespModel = repository.getRestAccessLogsFromTables(tableNames,
					reqModel.getUserId(), reqModel.getFromDate(), reqModel.getToDate(), reqModel.getPageNo(),
					reqModel.getPageSize());
			if (StringUtil.isListNotNullOrEmpty(accessRespModel)) {
				return prepareResponse.prepareSuccessResponseObject(accessRespModel);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate log req
	 * 
	 * @author SOWMIYA
	 * @param reqModel
	 * @return
	 */
	private boolean validateLogReq(AccessLogReqModel reqModel) {
		if (StringUtil.isNotNullOrEmpty(reqModel.getFromDate()) && StringUtil.isNotNullOrEmpty(reqModel.getToDate())) {
			return true;
		}
		return false;
	}

	/**
	 * method to get the access log table with pagination
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	public RestResponse<GenericResponse> getAccessLogTablewithPageable(LogsRequestModel reqModel) {

		try {
			if (!validateaccessLogReq(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<String> tableNames = new ArrayList<>();

			String from = reqModel.getFromDate();
			String to = reqModel.getToDate();
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date fromDate1 = inputFormat.parse(from);
			Date toDate1 = inputFormat.parse(to);
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			LocalDate fromDate = LocalDate.parse(from, inputFormatter);
			LocalDate toDate = LocalDate.parse(to, inputFormatter);
			String Fromhour = new SimpleDateFormat("HH").format(fromDate1);
			String toHour = new SimpleDateFormat("HH").format(toDate1);
			int fromDateHours = Integer.valueOf(Fromhour);
			int toDateHours = Integer.valueOf(toHour);
			String toDateTableName = "tbl_" + toDate.format(formatter) + "_access_log_" + toDateHours;
			int i = 0;
			for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {
				for (i = fromDateHours; i <= 23; i++) {
					String jFormatted = String.format("%02d", i);
					String tableName = "tbl_" + date.format(formatter) + "_access_log_" + jFormatted;
					if (!tableName.equalsIgnoreCase(toDateTableName)) {
						tableNames.add(tableName);
						fromDateHours++;
					} else {
						break;
					}
				}
				fromDateHours = 00;

			}
			tableNames.add(toDateTableName);
			List<AccessLogModel> logPageableModel = new ArrayList<>();
			logPageableModel = repository.getAccessTableswithPage(tableNames, reqModel.getUserId(), reqModel.getUri(),
					reqModel.getFromDate(), reqModel.getToDate(), reqModel.getPageNo(), reqModel.getPageSize());
			if (StringUtil.isListNotNullOrEmpty(logPageableModel)) {
				return prepareResponse.prepareSuccessResponseObject(logPageableModel);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get rest access logs from database
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @return
	 */
	public RestResponse<GenericResponse> getRestAccessLogs(LogsRequestModel reqModel) {
		try {
			if (!validateaccessLogReq(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			List<String> tableNames = new ArrayList<>();

			String from = reqModel.getFromDate();
			String to = reqModel.getToDate();
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date fromDate1 = inputFormat.parse(from);
			Date toDate1 = inputFormat.parse(to);
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			LocalDate fromDate = LocalDate.parse(from, inputFormatter);
			LocalDate toDate = LocalDate.parse(to, inputFormatter);
			String Fromhour = new SimpleDateFormat("HH").format(fromDate1);
			String toHour = new SimpleDateFormat("HH").format(toDate1);
			int fromDateHours = Integer.valueOf(Fromhour);
			int toDateHours = Integer.valueOf(toHour);
			String toDateTableName = "tbl_" + toDate.format(formatter) + "_rest_access_log_" + toDateHours;
			int i = 0;
			for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {
				for (i = fromDateHours; i <= 23; i++) {
					String jFormatted = String.format("%02d", i);
					String tableName = "tbl_" + date.format(formatter) + "_rest_access_log_" + jFormatted;
					if (!tableName.equalsIgnoreCase(toDateTableName)) {
						tableNames.add(tableName);
						fromDateHours++;
					} else {
						break;
					}
				}
				fromDateHours = 00;

			}
			tableNames.add(toDateTableName);
			List<AccessLogRespModel> logPageableModel = new ArrayList<>();
			logPageableModel = repository.getRestAccessTableswithPage(tableNames, reqModel.getUserId(),
					reqModel.getUri(), reqModel.getFromDate(), reqModel.getToDate(), reqModel.getPageNo(),
					reqModel.getPageSize());
			if (StringUtil.isListNotNullOrEmpty(logPageableModel)) {
				return prepareResponse.prepareSuccessResponseObject(logPageableModel);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getRestAccessLogs", e);

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate access log request
	 * 
	 * @param reqModel
	 * @return
	 */
	private boolean validateaccessLogReq(LogsRequestModel reqModel) {
		if (StringUtil.isNotNullOrEmpty(reqModel.getFromDate()) && StringUtil.isNotNullOrEmpty(reqModel.getToDate())
				&& reqModel.getPageNo() >= 0 && reqModel.getPageSize() >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * method to check 24 rest access log table
	 * 
	 * @author SowmiyaThangaraj
	 */
	/*
	 * method to check the access log table if exist or not
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> check24RestAccessLogTables() {
		try {
			/** to get total number of table names from specific database **/
			List<String> existingTable = repository.getExistingTables();
			List<String> tableToCreate = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			String tableName = "";
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.plusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
				String formattedDate = dateTimeFormatter.format(local);
				for (int j = 0; j <= 23; j++) {
					String jFormatted = String.format("%02d", j);
					tableName = "tbl_" + formattedDate + "_rest_access_log_" + jFormatted;
					if (!existingTable.contains(tableName)) {
						tableToCreate.add(tableName);
					}
				}
			}
			/** if table not exist from database to create the tables **/
			repository.createRestAccessTables(tableToCreate);
			return prepareResponse.prepareSuccessResponseObject(AppConstants.TABLES_CREATED);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
