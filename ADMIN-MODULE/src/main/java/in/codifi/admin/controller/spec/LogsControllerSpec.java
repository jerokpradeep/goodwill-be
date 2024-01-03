package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.AccessLogReqModel;
import in.codifi.admin.req.model.LogsRequestModel;

public interface LogsControllerSpec {

	/**
	 * Method to get error logs from database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/getlogs")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getAccessLogs(AccessLogReqModel reqModel);

	/**
	 * Method to get error logs from database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/getrestlogsInDB")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getAccessLogsInDB(AccessLogReqModel reqModel);

	/**
	 * method to get the access log table with pageable
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	@Path("/getaccesslog")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getAccessLogTablewithPageable(LogsRequestModel reqModel);

	/**
	 * method check 24 rest access log tables
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @return
	 */
	@Path("/create/rest/24tables")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> check24RestAccessLogTables();

	/**
	 * method check 24 access log tables
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @return
	 */
	@Path("/create/access/24tables")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> checkAccessLogTable();

	/**
	 * method to get rest access logs
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @return
	 */
	@Path("/getrestlogs")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getRestAccessLogs(LogsRequestModel reqModel);

}
