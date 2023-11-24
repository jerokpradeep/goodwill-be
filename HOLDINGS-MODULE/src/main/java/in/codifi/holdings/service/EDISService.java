package in.codifi.holdings.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.holdings.model.request.EdisReqModel;
import in.codifi.holdings.model.response.GenericResponse;
import in.codifi.holdings.repository.HoldingRepository;
import in.codifi.holdings.service.spec.EDISServiceSpec;
import in.codifi.holdings.utility.AppConstants;
import in.codifi.holdings.utility.AppUtil;
import in.codifi.holdings.utility.PrepareResponse;
import in.codifi.holdings.utility.StringUtil;
import in.codifi.holdings.ws.model.EdisRestReqModel;
import in.codifi.holdings.ws.model.EdisRestReqModel.PartsRestReqModel;
import in.codifi.holdings.ws.model.GetHSTokenRestReqModel;
import in.codifi.holdings.ws.service.EDISRestService;

@ApplicationScoped
public class EDISService implements EDISServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	EDISRestService restService;
	@Inject
	HoldingRepository holdingsrepo;

	/**
	 * method to initializeEdis request
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> initializeEdisRequest(List<EdisReqModel> model, ClinetInfoModel info) {
		try {
			if (!validateEdisReqParams(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Get user session from cache **/

			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			String request = prepareEdisRequest(model, userSession, info);
			if (request != null)
				return restService.initializeEdisRequest(request, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare edis request
	 * 
	 * @author SOWMIYA
	 * @param model
	 * @param userSession
	 * @return
	 */
	private String prepareEdisRequest(List<EdisReqModel> model, String userSession, ClinetInfoModel info) {
		String response = "";
		try {
			EdisRestReqModel edisRestReqModel = new EdisRestReqModel();
			edisRestReqModel.setUid(info.getUcc());
			edisRestReqModel.setActid(info.getUcc());
			List<PartsRestReqModel> partsRestReqModels = new ArrayList<>();
			for (EdisReqModel edisReqModel : model) {
				EdisRestReqModel reqModel = new EdisRestReqModel();
				EdisRestReqModel.PartsRestReqModel partsRestReqModel = reqModel.new PartsRestReqModel();
				edisRestReqModel.setSettle_t(edisReqModel.getSettlementType());
				partsRestReqModel.setIsin(edisReqModel.getIsin());
				partsRestReqModel.setQty(edisReqModel.getQty());
				partsRestReqModels.add(partsRestReqModel);
			}
			edisRestReqModel.setParts(partsRestReqModels);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(edisRestReqModel);
			response = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * method to validate edis request
	 * 
	 * @author SOWMIYA
	 * @param model Modified by Dinesh
	 * @return
	 */
	private boolean validateEdisReqParams(List<EdisReqModel> model) {
		for (EdisReqModel edisReqModel : model) {
			if (StringUtil.isNullOrEmpty(edisReqModel.getSettlementType())
					|| StringUtil.isNullOrEmpty(edisReqModel.getIsin())
					|| StringUtil.isNullOrEmpty(edisReqModel.getQty())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * method to get hs token
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> getHSToken(ClinetInfoModel info) {
		try {

			/** Get user session from cache **/

			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			String request = prepareHSTokenRequest(info, userSession);
			if (StringUtil.isNotNullOrEmpty(request)) {
				return restService.getHSToken(request, info.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getHSToken", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare hs Token request
	 * 
	 * @author SowmiyaThangaraj
	 * @param info
	 * @param userSession
	 * @return
	 */
	private String prepareHSTokenRequest(ClinetInfoModel info, String userSession) {
		String request = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			GetHSTokenRestReqModel reqModel = new GetHSTokenRestReqModel();
			reqModel.setUid(info.getUserId());
			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

}

//	/**
//	 * 
//	 * Method to get uploaded holdings
//	 * 
//	 * @author Dinesh Kumar
//	 *
//	 * @param userId
//	 * @return
//	 */
//	private List<HoldingsData> getUploadedHoldingsData(String userId) {
//		List<HoldingsData> holdingsList = new ArrayList<>();
//		if (HazelcastConfig.getInstance().getUploadedHoldings().get(userId) != null) {
//			holdingsList = HazelcastConfig.getInstance().getUploadedHoldings().get(userId);
//		} else {
//			if (HazelcastConfig.getInstance().getUploadedHoldings().containsKey(userId)) {
//				return null;
//			} else {
//				List<HoldingsEntity> dbData = holdingsrepo.findAllByUserId(userId);
//				for (HoldingsEntity holdingsEntity : dbData) {
//					HoldingsData holdingsData = new HoldingsData();
//					holdingsData.setActualPrice(holdingsEntity.getActualPrice());
//					holdingsData.setIsin(holdingsEntity.getIsin());
//					holdingsData.setAuthFlag(holdingsEntity.getAuthFlag());
//					holdingsData.setPoaStatus(holdingsEntity.getPoaStatus());
//					holdingsData.setUserId(holdingsEntity.getUserId());
//					holdingsData.setClosePrice(holdingsEntity.getClosePrice());
//					holdingsData.setAuthQty(holdingsEntity.getAuthQty());
//					holdingsData.setProduct(holdingsEntity.getProduct());
//					holdingsList.add(holdingsData);
//				}
//				if (holdingsList != null && holdingsList.size() > 0) {
//					HazelcastConfig.getInstance().getUploadedHoldings().remove(userId);
//					HazelcastConfig.getInstance().getUploadedHoldings().put(userId, holdingsList);
//				} else {
//					List<HoldingsData> holdings = new ArrayList<>();
//					HazelcastConfig.getInstance().getUploadedHoldings().remove(userId);
//					HazelcastConfig.getInstance().getUploadedHoldings().put(userId, holdings);
//				}
//			}
//		}
//		return holdingsList;
//	}
//
//	@Override
//	public void revocationReturn(String req, String userId) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
