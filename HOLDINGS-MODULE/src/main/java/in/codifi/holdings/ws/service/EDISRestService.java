package in.codifi.holdings.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.holdings.config.RestServiceProperties;
import in.codifi.holdings.entity.logs.RestAccessLogModel;
import in.codifi.holdings.model.response.GenericResponse;
import in.codifi.holdings.model.transformation.EdisRespModel;
import in.codifi.holdings.repository.AccessLogManager;
import in.codifi.holdings.utility.AppConstants;
import in.codifi.holdings.utility.CodifiUtil;
import in.codifi.holdings.utility.PrepareResponse;
import in.codifi.holdings.utility.StringUtil;
import in.codifi.holdings.ws.model.EDISRestRespModel;
import in.codifi.holdings.ws.model.GetHSTokenRestRespModel;
import in.codifi.holdings.ws.remodeling.EdisRemodeling;
import io.quarkus.logging.Log;

@ApplicationScoped
public class EDISRestService {
	@Inject
	RestServiceProperties props;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AccessLogManager accessLogManager;
	@Inject
	EdisRemodeling edisRemodeling;

	/**
	 * method to get initialize edis request
	 * 
	 * @author SOWMIYA
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> initializeEdisRequest(String request, String userId) {
		EDISRestRespModel response = new EDISRestRespModel();
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala EDIS request" + request);
		try {

			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("initializeEdisRequest");
			accessLogModel.setModule(AppConstants.MODULE_HOLDINGS);
			accessLogModel.setUrl(props.getEdisInitialize());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			String edisRequestUrl = props.getEdisInitialize();
			URL url = new URL(edisRequestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			int responseCode = conn.getResponseCode();
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				Log.error("Unauthorized error in kambala initialize edis api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala Initialize edis response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					response = mapper.readValue(output, EDISRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(response.getStat())) {
						if (response.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
							EdisRespModel edisRespModel = edisRemodeling.bindEdisResponse(response);
							return prepareResponse.prepareSuccessResponseObject(edisRespModel);
						} else {
							return prepareResponse.prepareFailedResponse(response.getEmsg());
						}
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					response = mapper.readValue(output, EDISRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(response.getEmsg()))
						System.out.println(
								"Error Connection in Initialize edis api. Rsponse code -" + response.getEmsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to insert rest service access logs
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param accessLogModel
	 */
	public void insertRestAccessLogs(RestAccessLogModel accessLogModel) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					accessLogManager.insertRestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * method to get hs token
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> getHSToken(String request, String userId) {
		GetHSTokenRestRespModel respModel = new GetHSTokenRestRespModel();
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala getHSToken request" + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getHSToken");
			accessLogModel.setModule(AppConstants.MODULE_HOLDINGS);
			accessLogModel.setUrl(props.getHsTokenUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			String edisRequestUrl = props.getHsTokenUrl();
			URL url = new URL(edisRequestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			int responseCode = conn.getResponseCode();
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				Log.error("Unauthorized error in kambala get hs token api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala hsToken response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					respModel = mapper.readValue(output, GetHSTokenRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(respModel.getStat())) {
						if (respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
							return prepareResponse.prepareSuccessResponseObject(respModel);
						} else {
							return prepareResponse.prepareFailedResponse(respModel.getEmsg());
						}
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					respModel = mapper.readValue(output, GetHSTokenRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(respModel.getEmsg()))
						System.out.println("Error Connection in hs token api. Response code -" + respModel.getEmsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
