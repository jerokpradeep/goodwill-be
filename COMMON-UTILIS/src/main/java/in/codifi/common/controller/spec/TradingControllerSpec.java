package in.codifi.common.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.TradingDashBoardRespModel;

public interface TradingControllerSpec {

	/**
	 * 
	 * Method to get trading dash board info for mobile
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<TradingDashBoardRespModel> getTradingInfo();

}
