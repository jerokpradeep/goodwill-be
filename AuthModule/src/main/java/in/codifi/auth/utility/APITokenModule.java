package in.codifi.auth.utility;

import java.util.Calendar;

import javax.ws.rs.NotAuthorizedException;

import in.codifi.auth.config.HazelcastConfig;
import in.com.sas.util.TokenObject;

public class APITokenModule {

	public static boolean storeTokenCache(String token, String custId) {
		String storeToken = HazelcastConfig.getInstance().getUserIdKeyCacheAPI().get(custId);
		TokenObject user = null;
		if (StringUtil.isNotNullOrEmpty(storeToken)) {
			user = HazelcastConfig.getInstance().getTokenKeyCacheAPI().get(storeToken);
		}

		/*
		 * User token already exists but user logged in again. This will invalidate the
		 * existing login from any other device Comment out the following code part IF
		 * MULTIPLE USER LOGINS ARE ALLOWED
		 */
		if (storeToken != null && user != null) {
			if (user.getUser().equalsIgnoreCase(custId)) {
				HazelcastConfig.getInstance().getTokenKeyCacheAPI().remove(storeToken);
			}
		}

		/* Store the token with object as value. Set expiry till midnight */
		final TokenObject tokenObj = new TokenObject();
		tokenObj.setExpiry(CommonUtils.getExpiryInMilleSeconds());
		tokenObj.setUser(custId);
		HazelcastConfig.getInstance().getTokenKeyCacheAPI().put(token, tokenObj);
		HazelcastConfig.getInstance().getUserIdKeyCacheAPI().put(custId, token);
		HazelcastConfig.getInstance().getRequestPerSecondAPI().put(custId, System.currentTimeMillis());

		return true;
	}

	/**
	 * Method validates the provided token and returns the user id for processing
	 * 
	 * @param pToken
	 */
	public static void validateToken(String pToken, String userId, String path) throws Exception {
		TokenObject tokenObj = HazelcastConfig.getInstance().getTokenKeyCacheAPI().get(pToken);
		String userToken = HazelcastConfig.getInstance().getApiUser256Cache().get(userId);
		if (!pToken.equalsIgnoreCase(userToken)
				|| (tokenObj == null || tokenObj.getExpiry() < Calendar.getInstance().getTimeInMillis()
						|| !tokenObj.getUser().equalsIgnoreCase(userId))) {
			throw new NotAuthorizedException("Not Authorized");
		}

		/** * Check Current Time, Store it */
//		if (CSEnvVariables.getMethodNames(AppConstants.ACCESS_RESTRICTED_METHODS).contains(path)) {
//			long apiRequestCount = HazleCacheController.getInstance().getApiRequestCount().get(userId);
//			if (apiRequestCount > 0) {
//				HazleCacheController.getInstance().getApiRequestCount().put(userId, apiRequestCount - 1);
//			} else {
//				throw new NotAcceptableException("Request level reached");
//			}
//		}

//		/** * Check Current Time, Store it */
//		if (CSEnvVariables.getMethodNames(AppConstants.ACCESS_RESTRICTED_METHODS).contains(path)) {
//			// int mill
//			if ((requestPerSecond.get(userId) + 1000) <= System.currentTimeMillis()) {
//				requestPerSecond.put(userId, System.currentTimeMillis());
//			} else { // error message
//				throw new NotAcceptableException("Not Acceptable");
//			}
//		}
	}
}
