package in.codifi.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.model.response.GenericResponse;
import in.codifi.scrips.repository.ScripSearchEntityManager;
import in.codifi.scrips.service.ContractService;
import in.codifi.scrips.utility.AppConstants;
import in.codifi.scrips.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class CacheService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ScripSearchEntityManager entityManager;

	@Inject
	ContractService contractService;

	/**
	 * 
	 * Method to reload a cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> reloadCache() {
		try {
//			Log.info("Started to clear cache");
//			HazelcastConfig.getInstance().getFetchDataFromCache().clear();
//			HazelcastConfig.getInstance().getDistinctSymbols().clear();
//			HazelcastConfig.getInstance().getLoadedSearchData().clear();
//			HazelcastConfig.getInstance().getFetchDataFromCache().clear();
//			Log.info("Cache cleared and started to load new data");
//			HazelcastConfig.getInstance().getFetchDataFromCache().put(AppConstants.FETCH_DATA_FROM_CACHE, true);
//			entityManager.loadDistintValue(2);
//			entityManager.loadDistintValue(3);
//			contractService.loadContractMaster();
//			Log.info("Cache loaded sucessfully");
			contractService.loadContractIntoCache();
			return prepareResponse.prepareSuccessMessage(AppConstants.CACHE_LOADED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to lear search data from cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> clearSearchData() {
		try {
			Log.info("Started to clear cache");
			HazelcastConfig.getInstance().getLoadedSearchData().clear();
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
