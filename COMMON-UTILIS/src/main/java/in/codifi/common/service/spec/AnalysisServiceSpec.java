package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.model.request.CFRatioReq;
import in.codifi.common.model.request.QuarterlyTrendReq;
import in.codifi.common.model.response.GenericResponse;

public interface AnalysisServiceSpec {

	/**
	 * method to get top gainers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> getTopGainers();

	/**
	 * method to get top losers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> getTopLosers();

	/**
	 * method to get top losers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> getFiftyTwoWeekHigh(String url);

	/**
	 * method to get fifty two week low
	 * 
	 * @author sowmiya
	 */
	RestResponse<GenericResponse> getFiftyTwoWeekLow(String url);

	/**
	 * method to get analysis data
	 * 
	 * @author sowmiya
	 */
	RestResponse<GenericResponse> getAnalysisData(String url);

}
