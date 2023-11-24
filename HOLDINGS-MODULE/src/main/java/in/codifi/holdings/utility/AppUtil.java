package in.codifi.holdings.utility;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ContractMasterModel;
import in.codifi.holdings.config.HazelcastConfig;
import in.codifi.holdings.controller.DefaultRestController;
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

	public String getExchSeg(String exch) {
		String exchSeg = "";
		try {
			exch = exch.toUpperCase();
			switch (exch) {
			case AppConstants.NSE:

				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exchSeg;

	}

}