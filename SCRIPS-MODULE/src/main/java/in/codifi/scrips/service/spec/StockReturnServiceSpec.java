package in.codifi.scrips.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.model.response.GenericResponse;

public interface StockReturnServiceSpec {
	/**
	 * method to load stock return
	 * 
	 * @author sowmiya
	 * @return
	 */
	RestResponse<GenericResponse> loadStockReturn();

}
