package in.codifi.alerts.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.alerts.config.ApplicationProperties;
import in.codifi.alerts.entity.logs.RestAccessLogModel;
import in.codifi.alerts.model.response.ExchMsgRespModel;
import in.codifi.alerts.model.response.ExchStatusRespModel;
import in.codifi.alerts.model.response.GenericResponse;
import in.codifi.alerts.repository.AccessLogManager;
import in.codifi.alerts.utility.AppConstants;
import in.codifi.alerts.utility.CodifiUtil;
import in.codifi.alerts.utility.PrepareResponse;
import in.codifi.alerts.utility.StringUtil;
import in.codifi.alerts.ws.model.RestBrokerMsgRepModel;
import in.codifi.alerts.ws.model.RestExchMsgRespModel;
import in.codifi.alerts.ws.model.RestExchStatusRepModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class MessagesRestService {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ApplicationProperties props;
	@Inject
	AccessLogManager accessLogManager;

	/**
	 * method to get exchange message
	 * 
	 * @author SOWMIYA
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> getExchMsg(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala exch msg request" + request);
		RestExchMsgRespModel response = null;
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getExchMsg");
			accessLogModel.setModule(AppConstants.MODULE_ALERTS);
			accessLogModel.setUrl(props.getExchMsgUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getExchMsgUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (conn.getResponseCode() == 401) {
				Log.error("Unauthorized error in kambala exchange message");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (conn.getResponseCode() == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala exchange message response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<RestExchMsgRespModel> success = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, RestExchMsgRespModel.class));
						List<ExchMsgRespModel> respModel = bindExchResp(success);
						return prepareResponse.prepareSuccessResponseObject(respModel);

					} else if (output.startsWith("[]")) {
						return prepareResponse.prepareFailedResponseForRestService(AppConstants.REST_NO_DATA);
					} else {
						RestExchMsgRespModel fail = mapper.readValue(output, RestExchMsgRespModel.class);
						if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
							return prepareResponse.prepareFailedResponse(fail.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					response = mapper.readValue(output, RestExchMsgRespModel.class);
					if (StringUtil.isNotNullOrEmpty(response.getEmsg()))
						Log.error("Error Connection Response code -" + response.getEmsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to bindExchResponse
	 * 
	 * @author SOWMIYA
	 * @param success
	 * @return
	 */
	private List<ExchMsgRespModel> bindExchResp(List<RestExchMsgRespModel> success) {
		List<ExchMsgRespModel> exchResponse = new ArrayList<>();
		try {
			for (RestExchMsgRespModel response : success) {
				ExchMsgRespModel exchResp = new ExchMsgRespModel();
				exchResp.setExchange(response.getExch());
				exchResp.setExchangemsg(response.getExch_msg());
				exchResp.setExchangeTime(response.getExch_tm());
				exchResponse.add(exchResp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return exchResponse;
	}

	/**
	 * method to get brokerage message
	 * 
	 * @author SOWMIYA
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> getBrokerageMsg(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala brokerage msg request" + request);
		RestBrokerMsgRepModel response = null;
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getExchMsg");
			accessLogModel.setModule(AppConstants.MODULE_ALERTS);
			accessLogModel.setUrl(props.getBrokerageMsgUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getBrokerageMsgUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (conn.getResponseCode() == 401) {
				Log.error("Unauthorized error in kambala brokerage msg api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (conn.getResponseCode() == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala brokerage message response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					response = mapper.readValue(output, RestBrokerMsgRepModel.class);
					if (StringUtil.isNotNullOrEmpty(response.getStat())
							&& response.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return prepareResponse.prepareSuccessResponseObject(response);
					} else {
						return prepareResponse.prepareFailedResponse(response.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					response = mapper.readValue(output, RestBrokerMsgRepModel.class);
					if (StringUtil.isNotNullOrEmpty(response.getEmsg()))
						Log.error("Error Connection Response code -" + response.getEmsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get exchange status
	 * 
	 * @author SOWMIYA
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> getExchStatus(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala exchange status request" + request);
		RestExchStatusRepModel response = null;
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getExchMsg");
			accessLogModel.setModule(AppConstants.MODULE_ALERTS);
			accessLogModel.setUrl(props.getExchStatusUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getExchStatusUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (conn.getResponseCode() == 401) {
				Log.error("Unauthorized error in kambala exchange status api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (conn.getResponseCode() == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala exchange status response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<RestExchStatusRepModel> success = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, RestExchStatusRepModel.class));
						List<ExchStatusRespModel> respModel = bindExchStatus(success);
						return prepareResponse.prepareSuccessResponseObject(respModel);
					} else if (output.startsWith("[]")) {
						return prepareResponse.prepareFailedResponseForRestService(AppConstants.REST_NO_DATA);
					} else {
						response = mapper.readValue(output, RestExchStatusRepModel.class);
						if (StringUtil.isNotNullOrEmpty(response.getEmsg()))
							return prepareResponse.prepareFailedResponse(response.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					response = mapper.readValue(output, RestExchStatusRepModel.class);
					if (StringUtil.isNotNullOrEmpty(response.getEmsg()))
						Log.error("Error Connection Response code -" + response.getEmsg());
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to bind exchange status
	 * 
	 * @author SOWMIYA
	 * @param success
	 * @return
	 */
	private List<ExchStatusRespModel> bindExchStatus(List<RestExchStatusRepModel> success) {
		List<ExchStatusRespModel> responseModel = new ArrayList<>();
		try {
			for (RestExchStatusRepModel model : success) {
				ExchStatusRespModel response = new ExchStatusRespModel();
				response.setExchange(model.getExch());
				response.setStatus(model.getExchstat());
				response.setType(model.getExchtype());
				responseModel.add(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseModel;
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
					accessLogManager.insert24RestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

}