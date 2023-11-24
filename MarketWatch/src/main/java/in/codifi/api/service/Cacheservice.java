package in.codifi.api.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.cache.MwCacheController;
import in.codifi.api.model.CacheMwAdvDetailsModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.repository.MarketWatchEntityManager;
import in.codifi.api.repository.MarketWatchNameRepository;
import in.codifi.api.service.spec.ICacheService;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.PrepareResponse;

@ApplicationScoped
public class Cacheservice implements ICacheService {

	@Inject
	MarketWatchNameRepository mwNameRepo;
	@Inject
	MarketWatchService marketWatchService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	MarketWatchEntityManager entityManager;
	@Inject
	AdvanceMWService mwService;

	/**
	 * Method to load all MW data into cache
	 *
	 * @author Gowri Sankar
	 */
	public RestResponse<ResponseModel> loadUserMWData() {
		try {
			/*
			 * take the scrip details from the Data base for the All user
			 */
//			List<IMwTblResponse> scripDetails = mwNameRepo.getUserScripDetailsForAllUser();
			List<CacheMwAdvDetailsModel> scripDetails = entityManager.getAllMarketWatch();
			if (scripDetails != null && scripDetails.size() > 0) {
				MwCacheController.getAdvanceMWListByUserId().clear();
				marketWatchService.populateFieldsMWForAll(scripDetails);
				prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_MW);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to load pre defined market watch into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public RestResponse<ResponseModel> loadPreDefinedMWData() {

		try {
			mwService.loadPredefinedMWData();
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
