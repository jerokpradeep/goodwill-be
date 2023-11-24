package in.codifi.common.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.controller.spec.StockReturnControllerSpec;
import in.codifi.common.model.request.StockReturnReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.spec.StockReturnServiceSpec;

@Path("/stock")
public class StockReturnController implements StockReturnControllerSpec {
	@Inject
	StockReturnServiceSpec service;

	/**
	 * method to get stock returns
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<GenericResponse> getStockReturn(StockReturnReqModel reqModel) {
		return service.getStockReturn(reqModel);
	}

}
