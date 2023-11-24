package in.codifi.holdings.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.holdings.model.request.HoldingsReqModel;
import in.codifi.holdings.model.response.GenericResponse;

public interface HoldingsControllerSpec {

	/**
	 * Method to get CNC holding data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getCNCHoldings();

	/**
	 * Method to get MTF holding data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Path("/mtf")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getMTFHoldings();

	/**
	 * Method to get Holdings data by product
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Path("/product")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getHoldingsByProduct(HoldingsReqModel reqModel);

	/**
	 * Method to get non POA CNC holdings
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/nonpoa/cnc")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getCNCNonPOAHoldings();

	/**
	 * Method to get non POA MTF Holdings data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Path("/nonpoa/mtf")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getMTFNonPOAHoldings();
}
