package in.codifi.common.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.request.StockReturnReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface StockReturnControllerSpec {

	@POST
	@Path("/stockreturn/get")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getStockReturn(StockReturnReqModel reqModel);

}
