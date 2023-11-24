package in.codifi.client.ws.service;

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
import org.json.simple.JSONValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.client.config.RestServiceProperties;
import in.codifi.client.entity.logs.RestAccessLogModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.repository.AccessLogManager;
import in.codifi.client.transformation.ClientDetailsRespModel;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.CodifiUtil;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import in.codifi.client.ws.model.ClientDetailsRestFailModel;
import in.codifi.client.ws.model.ClientDetailsRestSuccessModel;
import in.codifi.client.ws.remodeling.ClientDetailsRemodeling;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ClientDetailsRestService {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	RestServiceProperties props;
	@Inject
	ClientDetailsRemodeling clientDetailsRemodeling;
	@Inject
	AccessLogManager accessLogManager;

	public RestResponse<GenericResponse> getClientDetails(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("client details request" + request);
		try {
			String baseUrl = props.getClientDetails();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getClientDetails");
			accessLogModel.setModule(AppConstants.MODULE_CLIENT);
			accessLogModel.setUrl(baseUrl);
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				Log.error("Unauthorized error in client details");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				Log.info("client details response" + output);
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				ClientDetailsRestSuccessModel clientDetailsSuccess = mapper.readValue(output,
						ClientDetailsRestSuccessModel.class);
				if (clientDetailsSuccess.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
					return prepareResponse.prepareFailedResponseForRestService(clientDetailsSuccess.getEmsg());
				} else {
					ClientDetailsRespModel response = clientDetailsRemodeling.bindClientDetails(clientDetailsSuccess,
							userId);
					return prepareResponse.prepareSuccessResponseObject(response);
				}

			} else {
				Log.info("Error Connection in client details. Response Code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					ClientDetailsRestFailModel clientDetailsRestFailModel = mapper.readValue(output,
							ClientDetailsRestFailModel.class);
					if (StringUtil.isNotNullOrEmpty(clientDetailsRestFailModel.getEmsg()))
						return prepareResponse.prepareFailedResponse(clientDetailsRestFailModel.getEmsg());
				}
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to invalidate WS session
	 * 
	 * @author dinesh
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> invalidateWsSession(String request) {
		Object object = new Object();
		try {
			Log.info("invalidateWsSession request - " + request);
			Log.info("invalidateWsSession URL - " + props.getWsInvalidateSession());
			System.out.println("invalidateWsSession request - " + request + "\n createWsSession URL - "
					+ props.getWsInvalidateSession());
			CodifiUtil.trustedManagement();
			URL url = new URL(props.getWsInvalidateSession());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "text/plain");
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			if (conn.getResponseCode() == 401) {
				Log.error("Unauthorized error in client details");
				return prepareResponse.prepareUnauthorizedResponse();
			}
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br2 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br2.readLine()) != null) {
				object = JSONValue.parse(output);
			}
			return prepareResponse.prepareSuccessResponseObject(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to create Web socket Session
	 * 
	 * @author dinesh
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> createWsSession(String request, String userId) {
		Object object = new Object();
		try {
			Log.info("createWsSession request - " + request);
			Log.info("createWsSession URL - " + props.getWsCreateSession());
			System.out.println(
					"createWsSession request - " + request + "\n createWsSession URL - " + props.getWsCreateSession());
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("createWsSession");
			accessLogModel.setModule(AppConstants.MODULE_CLIENT);
			accessLogModel.setUrl(props.getWsCreateSession());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			CodifiUtil.trustedManagement();
			URL url = new URL(props.getWsCreateSession());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "text/plain");
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			if (responseCode == 401) {
				Log.error("Unauthorized error in client details");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			}
			if (responseCode != 200) {
				insertRestAccessLogs(accessLogModel);
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br2 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br2.readLine()) != null) {
				object = JSONValue.parse(output);
			}
			return prepareResponse.prepareSuccessResponseObject(object);
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

}
