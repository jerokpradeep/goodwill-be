package in.codifi.common.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

public interface AnalysisControllerSpec {

	/**
	 * Method to get top gainers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/topgainers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getTopGainers();

	/**
	 * Method to get top losers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/toplosers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getTopLosers();

	/**
	 * Method to get 52 week high details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/52weekhigh")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> get52WeekHigh();

	/**
	 * Method to get 52 week low details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/52weeklow")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> get52WeekLow();

}
