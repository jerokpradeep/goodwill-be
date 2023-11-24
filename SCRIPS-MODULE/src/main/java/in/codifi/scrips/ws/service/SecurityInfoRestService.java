package in.codifi.scrips.ws.service;

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

import in.codifi.scrips.config.RestProperties;
import in.codifi.scrips.entity.logs.RestAccessLogModel;
import in.codifi.scrips.model.response.GenericResponse;
import in.codifi.scrips.model.transformation.SecurityInfoRespModel;
import in.codifi.scrips.repository.AccessLogManager;
import in.codifi.scrips.utility.AppConstants;
import in.codifi.scrips.utility.CodifiUtil;
import in.codifi.scrips.utility.PrepareResponse;
import in.codifi.scrips.utility.StringUtil;
import in.codifi.scrips.ws.model.SecurityInfoRestFailRespModel;
import in.codifi.scrips.ws.model.SecurityInfoRestSuccRespModel;
import in.codifi.scrips.ws.remodeling.SecurityInfoRemodeling;
import io.quarkus.logging.Log;

@ApplicationScoped
public class SecurityInfoRestService {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	RestProperties props;
	@Inject
	SecurityInfoRemodeling securityRemodeling;
	@Inject
	AccessLogManager accessLogManager;

	/*
	 * method to connect get security information to kambala server
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */

	public RestResponse<GenericResponse> getSecurityInfo(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("security info request" + request);
		try {
			CodifiUtil.trustedManagement();
			String baseUrl = props.getGetSecurityInfo();

			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getSecurityInfo");
			accessLogModel.setModule(AppConstants.MODULE_SCRIPS);
			accessLogModel.setUrl(baseUrl);
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

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
				Log.error("Unauthorized error in security info");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				Log.info("security info response" + output);
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				SecurityInfoRestSuccRespModel respModel = mapper.readValue(output, SecurityInfoRestSuccRespModel.class);
				if (respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
					return prepareResponse.prepareFailedResponseForRestService(respModel.getEmsg());
				} else {
					SecurityInfoRespModel response = securityRemodeling.bindSecurityInfoData(respModel);
					return prepareResponse.prepareSuccessResponseObject(response);
				}
			} else {
				Log.info("Error Connection in get security info. Response Code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					SecurityInfoRestFailRespModel failModel = mapper.readValue(output,
							SecurityInfoRestFailRespModel.class);
					if (StringUtil.isNotNullOrEmpty(failModel.getEmsg()))
						return prepareResponse.prepareFailedResponse(failModel.getEmsg());
				}
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
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
