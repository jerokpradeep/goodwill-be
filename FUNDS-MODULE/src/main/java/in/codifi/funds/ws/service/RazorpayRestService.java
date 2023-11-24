package in.codifi.funds.ws.service;

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

import in.codifi.funds.config.HazelcastConfig;
import in.codifi.funds.config.PaymentsProperties;
import in.codifi.funds.entity.logs.RestAccessLogModel;
import in.codifi.funds.model.response.GenericResponse;
import in.codifi.funds.repository.AccessLogManager;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.CodifiUtil;
import in.codifi.funds.utility.PrepareResponse;
import in.codifi.funds.utility.StringUtil;
import in.codifi.funds.ws.model.BankDetailsRestResp;
import in.codifi.funds.ws.model.GetHSTokenRestRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class RazorpayRestService {

	@Inject
	PaymentsProperties props;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AccessLogManager accessLogManager;

	/**
	 * Method to get bank code from IFSC Details
	 * 
	 * @author Dinesh Kumar
	 * @param ifsc
	 * @return
	 */
	public BankDetailsRestResp getBankDetails(String ifsc) {
		BankDetailsRestResp response = new BankDetailsRestResp();
		try {
			URL url = new URL(props.getRazorpayIfscUrl() + ifsc);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = bufferedReader.readLine();
			if (StringUtil.isNotNullOrEmpty(output)) {
				if (output.startsWith("Not Found")) {
					Log.error("Failed to get Bank code - Invalid IFSC code - " + ifsc);
				} else {
					ObjectMapper mapper = new ObjectMapper();
					response = mapper.readValue(output, BankDetailsRestResp.class);
					if (response != null && StringUtil.isNotNullOrEmpty(response.getBankcode())) {
						HazelcastConfig.getInstance().getIfscCodeMapping().put(ifsc, response.getBankcode());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	/**
	 * method to get hs token for payout
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
			accessLogModel.setModule(AppConstants.MODULE_FUNDS);
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
