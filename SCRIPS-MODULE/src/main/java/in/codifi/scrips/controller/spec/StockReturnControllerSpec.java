package in.codifi.scrips.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.model.response.GenericResponse;

public interface StockReturnControllerSpec {

	/**
	 * method to load stock returns
	 * 
	 * @author sowmiya
	 * @return
	 */
	@Path("/load")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> loadStockReturn();

}
