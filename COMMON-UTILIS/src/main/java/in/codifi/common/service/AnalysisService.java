package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.AnalysisRespModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.config.RestServiceProperties;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.AnnoucementsDataRepository;
import in.codifi.common.service.spec.AnalysisServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import in.codifi.common.ws.service.AnalysisRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AnalysisService implements AnalysisServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AnalysisRestService analysisRestService;
	@Inject
	RestServiceProperties props;
	@Inject
	AnnoucementsDataRepository annoucementsDataRepository;
	@Inject
	ContractEntityManger contractEntityManger;

	/**
	 * method to get top gainers details from server
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> getTopGainers() {
		List<AnalysisRespModel> response = new ArrayList<>();
		try {
			long lastGenTime = 0;
			String topGainerKey = props.getTopGainersUrl() + "_" + "Bullish";
			String url = props.getTopGainersUrl();
			if (HazelcastConfig.getInstance().getAnalysisUpdateTime().get(topGainerKey) != null) {
				lastGenTime = HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url);
			}
			long timeDiff = System.currentTimeMillis() - lastGenTime;
			if (timeDiff < 120000 && HazelcastConfig.getInstance().getAnalysistopGainers().get(topGainerKey) != null) {
				response = HazelcastConfig.getInstance().getAnalysistopGainers().get(url);
				return prepareResponse.prepareSuccessResponseObject(response);
			} else {
				List<AnalysisRespModel> result = analysisRestService.getFundamentalAnalysisData(url);
				if (result != null && result.size() > 0) {
					for (AnalysisRespModel model : result) {
						String direction = model.getDirection();
						if (StringUtil.isNotNullOrEmpty(direction) && direction.equalsIgnoreCase("Bullish")) {
							response.add(model);
						}
					}
					HazelcastConfig.getInstance().getAnalysistopGainers().clear();
					HazelcastConfig.getInstance().getAnalysistopGainers().put(topGainerKey, response);
					HazelcastConfig.getInstance().getAnalysisUpdateTime().put(AppConstants.UPDATED_TIME, lastGenTime);
					return prepareResponse.prepareSuccessResponseObject(response);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.INTERNAL_ERROR);
				}
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get top losers from server
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> getTopLosers() {
		List<AnalysisRespModel> response = new ArrayList<>();
		try {
			long lastGenTime = 0;
			String topLoser = props.getTopGainersUrl() + "_" + "Bearish";
			String url = props.getTopGainersUrl();
			if (HazelcastConfig.getInstance().getAnalysisUpdateTime().get(topLoser) != null) {
				lastGenTime = HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url);
			}
			long timeDiff = System.currentTimeMillis() - lastGenTime;
			if (timeDiff < 120000 && HazelcastConfig.getInstance().getAnalysistopLosers().get(topLoser) != null) {
				response = HazelcastConfig.getInstance().getAnalysistopLosers().get(topLoser);
				return prepareResponse.prepareSuccessResponseObject(response);
			} else {

				List<AnalysisRespModel> result = analysisRestService.getFundamentalAnalysisData(url);
				if (result != null && result.size() > 0) {
					for (AnalysisRespModel model : result) {
						String direction = model.getDirection();
						if (StringUtil.isNotNullOrEmpty(direction) && direction.equalsIgnoreCase("Bearish")) {
							response.add(model);
						}
					}
					HazelcastConfig.getInstance().getAnalysistopLosers().clear();
					HazelcastConfig.getInstance().getAnalysistopLosers().put(topLoser, response);
					HazelcastConfig.getInstance().getAnalysisUpdateTime().put(AppConstants.UPDATED_TIME, lastGenTime);
					return prepareResponse.prepareSuccessResponseObject(response);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.INTERNAL_ERROR);
				}
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get analysis data
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getFiftyTwoWeekHigh(String url) {
		List<AnalysisRespModel> response = new ArrayList<>();
		try {
			long lastGenTime = 0;
			if (HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url) != null) {
				lastGenTime = HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url);
			}
			long timeDiff = System.currentTimeMillis() - lastGenTime;
			if (timeDiff < 120000 && HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekHigh().get(url) != null) {
				response = HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekHigh().get(url);
				return prepareResponse.prepareSuccessResponseObject(response);
			} else {
				response = analysisRestService.getFundamentalAnalysisData(url);
				if (response != null && response.size() > 0) {
					HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekHigh().clear();
					HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekHigh().put(url, response);
					HazelcastConfig.getInstance().getAnalysisUpdateTime().put(AppConstants.UPDATED_TIME, lastGenTime);
					return prepareResponse.prepareSuccessResponseObject(response);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.INTERNAL_ERROR);
				}
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get analysis data
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getAnalysisData(String url) {
		List<AnalysisRespModel> response = new ArrayList<>();
		try {
			long lastGenTime = 0;
			if (HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url) != null) {
				lastGenTime = HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url);
			}
			long timeDiff = System.currentTimeMillis() - lastGenTime;
			if (timeDiff < 120000 && HazelcastConfig.getInstance().getAnalysisData().get(url) != null) {
				response = HazelcastConfig.getInstance().getAnalysisData().get(url);
				return prepareResponse.prepareSuccessResponseObject(response);
			} else {
				response = analysisRestService.getFundamentalAnalysisData(url);
				if (response != null && response.size() > 0) {
					HazelcastConfig.getInstance().getAnalysisData().clear();
					HazelcastConfig.getInstance().getAnalysisData().put(url, response);
					HazelcastConfig.getInstance().getAnalysisUpdateTime().put(AppConstants.UPDATED_TIME, lastGenTime);
					return prepareResponse.prepareSuccessResponseObject(response);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.INTERNAL_ERROR);
				}
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get analysis data
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getFiftyTwoWeekLow(String url) {
		List<AnalysisRespModel> response = new ArrayList<>();
		try {
			long lastGenTime = 0;
			if (HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url) != null) {
				lastGenTime = HazelcastConfig.getInstance().getAnalysisUpdateTime().get(url);
			}
			long timeDiff = System.currentTimeMillis() - lastGenTime;
			if (timeDiff < 120000 && HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekLow().get(url) != null) {
				response = HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekLow().get(url);
				return prepareResponse.prepareSuccessResponseObject(response);
			} else {
				response = analysisRestService.getFundamentalAnalysisData(url);
				if (response != null && response.size() > 0) {
					HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekLow().clear();
					HazelcastConfig.getInstance().getAnalysisfiftyTwoWeekLow().put(url, response);
					HazelcastConfig.getInstance().getAnalysisUpdateTime().put(AppConstants.UPDATED_TIME, lastGenTime);
					return prepareResponse.prepareSuccessResponseObject(response);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.INTERNAL_ERROR);
				}
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
