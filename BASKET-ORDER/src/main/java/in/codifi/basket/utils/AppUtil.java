package in.codifi.basket.utils;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.basket.config.HazelcastConfig;
import in.codifi.basket.controller.DefaultRestController;
import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ContractMasterModel;
import in.codifi.cache.model.ProductMasterModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AppUtil extends DefaultRestController {

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
	 * Method to get access token
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	public String getAccessToken() {
		String token = "";
		try {
			token = getAcToken();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return token;
	}

	/**
	 * Method to get contract info
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param exch
	 * @param token
	 * @return
	 */
	public ContractMasterModel getContractInfo(String exch, String token) {
		ContractMasterModel contractMasterModel = new ContractMasterModel();
		try {
			if (HazelcastConfig.getInstance().getContractMaster().get(exch + "_" + token) != null) {
				contractMasterModel = HazelcastConfig.getInstance().getContractMaster().get(exch + "_" + token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return contractMasterModel;
	}


	/**
	 * Method to get rest price type
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param priceType
	 * @return
	 */
	public String getRestPriceType(String priceType) {
		String restPriceType = "";
		try {
			List<ProductMasterModel> productMasterModels = new ArrayList<>();
			if (HazelcastConfig.getInstance().getPriceTypes().containsKey(AppConstants.PRICE_TYPE)) {
				productMasterModels = HazelcastConfig.getInstance().getPriceTypes().get(AppConstants.PRICE_TYPE);
				for (ProductMasterModel model : productMasterModels) {
					if (model.getKeyVariable().equalsIgnoreCase(priceType.trim())) {
						restPriceType = model.getValue();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return restPriceType;
	}
	
	/**
	 * Method to get rest product type
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param productType
	 * @return
	 */
	public String getRestProductType(String productType) {
		String restProductType = "";
		try {
			List<ProductMasterModel> productMasterModels = new ArrayList<>();
			if (HazelcastConfig.getInstance().getProductTypes().containsKey(AppConstants.PRODUCT_TYPE)) {
				productMasterModels = HazelcastConfig.getInstance().getProductTypes().get(AppConstants.PRODUCT_TYPE);
				for (ProductMasterModel model : productMasterModels) {
					if (model.getKeyVariable().equalsIgnoreCase(productType.trim())) {
						restProductType = model.getValue();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return restProductType;
	}
}