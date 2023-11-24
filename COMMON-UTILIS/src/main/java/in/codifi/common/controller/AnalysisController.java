package in.codifi.common.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.config.RestServiceProperties;
import in.codifi.common.controller.spec.AnalysisControllerSpec;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.spec.AnalysisServiceSpec;
import in.codifi.common.utility.AppUtil;
import in.codifi.common.utility.PrepareResponse;

@Path("/analysis")
public class AnalysisController implements AnalysisControllerSpec {

	@Inject
	AnalysisServiceSpec analysisService;
	@Inject
	RestServiceProperties props;
	@Inject
	AppUtil appUtil;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to get top gainers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getTopGainers() {
		return analysisService.getTopGainers();
	}

	/**
	 * Method to get top Losers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getTopLosers() {
		return analysisService.getTopLosers();
	}

	/**
	 * Method to get top gainers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> get52WeekHigh() {
		String url = props.getFiftyTwoWeekHigh();
		return analysisService.getFiftyTwoWeekHigh(url);
	}

	/**
	 * Method to get top gainers details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> get52WeekLow() {
		String url = props.getFiftyTwoWeekLow();
		return analysisService.getFiftyTwoWeekLow(url);
	}

}
