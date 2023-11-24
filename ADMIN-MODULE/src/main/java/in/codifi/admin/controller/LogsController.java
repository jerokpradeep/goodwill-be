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
	
	

}
