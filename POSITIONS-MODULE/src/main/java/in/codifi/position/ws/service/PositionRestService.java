package in.codifi.position.ws.service;

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

import in.codifi.position.config.RestServiceProperties;
import in.codifi.position.entity.logs.RestAccessLogModel;
import in.codifi.position.model.response.CommonErrorResponse;
import in.codifi.position.model.response.GenericResponse;
import in.codifi.position.model.transformation.PositionsRespModel;
import in.codifi.position.repository.AccessLogManager;
import in.codifi.position.utility.AppConstants;
import in.codifi.position.utility.CodifiUtil;
import in.codifi.position.utility.PrepareResponse;
import in.codifi.position.utility.StringUtil;
import in.codifi.position.ws.model.RestConversionResp;
import in.codifi.position.ws.model.RestPositionFailResp;
import in.codifi.position.ws.model.RestPositionSuccessResp;
import in.codifi.position.ws.remodeling.PositionsRemodeling;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PositionRestService {

	@Inject
	RestServiceProperties props;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	PositionsRemodeling positionsRemodeling;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * Get position form kamabala api
	 * 
	 * @author Nesan
	 * 
	 */
	public RestResponse<GenericResponse> getPositionKambala(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		Log.info("Kambala position request" + request);
		try {
			accessLogModel.setMethod("getPosition");
			accessLogModel.setModule(AppConstants.MODULE_POSITIONS);
			accessLogModel.setUrl(props.getPositionUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getPositionUrl());
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
				Log.error("Unauthorized error in kambala position api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<RestPositionSuccessResp> successRepList = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, RestPositionSuccessResp.class));
//						List<PositionExtraction> extract = positionsRemodeling.bindPostitionData(successRepList);
						List<PositionsRespModel> extract = positionsRemodeling.preparePostitionResp(successRepList,
								userId);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else if (output.startsWith("[]")) {
						return prepareResponse.prepareFailedResponseForRestService(AppConstants.REST_NO_DATA);
					} else {
						RestPositionFailResp failResp = mapper.readValue(output, RestPositionFailResp.class);
						if (StringUtil.isNotNullOrEmpty(failResp.getEmsg()))
							return prepareResponse.prepareFailedResponseForRestService(failResp.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					RestPositionFailResp fail = mapper.readValue(output, RestPositionFailResp.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println("Error Connection in position api. Rsponse code -" + fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get position from Kambala for API
	 * 
	 * @author Gowrisankar
	 * @param request
	 * @param userId
	 * @return
	 */
	public Object getPositionKambalaApi(String request, String userId) {
		CommonErrorResponse errorResponse = new CommonErrorResponse();
		ObjectMapper mapper = new ObjectMapper();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		Log.info("Kambala position request" + request);
		try {
			accessLogModel.setMethod("getPosition");
			accessLogModel.setModule(AppConstants.MODULE_POSITIONS);
			accessLogModel.setUrl(props.getPositionUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getPositionUrl());
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
				Log.error("Unauthorized error in kambala position api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.SESSION_EXP_API);
				return errorResponse;

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<RestPositionSuccessResp> successRepList = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, RestPositionSuccessResp.class));
						return successRepList;
					} else if (output.startsWith("[]")) {
						errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
						errorResponse.setEmsg(AppConstants.REST_NO_DATA);
						return errorResponse;
					} else {
						RestPositionFailResp failResp = mapper.readValue(output, RestPositionFailResp.class);
						if (StringUtil.isNotNullOrEmpty(failResp.getEmsg())) {
							errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
							errorResponse.setEmsg(failResp.getEmsg());
							return errorResponse;
						}
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					RestPositionFailResp fail = mapper.readValue(output, RestPositionFailResp.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println("Error Connection in position api. Rsponse code -" + fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return errorResponse;
	}

	/**
	 * position conversion form kamabala api
	 * 
	 * @author Nesan
	 * 
	 */
	public RestResponse<GenericResponse> positionConversionKambala(String request, String userId) {
		RestConversionResp conversionRespModel = new RestConversionResp();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		Log.info("Kambala position conversion request" + request);
		ObjectMapper mapper = new ObjectMapper();
		try {

			accessLogModel.setMethod("positionConversion");
			accessLogModel.setModule(AppConstants.MODULE_POSITIONS);
			accessLogModel.setUrl(props.getConversionUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getConversionUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();

			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					conversionRespModel = mapper.readValue(output, RestConversionResp.class);

					if (conversionRespModel != null && StringUtil.isNotNullOrEmpty(conversionRespModel.getStat())) {

						return conversionRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)
								? prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY)
								: prepareResponse.prepareFailedResponseForRestService(conversionRespModel.getEmsg());
					}
				}

			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					RestPositionFailResp fail = mapper.readValue(output, RestPositionFailResp.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println(
								"Error Connection in position conversion api. Rsponse code -" + fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to convert position
	 * 
	 * @author Dinesh Kumar
	 * @param request
	 * @param userId
	 * @return
	 */
	public String positionConversion(String request, String userId) {
		RestConversionResp conversionRespModel = new RestConversionResp();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		Log.info("Kambala position conversion request" + request);
		ObjectMapper mapper = new ObjectMapper();
		try {

			accessLogModel.setMethod("positionConversion");
			accessLogModel.setModule(AppConstants.MODULE_POSITIONS);
			accessLogModel.setUrl(props.getConversionUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getConversionUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();

			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return "Unauthorized";

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				Log.info("Kambala position conversion response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {
					conversionRespModel = mapper.readValue(output, RestConversionResp.class);

					if (conversionRespModel != null && StringUtil.isNotNullOrEmpty(conversionRespModel.getStat())) {

						return conversionRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)
								? AppConstants.SUCCESS_STATUS
								: AppConstants.REST_STATUS_NOT_OK + " " + conversionRespModel.getEmsg();
					}
				}

			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					RestPositionFailResp fail = mapper.readValue(output, RestPositionFailResp.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println(
								"Error Connection in position conversion api. Rsponse code -" + fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return AppConstants.FAILED_STATUS;
	}

	/**
	 * method to get position for conversion
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @param userId
	 * @return
	 */
	public List<RestPositionSuccessResp> getPositionForConversion(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		List<RestPositionSuccessResp> extract = new ArrayList<>();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		Log.info("Kambala position request" + request);
		try {
			accessLogModel.setMethod("getPositionForConversion");
			accessLogModel.setModule(AppConstants.MODULE_POSITIONS);
			accessLogModel.setUrl(props.getPositionUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getPositionUrl());
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
				Log.error("Unauthorized error in kambala position api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return extract;

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						List<RestPositionSuccessResp> successRepList = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, RestPositionSuccessResp.class));
						return successRepList;
					} else if (output.startsWith("[]")) {
						Log.error(AppConstants.REST_NO_DATA);
					} else {
						RestPositionFailResp failResp = mapper.readValue(output, RestPositionFailResp.class);
						if (StringUtil.isNotNullOrEmpty(failResp.getEmsg()))
							Log.error(failResp.getEmsg());
					}
				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					RestPositionFailResp fail = mapper.readValue(output, RestPositionFailResp.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						System.out.println("Error Connection in position api. Response code -" + fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return extract;
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
				} finally {
					pool.shutdown();
				}
			}
		});
	}

}
