package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.TradingDashBoardRespModel;

public interface TradingServiceSpec {

	/**
	 * 
	 * Method to get trading dash board info for mobile
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	RestResponse<TradingDashBoardRespModel> getTradingInfo(String userId);

}
