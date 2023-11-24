package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.request.StockReturnReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface StockReturnServiceSpec {

	/**
	 * method to load stock return
	 * 
	 * @author sowmiya
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getStockReturn(StockReturnReqModel reqModel);

}
