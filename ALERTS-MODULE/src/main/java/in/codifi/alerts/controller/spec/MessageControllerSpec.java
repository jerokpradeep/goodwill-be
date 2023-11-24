package in.codifi.alerts.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.alerts.model.response.GenericResponse;

public interface MessageControllerSpec {

	/**
	 * method to get exch message
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Path("/exch")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> exchMsg(@QueryParam("exchange") String exchange);

	/**
	 * method to get brokerage message
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@GET
	@Path("/broker")
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getBrokerageMsg();

	/**
	 * method to get exchange status
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@GET
	@Path("/exch/status")
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getExchStatus();

}