package in.codifi.common.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

public interface ScanControllerSpec {

	/**
	 * method to get equity scan details by scan master model
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/equity/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> findEquityScan();

	/**
	 * method to get equtiy scan from data base
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/equity/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> findEquityScanAll();

	/**
	 * method to get Future Scan details by scan master model
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/future/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> findFutureScan();

	/**
	 * method to get Future Scan details from database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/future/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> findFutureScanAll();

}
