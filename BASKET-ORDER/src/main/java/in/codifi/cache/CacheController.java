package in.codifi.cache;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.controller.spec.ICacheController;
import in.codifi.basket.model.response.GenericResponse;
import in.codifi.basket.service.spec.InterfaceCacheService;
import in.codifi.basket.utils.PrepareResponse;

@Path("/cache")
public class CacheController implements ICacheController {

	@Inject
	InterfaceCacheService cacheService;
	@Inject
	PrepareResponse prepareResponse;

//	/**
//	 * method to Load Cache
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Override
//	public RestResponse<CommonResponseModel> loadCache(RequestModel requestModel) {
//		if (requestModel == null)
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
//		if (!validationService.isValidUser(requestModel.getUserId()))
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER_SESSION);
//		return cacheService.loadCache();
//	}
//
//	/**
//	 * method to Clear Cache
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Override
//	public RestResponse<CommonResponseModel> clearCache(RequestModel requestModel) {
//		if (requestModel == null)
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
//		if (!validationService.isValidUser(requestModel.getUserId()))
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER_SESSION);
//		return cacheService.clearCache();
//	}
//
//	/**
//	 * method to Load Cache By UserId
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Override
//	public RestResponse<CommonResponseModel> cacheLoadByUserId(RequestModel requestModel) {
//		if (requestModel == null)
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
//		if (!validationService.isValidUser(requestModel.getUserId()))
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER_SESSION);
//		return cacheService.cacheLoadByUserId(requestModel);
//	}
//
//	/**
//	 * method to clear Cache By UserId
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Override
//	public RestResponse<CommonResponseModel> clearCacheByUserId(RequestModel requestModel) {
//		if (requestModel == null)
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
//		if (!validationService.isValidUser(requestModel.getUserId()))
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER_SESSION);
//		return cacheService.cacheLoadByUserId(requestModel);
//	}

	/**
	 * method to delete expiry scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteExpiry() {
		return cacheService.deleteExpiry();
	}

}
