package in.codifi.position.utility;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ContractMasterModel;
import in.codifi.cache.model.PnlLotModel;
import in.codifi.cache.model.ProductMasterModel;
import in.codifi.position.config.HazelcastConfig;
import in.codifi.position.controller.DefaultRestController;
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
	 * To get PDC from cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param token
	 * @param exch
	 * @return
	 */
	public static String getPdc(String exch, String token) {
		String pdc = "0";

		if (HazelcastConfig.getInstance().getContractMaster().containsKey(exch + "_" + token)) {
			ContractMasterModel contractMasterModel = HazelcastConfig.getInstance().getContractMaster()
					.get(exch + "_" + token);
			if (contractMasterModel != null && StringUtil.isNotNullOrEmpty(contractMasterModel.getPdc())) {
				pdc = contractMasterModel.getPdc();
			}
		} else {
			Log.error("Not able to get PDC from cache");
		}
		return pdc;
	}

	/**
	 * 
	 * Method to get contract master
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param exch
	 * @param token
	 * @return
	 */
	public static ContractMasterModel getContractMaster(String exch, String token) {
		ContractMasterModel contractMasterModel = new ContractMasterModel();
		contractMasterModel = HazelcastConfig.getInstance().getContractMaster().get(exch + "_" + token);
		return contractMasterModel;
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
	 * Method to get order type by rest order type
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param restOrderType
	 * @return
	 */
	public String getOrderType(String restOrderType) {
		String orderType = "";
		try {
			List<ProductMasterModel> productMasterModels = new ArrayList<>();
			if (HazelcastConfig.getInstance().getOrderTypes().containsKey(AppConstants.ORDER_TYPE)) {
				productMasterModels = HazelcastConfig.getInstance().getOrderTypes().get(AppConstants.ORDER_TYPE);
				for (ProductMasterModel model : productMasterModels) {
					if (model.getValue().equalsIgnoreCase(restOrderType.trim())) {
						orderType = model.getKeyVariable();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return orderType;
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

	/**
	 * Method to get rest order type
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param orderType
	 * @return
	 */
	public String getRestOrderType(String orderType) {
		String restOrderType = "";
		try {
			List<ProductMasterModel> productMasterModels = new ArrayList<>();
			if (HazelcastConfig.getInstance().getOrderTypes().containsKey(AppConstants.ORDER_TYPE)) {
				productMasterModels = HazelcastConfig.getInstance().getOrderTypes().get(AppConstants.ORDER_TYPE);
				for (ProductMasterModel model : productMasterModels) {
					if (model.getKeyVariable().equalsIgnoreCase(orderType.trim())) {
						restOrderType = model.getValue();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return restOrderType;
	}
	/**
	 * Method to get pnl lot
	 * @author Dinesh Kumar
	 * @param exch
	 * @param symbol
	 * @return
	 */
	public static String getPnlLotSize(String exch, String symbol) {
		String pnlLotSize = "";
		List<PnlLotModel> pnlLotModelList = new ArrayList<>();
		if (HazelcastConfig.getInstance().getPnlLot().get(AppConstants.PNL_LOT) != null) {
			pnlLotModelList = HazelcastConfig.getInstance().getPnlLot().get(AppConstants.PNL_LOT);
			for (PnlLotModel pnlLotModel : pnlLotModelList) {
				if (pnlLotModel.getExch().equalsIgnoreCase(exch) && pnlLotModel.getSymbol().equalsIgnoreCase(symbol)) {
					pnlLotSize = pnlLotModel.getLotSize();
					break;
				}
			}
		}else {
			Log.error("PNL_LOT is not in cache");
			//TODO need to load if pnl lot is not in cache
		}
		return pnlLotSize;
	}

}