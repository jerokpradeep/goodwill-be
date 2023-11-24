package in.codifi.scrips.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.controller.spec.StockReturnControllerSpec;
import in.codifi.scrips.model.response.GenericResponse;
import in.codifi.scrips.service.spec.StockReturnServiceSpec;

@Path("/stock")
public class StockReturnController implements StockReturnControllerSpec {
	@Inject
	StockReturnServiceSpec service;

	@Override
	public RestResponse<GenericResponse> loadStockReturn() {
		return service.loadStockReturn();
	}

}
