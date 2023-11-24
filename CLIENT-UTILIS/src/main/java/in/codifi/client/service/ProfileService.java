package in.codifi.client.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.service.spec.IProfileService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.AppUtil;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import in.codifi.client.ws.model.ClientDetailsRestReqModel;
import in.codifi.client.ws.service.ClientDetailsRestService;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ProfileService implements IProfileService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;
	@Inject
	ClientDetailsRestService restService;

	/**
	 * method to get client details
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getClientDetails(ClinetInfoModel info) {
		try {
			/** Verify session **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			String request = prepareClientDetails(info, userSession);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			return restService.getClientDetails(request, info.getUserId());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to prepare client details request body
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */

	private String prepareClientDetails(ClinetInfoModel info, String userSession) {
		ObjectMapper mapper = new ObjectMapper();
		String request = "";
		try {
			ClientDetailsRestReqModel reqModel = new ClientDetailsRestReqModel();
			reqModel.setUid(info.getUcc());
			QuickAuthRespModel authModel = appUtil.getUserInfo(info.getUserId());
			if (authModel != null && StringUtil.isNotNullOrEmpty(authModel.getActId())
					&& StringUtil.isNotNullOrEmpty(authModel.getBrkName())) {
				reqModel.setActid(authModel.getActId());
				reqModel.setBrkname(authModel.getBrkName());
				String json = mapper.writeValueAsString(reqModel);
				request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
						+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;

	}

	/**
	 * Method to invalidate web socket session
	 * 
	 * @author dinesh
	 * @param model
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> invalidateWsSession(ClientDetailsReqModel reqModel, ClinetInfoModel info) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(reqModel.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Verify session **/
			String accessToken = appUtil.getAcToken();
			if (StringUtil.isNullOrEmpty(accessToken))
				return prepareResponse.prepareUnauthorizedResponse();

			StringBuilder request = new StringBuilder();
			request.append("uid=" + info.getUserId() + "_" + reqModel.getSource());
			request.append("&usession=" + accessToken);
			request.append("&src=" + reqModel.getSource());
			request.append("&vcode=" + "STONE_AGE");
			return restService.invalidateWsSession(request.toString());

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to create web socket session
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createWsSession(ClientDetailsReqModel reqModel, ClinetInfoModel info) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(reqModel.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Get session **/
			String accessToken = appUtil.getAcToken();
			if (StringUtil.isNullOrEmpty(accessToken))
				return prepareResponse.prepareUnauthorizedResponse();

			StringBuilder request = new StringBuilder();
			request.append("uid=" + info.getUserId() + "_" + reqModel.getSource());
			request.append("&usession=" + accessToken);
			request.append("&src=" + reqModel.getSource());
			request.append("&vcode=" + "STONE_AGE");
			return restService.createWsSession(request.toString(), info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
