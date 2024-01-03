package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.LogsControllerSpec;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.AccessLogReqModel;
import in.codifi.admin.req.model.LogsRequestModel;
import in.codifi.admin.service.spec.LogsServiceSpec;

@Path("/log")
public class LogsController implements LogsControllerSpec {

	@Inject
	LogsServiceSpec logsServiceSpec;

	/**
	 * Method to get error logs in database
	 * 
	 * @author SOWMIYA
	 * @return
	 */

	@Override
	public RestResponse<GenericResponse> getAccessLogs(AccessLogReqModel reqModel) {
		return logsServiceSpec.getAccessLogs(reqModel);
	}

	/**
	 * Method to get error logs in database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getAccessLogsInDB(AccessLogReqModel reqModel) {
		return logsServiceSpec.getAccessLogsInDB(reqModel);
	}

	/**
	 * method to get the access log table with pageable
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getAccessLogTablewithPageable(LogsRequestModel reqModel) {
		return logsServiceSpec.getAccessLogTablewithPageable(reqModel);
	}

	/*
	 * method to check the rest access log table if exist or not
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> check24RestAccessLogTables() {
		return logsServiceSpec.check24RestAccessLogTables();
	}

	/**
	 * method to check 24 access log table if not exits means to create
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	public RestResponse<GenericResponse> checkAccessLogTable() {
		return logsServiceSpec.checkAccessLogTable();
	}

	/**
	 * method get rest access logs
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @return
	 */
	public RestResponse<GenericResponse> getRestAccessLogs(LogsRequestModel reqModel) {
		return logsServiceSpec.getRestAccessLogs(reqModel);
	}
}
