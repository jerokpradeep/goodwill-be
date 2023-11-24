package in.codifi.basket.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.model.response.GenericResponse;

public interface InterfaceCacheService {

	/**
	 * method to Load Cache
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	RestResponse<CommonResponseModel> loadCache();

	/**
	 * method to Clear Cache
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	RestResponse<CommonResponseModel> clearCache();

	/**
	 * method to Load Cache By UserId
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	RestResponse<CommonResponseModel> cacheLoadByUserId(RequestModel requestModel);

	/**
	 * method to clear Cache By UserId
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	RestResponse<CommonResponseModel> clearCacheByUserId(RequestModel requestModel);

	/**
	 * method to delete expiry scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> deleteExpiry();

}
