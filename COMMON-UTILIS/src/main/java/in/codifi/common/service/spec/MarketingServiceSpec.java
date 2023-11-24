package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

public interface MarketingServiceSpec {

	/**
	 * method to get marketing data
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getMarketingData();

}
