package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.AccessLogReqModel;
import in.codifi.admin.req.model.LogsRequestModel;

public interface LogsServiceSpec {

	/**
	 * method to check the access log table if exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> checkAccessLogTable();

	/**
	 * method to check the access log table if exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> checkRestAccessLogTable();

	/**
	 * method to get access log in database
	 * 
	 * @author SOWMIYA
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getAccessLogs(AccessLogReqModel reqModel);

	/**
	 * method to get access log in database
	 * 
	 * @author SOWMIYA
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getAccessLogsInDB(AccessLogReqModel reqModel);

	/**
	 * method to get the access log table with pageable
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	RestResponse<GenericResponse> getAccessLogTablewithPageable(LogsRequestModel reqModel);

	/**
	 * method to check 24 rest access log tables
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	RestResponse<GenericResponse> check24RestAccessLogTables();

	/**
	 * method to get rest access logs
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getRestAccessLogs(LogsRequestModel reqModel);

}
