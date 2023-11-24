package in.codifi.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.model.response.GenericResponse;
import in.codifi.basket.repository.BasketOrderEntityManager;
import in.codifi.basket.service.spec.InterfaceCacheService;
import in.codifi.basket.utils.AppConstants;
import in.codifi.basket.utils.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class CacheService implements InterfaceCacheService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	BasketOrderEntityManager basketOrderEntityManager;

	/**
	 * 
	 * Method to get only basket names by userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
//	private List<BasketNameEntity> getBasketDetailsByUserId(String userId) {
//		List<BasketNameEntity> basketNameEntity = new ArrayList<>();
//		try {
//			basketNameEntity = basketOrderRepository.findAllByUserId(userId);
//			if (StringUtil.isListNotNullOrEmpty(basketNameEntity)) {
//				basketNameEntity = HazleCacheController.getInstance().getBasketOrderDto().put(userId, basketNameEntity);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.error(e.getMessage());
//		}
//		return basketNameEntity;
//	}

	/**
	 * method to Load Cache
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	@SuppressWarnings("unused")
//	@Override
//	public RestResponse<CommonResponseModel> loadCache() {
//		try {
//			List<String> getAllUserId = basketOrderRepository.getUserId();
//			for (int i = 0; i < getAllUserId.size(); i++) {
//				List<BasketNameEntity> basketNameEntity = getBasketDetailsByUserId(getAllUserId.get(i));
//			}
//			if (HazleCacheController.getInstance().getBasketOrderDto().getClass() != null)
//				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.info(e.getMessage());
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//	}

	/**
	 * method to Clear Cache
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	@Override
//	public RestResponse<CommonResponseModel> clearCache() {
//		try {
//			HazleCacheController.getInstance().getBasketOrderDto().clear();
//			return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.info(e.getMessage());
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//	}

	/**
	 * method to Load Cache By UserId
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	@Override
//	public RestResponse<CommonResponseModel> cacheLoadByUserId(RequestModel requestModel) {
//		try {
//			List<BasketNameEntity> basketNameEntity = getBasketDetailsByUserId(requestModel.getUserId());
//			basketNameEntity = HazleCacheController.getInstance().getBasketOrderDto().put(requestModel.getUserId(),
//					basketNameEntity);
//			if (HazleCacheController.getInstance().getBasketOrderDto().get(requestModel.getUserId()) != null)
//				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.info(e.getMessage());
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//	}

	/**
	 * method to clear Cache By UserId
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	@Override
//	public RestResponse<CommonResponseModel> clearCacheByUserId(RequestModel requestModel) {
//		try {
//			HazleCacheController.getInstance().getBasketOrderDto().get(requestModel.getUserId()).clear();
//			if (HazleCacheController.getInstance().getBasketOrderDto().get(requestModel.getUserId()) == null)
//				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.info(e.getMessage());
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//	}

	/**
	 * method to delete expiry scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteExpiry() {
		try {
			return basketOrderEntityManager.deleteExpiredScrip();
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
