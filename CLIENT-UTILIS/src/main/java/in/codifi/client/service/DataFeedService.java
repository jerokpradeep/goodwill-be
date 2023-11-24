package in.codifi.client.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.client.config.HazelcastConfig;
import in.codifi.client.entity.primary.StockSuggestEntity;
import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.repository.StocksSuggestRepository;
import in.codifi.client.service.spec.IDataFeedService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class DataFeedService implements IDataFeedService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	StocksSuggestRepository stocksSuggestRepository;

	/**
	 * Method to get stocks for client
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getStocksForUser(ClientDetailsReqModel model) {

		List<StockSuggestEntity> entity = new ArrayList<>();
		try {

			// TODO need to change to actual user
			String userId = AppConstants.TEMP_USER_ID;

			/** Check if data exist in cache or not **/
			entity = HazelcastConfig.getInstance().getStocks().get(userId);

			/** if data exist in cache then return **/
			if (entity != null && entity.size() > 0)
				return prepareResponse.prepareSuccessResponseObject(entity);

			/** If data does not exist in cache load it and return **/
			entity = loadStockDataByUserId(userId);
			if (entity != null && entity.size() > 0)
				return prepareResponse.prepareSuccessResponseObject(entity);

			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to load stock data for user into cache by user id
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	public List<StockSuggestEntity> loadStockDataByUserId(String userId) {
		List<StockSuggestEntity> entities = new ArrayList<>();
		try {
			entities = stocksSuggestRepository.findByUserId(userId);
			if (entities != null && entities.size() > 0) {
				HazelcastConfig.getInstance().getStocks().remove(userId);
				HazelcastConfig.getInstance().getStocks().put(userId, entities);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return entities;
	}

	/**
	 * Method to load all stock data for user into cache
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	public RestResponse<GenericResponse> loadStockData() {
		try {
			List<String> userId = stocksSuggestRepository.getDistinctUserId();
			if (StringUtil.isListNullOrEmpty(userId))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			HazelcastConfig.getInstance().getStocks().clear();
			for (int i = 0; i < userId.size(); i++) {
				loadStockDataByUserId(userId.get(i));
			}
			return prepareResponse.prepareSuccessResponseMessage(AppConstants.CACHE_LOAD_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
