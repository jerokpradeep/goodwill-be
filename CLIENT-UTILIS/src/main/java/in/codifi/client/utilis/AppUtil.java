package in.codifi.client.utilis;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ProductMasterModel;
import in.codifi.client.config.HazelcastConfig;
import in.codifi.client.controller.DefaultRestController;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AppUtil extends DefaultRestController {

	@Inject
	JsonWebToken accessToken;

	public String getAcToken() {
		return this.accessToken.getRawToken();
	}

	public static String getUserSession(String userId) {
		String userSession = "";
		String hzUserSessionKey = userId + AppConstants.HAZEL_KEY_REST_SESSION;
		userSession = HazelcastConfig.getInstance().getRestUserSession().get(hzUserSessionKey);
		return userSession;
	}

	/**
	 * 
	 * Method to get client info
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public ClinetInfoModel getClientInfo() {
		ClinetInfoModel model = clientInfo();
		return model;
	}

	/**
	 * 
	 * Method to validate the userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqUserID
	 * @return
	 */
	public boolean isValidUser(String userID) {

		try {
			String userIdFromToken = getUserId();
			if (StringUtil.isNotNullOrEmpty(userID) && StringUtil.isNotNullOrEmpty(userIdFromToken)) {
				if (userID.equalsIgnoreCase(userIdFromToken)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * Method to get user details from cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	public QuickAuthRespModel getUserInfo(String userId) {
		QuickAuthRespModel authModel = new QuickAuthRespModel();
		String hzKey = userId + AppConstants.HAZEL_KEY_USER_DETAILS;
		authModel = HazelcastConfig.getInstance().getUserSessionDetails().get(hzKey);
		return authModel;
	}

	/**
	 * Method to get product type by rest product type
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param restProductType
	 * @return
	 */
	public String getProductType(String restProductType) {
		String productType = "";
		try {
			List<ProductMasterModel> productMasterModels = new ArrayList<>();
			if (HazelcastConfig.getInstance().getProductTypes().containsKey(AppConstants.PRODUCT_TYPE)) {
				productMasterModels = HazelcastConfig.getInstance().getProductTypes().get(AppConstants.PRODUCT_TYPE);
				for (ProductMasterModel model : productMasterModels) {
					if (model.getValue().equalsIgnoreCase(restProductType.trim())) {
						productType = model.getKeyVariable();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return productType;
	}

	/**
	 * Method to get price type by rest price type
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param restPriceType
	 * @return
	 */
	public String getPriceType(String restPriceType) {
		String priceType = "";
		try {
			List<ProductMasterModel> productMasterModels = new ArrayList<>();
			if (HazelcastConfig.getInstance().getPriceTypes().containsKey(AppConstants.PRICE_TYPE)) {
				productMasterModels = HazelcastConfig.getInstance().getPriceTypes().get(AppConstants.PRICE_TYPE);
				for (ProductMasterModel model : productMasterModels) {
					if (model.getValue().equalsIgnoreCase(restPriceType.trim())) {
						priceType = model.getKeyVariable();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return priceType;
	}

}
