package in.codifi.common.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.InvestmentDashBoardRespModel;

public interface InvestmentControllerSpec {

	/**
	 * 
	 * Method to get Investment dash board info for mobile
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<InvestmentDashBoardRespModel> getInvestmentInfo();

}
