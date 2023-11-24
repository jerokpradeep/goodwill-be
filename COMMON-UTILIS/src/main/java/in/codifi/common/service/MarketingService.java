package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.MarketingEntity;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.reposirory.MarketingRepository;
import in.codifi.common.service.spec.MarketingServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class MarketingService implements MarketingServiceSpec {

	@Inject
	MarketingRepository marketingRepository;

	@Inject
	PrepareResponse prepareResponse;

	@Override
	public RestResponse<GenericResponse> getMarketingData() {
		try {
			List<MarketingEntity> marketingEntity = new ArrayList<MarketingEntity>();
			if (HazelcastConfig.getInstance().getMarketingEntity()
					.containsKey(AppConstants.HAZEL_KEY_MARKETING_CARDS)) {
				marketingEntity = HazelcastConfig.getInstance().getMarketingEntity()
						.get(AppConstants.HAZEL_KEY_MARKETING_CARDS);
			} else {
				marketingEntity = loadMarketingData();
			}

			if (StringUtil.isListNotNullOrEmpty(marketingEntity)) {
				return prepareResponse.prepareSuccessResponseObject(marketingEntity);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * 
	 * Method to get marketing cards
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<MarketingEntity> getMarketingCards() {
		List<MarketingEntity> marketingEntity = new ArrayList<>();
		try {
			marketingEntity = new ArrayList<MarketingEntity>();
			if (StringUtil.isListNotNullOrEmpty(
					HazelcastConfig.getInstance().getMarketingEntity().get(AppConstants.HAZEL_KEY_MARKETING_CARDS))) {
				marketingEntity = HazelcastConfig.getInstance().getMarketingEntity()
						.get(AppConstants.HAZEL_KEY_MARKETING_CARDS);
			} else {
				marketingEntity = loadMarketingData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return marketingEntity;
	}

	/*
	 * method to load marketing data
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public List<MarketingEntity> loadMarketingData() {
		List<MarketingEntity> marketingEntity = marketingRepository.findAll();
		try {
			if (StringUtil.isListNotNullOrEmpty(marketingEntity)) {
				HazelcastConfig.getInstance().getMarketingEntity().clear();
				HazelcastConfig.getInstance().getMarketingEntity().put(AppConstants.HAZEL_KEY_MARKETING_CARDS,
						marketingEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return marketingEntity;
	}

}
