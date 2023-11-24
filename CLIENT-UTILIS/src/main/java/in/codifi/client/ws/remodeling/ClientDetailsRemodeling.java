package in.codifi.client.ws.remodeling;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.cache.model.Bankdetails;
import in.codifi.cache.model.ClientDetailsModel;
import in.codifi.client.config.HazelcastConfig;
import in.codifi.client.transformation.ClientDetailsRespModel;
import in.codifi.client.transformation.DpAccountNum;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.AppUtil;
import in.codifi.client.utilis.StringUtil;
import in.codifi.client.ws.model.Bankdetail;
import in.codifi.client.ws.model.ClientDetailsRestSuccessModel;
import in.codifi.client.ws.model.DpAcctNum;
import in.codifi.ws.model.kb.login.ProductArray;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ClientDetailsRemodeling {

	@Inject
	AppUtil appUtil;

	public ClientDetailsRespModel bindClientDetails(ClientDetailsRestSuccessModel clientDetailsSuccess, String userId) {

		ClientDetailsRespModel responseModel = new ClientDetailsRespModel();
		try {

			List<Bankdetails> bankDetails = new ArrayList<>();
			List<DpAccountNum> dpAccountNum = new ArrayList<>();
			if (StringUtil.isNotNullOrEmpty(clientDetailsSuccess.getPan())) {
				String maskedPanNo = clientDetailsSuccess.getPan();
				maskedPanNo = maskedPanNo.replaceAll(".(?=.{4})", "*");
				responseModel.setPan(maskedPanNo);
			}
			if (StringUtil.isNotNullOrEmpty(clientDetailsSuccess.getEmail())) {
				String maskedEmail = clientDetailsSuccess.getEmail();
				maskedEmail = maskedEmail.replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
//				responseModel.setEmail(maskedEmail);
				responseModel.setEmail(clientDetailsSuccess.getEmail());
			}
			if (StringUtil.isNotNullOrEmpty(clientDetailsSuccess.getMNum())) {
				String maskedPhoneNo = clientDetailsSuccess.getMNum();
				maskedPhoneNo = maskedPhoneNo.replaceAll(".(?=.{4})", "*");
//				responseModel.setMobNo(maskedPhoneNo);
				responseModel.setMobNo(clientDetailsSuccess.getMNum());
			}
			responseModel.setActId(clientDetailsSuccess.getActid());
			responseModel.setClientName(clientDetailsSuccess.getCliname());
			responseModel.setActStatus(clientDetailsSuccess.getActSts());
			responseModel.setCreatedDate(clientDetailsSuccess.getCreatdte());
			responseModel.setCreatedTime(clientDetailsSuccess.getCreattme());
			responseModel.setAddress(clientDetailsSuccess.getAddr());
			responseModel.setOfficeAddress(clientDetailsSuccess.getAddroffice());
			responseModel.setCity(clientDetailsSuccess.getAddrcity());
			responseModel.setState(clientDetailsSuccess.getAddrstate());
			responseModel.setMandateIdList(clientDetailsSuccess.getMandateIdList());
			responseModel.setExchange(clientDetailsSuccess.getExarr());
			for (Bankdetail bank : clientDetailsSuccess.getBankdetails()) {
				Bankdetails details = new Bankdetails();
				if (StringUtil.isNotNullOrEmpty(bank.getAcctnum())) {
					String maskedAcctno = bank.getAcctnum();
					maskedAcctno = maskedAcctno.replaceAll(".(?=.{4})", "*");
					details.setAccNumber(maskedAcctno);
				}
				details.setBankName(bank.getBankn());
				bankDetails.add(details);
				responseModel.setBankdetails(bankDetails);
			}
			for (DpAcctNum dp : clientDetailsSuccess.getDpAcctNum()) {
				DpAccountNum details = new DpAccountNum();
				details.setDpAccountNumber(dp.getDpnum());
				dpAccountNum.add(details);
				responseModel.setDpAccountNumber(dpAccountNum);
			}

			QuickAuthRespModel authModel = appUtil.getUserInfo(userId);
			if (authModel != null) {
				responseModel.setUserId(authModel.getUId());
				responseModel.setBranchId(authModel.getBrnchId());
				responseModel.setBrokerName(authModel.getBrkName());
				List<String> productList = new ArrayList<>();
				List<ProductArray> products = authModel.getProductArray();
				List<String> productTypes = new ArrayList<>();
				List<String> priceTypes = new ArrayList<>();
				for (ProductArray productArray : products) {
					productList.add(productArray.getPrd());
					String product = "";
					if (StringUtil.isNotNullOrEmpty(productArray.getPrd())
							&& productArray.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)) {
						productTypes.add(AppConstants.BRACKET);
					} else if (StringUtil.isNotNullOrEmpty(productArray.getPrd())
							&& productArray.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
						productTypes.add(AppConstants.COVER);
					} else {
						product = appUtil.getProductType(productArray.getPrd());
						if (StringUtil.isNotNullOrEmpty(product))
							productTypes.add(product);
					}
				}
				for (String orderArray : authModel.getOrderArray()) {
					String price = "";
					price = appUtil.getPriceType(orderArray);
					if (StringUtil.isNotNullOrEmpty(price))
						priceTypes.add(price);
				}
				responseModel.setProductTypes(productTypes);
				responseModel.setPriceTypes(priceTypes);
				responseModel.setProducts(productList);
				responseModel.setOrders(authModel.getOrderArray());
			}

			loadClientDeatilsIntoCache(responseModel);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return responseModel;

	}

	/**
	 * 
	 * Method to load client details into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param respModel
	 */
	private void loadClientDeatilsIntoCache(ClientDetailsRespModel respModel) {
		try {
			ClientDetailsModel responseModel = new ClientDetailsModel();
			responseModel.setPan(respModel.getPan());
			responseModel.setEmail(respModel.getEmail());
			responseModel.setMobNo(respModel.getMobNo());
			responseModel.setActId(respModel.getActId());
			responseModel.setClientName(respModel.getClientName());
			responseModel.setActStatus(respModel.getActStatus());
			responseModel.setCreatedDate(respModel.getCreatedDate());
			responseModel.setCreatedTime(respModel.getCreatedTime());
			responseModel.setAddress(respModel.getAddress());
			responseModel.setOfficeAddress(respModel.getOfficeAddress());
			responseModel.setCity(respModel.getCity());
			responseModel.setState(respModel.getState());
			responseModel.setExchange(respModel.getExchange());
			responseModel.setUserId(respModel.getUserId());
			responseModel.setBranchId(respModel.getBranchId());
			responseModel.setBrokerName(respModel.getBrokerName());
			responseModel.setBankdetails(respModel.getBankdetails());
			HazelcastConfig.getInstance().getClientDetails().remove(respModel.getUserId());
			HazelcastConfig.getInstance().getClientDetails().put(respModel.getUserId(), responseModel);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
	}

}
