package in.codifi.holdings.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.holdings.config.RestServiceProperties;
import in.codifi.holdings.entity.logs.RestAccessLogModel;
import in.codifi.holdings.model.response.GenericResponse;
import in.codifi.holdings.model.transformation.HoldingsRespModel;
import in.codifi.holdings.model.transformation.NonPoaHoldingsRespModel;
import in.codifi.holdings.repository.AccessLogManager;
import in.codifi.holdings.utility.AppConstants;
import in.codifi.holdings.utility.CodifiUtil;
import in.codifi.holdings.utility.PrepareResponse;
import in.codifi.holdings.utility.StringUtil;
import in.codifi.holdings.ws.model.Fail;
import in.codifi.holdings.ws.model.NonPoaHoldingsFail;
import in.codifi.holdings.ws.model.NonPoaHoldingsSuccess;
import in.codifi.holdings.ws.model.Success;
import in.codifi.holdings.ws.remodeling.HoldingsRemodeling;
import io.quarkus.logging.Log;

@ApplicationScoped
public class HoldingsRestService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	RestServiceProperties props;

	@Inject
	HoldingsRemodeling holdingsRemodeling;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * Method to get holdings
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param request
	 * @param product
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> getHoldings(String request, String product, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala holdings request" + request);
		try {

			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getHoldings");
			accessLogModel.setModule(AppConstants.MODULE_HOLDINGS);
			accessLogModel.setUrl(props.getHoldingsUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getHoldingsUrl());
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
				Log.error("Unauthorized error in kambala holdings api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala Holdings response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<Success> success = mapper.readValue(output,
								mapper.getTypeFactory().constructCollectionType(List.class, Success.class));
						HoldingsRespModel extract = holdingsRemodeling.bindHoldingData(success, product);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else if (output.startsWith("[]")) {
						return prepareResponse.prepareFailedResponseForRestService(AppConstants.REST_NO_DATA);
					} else {
						Fail fail = mapper.readValue(output, Fail.class);
						if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
							return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Fail fail = mapper.readValue(output, Fail.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println("Error Connection in holdings api. Response code -" + fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get non POA holdings
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param request
	 * @param product
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> getNonPoaHoldings(String request, String product, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("Kambala Non Poa holdings request" + request);
		try {

			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getNonPoaHoldings");
			accessLogModel.setModule(AppConstants.MODULE_HOLDINGS);
			accessLogModel.setUrl(props.getNonPoaHoldingsUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getNonPoaHoldingsUrl());
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
				Log.error("Unauthorized error in kambala Non Poa holdings api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala Non Poa Holdings response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<NonPoaHoldingsSuccess> success = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, NonPoaHoldingsSuccess.class));
						NonPoaHoldingsRespModel extract = holdingsRemodeling.bindNonPoaHoldingData(success, product);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else if (output.startsWith("[]")) {
						return prepareResponse.prepareFailedResponseForRestService(AppConstants.REST_NO_DATA);
					} else {
						NonPoaHoldingsFail fail = mapper.readValue(output, NonPoaHoldingsFail.class);
						if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
							return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					NonPoaHoldingsFail fail = mapper.readValue(output, NonPoaHoldingsFail.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println("Error Connection in Non Poa holdings api. Rsponse code -" + fail.getEmsg());
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
					accessLogManager.insert24RestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}
}
