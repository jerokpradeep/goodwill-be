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

import in.codifi.funds.config.RestServiceProperties;
import in.codifi.funds.entity.logs.RestAccessLogModel;
import in.codifi.funds.model.response.GenericResponse;
import in.codifi.funds.model.transformation.LimitsResponseModel;
import in.codifi.funds.repository.AccessLogManager;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.CodifiUtil;
import in.codifi.funds.utility.PrepareResponse;
import in.codifi.funds.utility.StringUtil;
import in.codifi.funds.ws.model.RestLimitsResp;
import in.codifi.funds.ws.remodeling.LimitsRemodeling;
import io.quarkus.logging.Log;

@ApplicationScoped
public class FundsRestService {

	@Inject
	LimitsRemodeling limitsRemodeling;

	@Inject
	RestServiceProperties props;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * Method to connect the API
	 * 
	 * @author SOWMIYA
	 * @param baseUrl
	 * @return
	 */
	public RestResponse<GenericResponse> getLimits(String request, String userId) {
		Log.info("Kambala Limits Request" + request);
		ObjectMapper mapper = new ObjectMapper();
		RestLimitsResp response = null;
		String baseUrl = props.getLimitsUrl();
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getLimits");
			accessLogModel.setModule(AppConstants.MODULE_FUNDS);
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
			System.out.println(baseUrl + request);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				Log.error("Unauthorized error in kambala Limits api");
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala Limits response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {

					response = mapper.readValue(output, RestLimitsResp.class);
					if (StringUtil.isNotNullOrEmpty(response.getStat())
							&& response.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						LimitsResponseModel limitsResponseModel = limitsRemodeling.bindLimitsResponse(response);
						return prepareResponse.prepareSuccessResponseObject(limitsResponseModel);
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
					response = mapper.readValue(output, RestLimitsResp.class);
					if (StringUtil.isNotNullOrEmpty(response.getEmsg()))
						System.out.println("Error Connection in Limits api. Rsponse code -" + response.getEmsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
