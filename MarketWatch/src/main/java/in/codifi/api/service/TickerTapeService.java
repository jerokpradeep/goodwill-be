package in.codifi.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.cache.HazelCacheController;
import in.codifi.api.entity.primary.TickerTapeEntity;
import in.codifi.api.model.ReqModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.repository.TickerTapeRepository;
import in.codifi.api.service.spec.ITickerTapeService;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.PrepareResponse;
import in.codifi.api.util.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class TickerTapeService implements ITickerTapeService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	TickerTapeRepository tickerTapeRepository;

	/**
	 * 
	 * Method to get ticker tape scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	@Override
	public RestResponse<ResponseModel> getTicketTapeScrips(ReqModel model) {
		List<TickerTapeEntity> entity = new ArrayList<>();
		try {

			// TODO need to change to actual user
			String userId = AppConstants.TEMP_USER_ID;

			/** Check if data exist in cache or not **/
			entity = HazelCacheController.getInstance().getTickerTapeList().get(userId);

			/** if data exist in cache then return **/
			if (entity != null && entity.size() > 0)
				return prepareResponse.prepareSuccessResponseObject(entity);

			/** If data does not exist in cache load it and return **/
			entity = loadTickerTapeScripsByUserId(userId);
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
	 * 
	 * Method to load ticker tape scrips for user into cache by user id
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	public List<TickerTapeEntity> loadTickerTapeScripsByUserId(String userId) {
		List<TickerTapeEntity> entities = new ArrayList<>();
		try {

			entities = tickerTapeRepository.findByUserId(userId);
			if (entities != null && entities.size() > 0) {
				HazelCacheController.getInstance().getTickerTapeList().remove(userId);
				HazelCacheController.getInstance().getTickerTapeList().put(userId, entities);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return entities;
	}

	/**
	 * 
	 * Method to load all Ticker Tape scrips for user into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public RestResponse<ResponseModel> loadTickerTapeScrips() {
		try {
			List<String> userId = tickerTapeRepository.getDistinctUserId();
			if (StringUtil.isListNullOrEmpty(userId))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			HazelCacheController.getInstance().getTickerTapeList().clear();
			for (int i = 0; i < userId.size(); i++) {
				loadTickerTapeScripsByUserId(userId.get(i));
			}
			return prepareResponse.prepareSuccessResponseObject(AppConstants.LOAD_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
