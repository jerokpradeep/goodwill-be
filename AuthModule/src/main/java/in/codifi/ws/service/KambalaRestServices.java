package in.codifi.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.auth.config.RestPropertiesConfig;
import in.codifi.auth.entity.logs.RestAccessLogModel;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.CommonUtils;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.ws.model.client.LogoutRestRespModel;
import in.codifi.ws.model.kb.login.ForgotOTPRestRespModel;
import in.codifi.ws.model.kb.login.ForgotPwdRestRespModel;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import in.codifi.ws.model.kc.ChangePwdRestRespModel;
import in.codifi.ws.model.kc.UnblockUsersRestRespModel;
import io.quarkus.logging.Log;

@Component
public class KambalaRestServices {

	@Inject
	RestPropertiesConfig props;

	@Inject
	AccessLogManager accessLogManager;

	@Inject
	PrepareResponse prepareResponse;

	ObjectMapper mapper = new ObjectMapper();

	/**
	 * Method to call kambala API to get user session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public QuickAuthRespModel quickAuthBypass(String request, String source, String userId) {

		QuickAuthRespModel respModel = null;
		try {
			Log.info("Kambal Login req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = "";
			if (source.equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
				baseUrl = props.getKambalaMobBaseUrl();
			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
				baseUrl = props.getKambalaWebBaseUrl();
			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_API)) {
				baseUrl = props.getKambalaApiBaseUrl();
			}

			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("quickAuthBypass");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaMethodAuth());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambal Login urls-" + baseUrl + props.getKambalaMethodAuth());
			URL url = new URL(baseUrl + props.getKambalaMethodAuth());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			} else {
				System.out.println("Error Connection in place Order api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.error(e1.getMessage());
		}

		return respModel;
	}

	/**
	 * Method to call kambala API to get user session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public QuickAuthRespModel quickAuthBypassLogin(String request, String source, String userId) {

		QuickAuthRespModel respModel = null;
		try {
			Log.info("Kambal Login req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaBaseUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("quickAuthBypassLogin");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaMethodAuth());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala Login urls-" + baseUrl + props.getKambalaMethodAuth());
			URL url = new URL(baseUrl + props.getKambalaMethodAuth());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			} else {
				System.out.println("Error Connection in Login api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.error(e1.getMessage());
		}

		return respModel;
	}

	/**
	 * Method to call kambala API to get user session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public QuickAuthRespModel quickAuthBypassLoginforWeb(String request, String source, String userId) {

		QuickAuthRespModel respModel = null;
		try {
			Log.info("Kambal web Login req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaWebLoginUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("quickAuthBypassLoginForWeb");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl);
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala web Login urls-" + baseUrl);
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal web Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			} else {
				System.out.println("Error Connection in web Login api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal web Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.error(e1.getMessage());
		}

		return respModel;
	}

	/**
	 * Method to call kambala API to get user session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public QuickAuthRespModel quickAuthBypassLoginForMob(String request, String source, String userId) {

		QuickAuthRespModel respModel = null;
		try {
			Log.info("Kambal mob Login req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaMobLoginUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("quickAuthBypassLoginForMob");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl);
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala Login for mob urls-" + baseUrl);
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal Login mob Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			} else {
				System.out.println("Error Connection in mob Login api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal mob Login Resp - " + output);
					respModel = mapper.readValue(output, QuickAuthRespModel.class);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.error(e1.getMessage());
		}

		return respModel;
	}

	/**
	 * 
	 * Method to get client details to validate the session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @param source
	 * @return
	 */
	public String getUserDetails(String request, String source, String userId) {
		Log.info("User details request" + request);
		try {
			AppUtils.trustedManagement();
			String baseUrl = "";

			if (source.equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
				baseUrl = props.getKambalaMobBaseUrl();
			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
				baseUrl = props.getKambalaWebBaseUrl();
			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_API)) {
				baseUrl = props.getKambalaApiBaseUrl();
			}
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getUserDetails");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaMethodAuth());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("User detail urls-" + baseUrl + props.getKambalaMethodUserDetails());
			URL url = new URL(baseUrl + props.getKambalaMethodUserDetails());
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
			Log.info("User details request - " + responseCode);
			if (responseCode == 401) {
				insertRestAccessLogs(accessLogModel);
				Log.error("Unauthorized error in client details");
				return AppConstants.UNAUTHORIZED;
			} else if (responseCode == 200) {
				insertRestAccessLogs(accessLogModel);
				return AppConstants.SUCCESS_STATUS;
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return AppConstants.FAILED_STATUS;
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

	/**
	 * method to logout
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @param request
	 * @param source
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> logout(String request, String source, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * method to forgot password
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> forgotPwd(String request, String userId) {
		ForgotPwdRestRespModel respModel = null;
		try {
			Log.info("Kambal forgot pwd  req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaBaseUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("forgotPwd");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaForgotPwd());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala forgot pwd urls-" + baseUrl + props.getKambalaForgotPwd());
			URL url = new URL(baseUrl + props.getKambalaForgotPwd());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal forgot pwd Resp - " + output);
					respModel = mapper.readValue(output, ForgotPwdRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(respModel.getStat())
							&& respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return prepareResponse.prepareSuccessMessage(AppConstants.NEW_PWD_GENERATED);
					} else {
						return prepareResponse.prepareSuccessMessage(respModel.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in forgot pwd api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal forgot pwd Resp - " + output);
					respModel = mapper.readValue(output, ForgotPwdRestRespModel.class);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.error(e1.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to forgot password otp
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @param userId
	 * @return
	 */
	public ForgotOTPRestRespModel forgotPwdOtp(String request, String userId) {
		ForgotOTPRestRespModel respModel = new ForgotOTPRestRespModel();
		try {
			Log.info("Kambal forgot pwd otp req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaBaseUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("forgotPwdOtp");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaForgotPwdOtp());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala forgot pwd otp urls-" + baseUrl + props.getKambalaForgotPwdOtp());
			URL url = new URL(baseUrl + props.getKambalaForgotPwdOtp());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal forgot pwd Resp - " + output);
					respModel = mapper.readValue(output, ForgotOTPRestRespModel.class);
				}
			} else {
				System.out.println("Error Connection in forgot pwd api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal forgot pwd Resp - " + output);
					respModel = mapper.readValue(output, ForgotOTPRestRespModel.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("forgotPwdOtp", e);
		}
		return respModel;
	}

	/**
	 * method to unblock users
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> unblockUsers(String request, String userId) {
		UnblockUsersRestRespModel respModel = new UnblockUsersRestRespModel();
		try {
			Log.info("Kambala unblock users req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaBaseUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("unblockUsers");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaUnblockUsers());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala unblock users urls-" + baseUrl + props.getKambalaUnblockUsers());
			URL url = new URL(baseUrl + props.getKambalaUnblockUsers());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambala unblock users Resp - " + output);
					respModel = mapper.readValue(output, UnblockUsersRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(respModel.getStat())
							&& respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return prepareResponse.prepareSuccessMessage(respModel.getDmsg());
					} else {
						return prepareResponse.prepareSuccessMessage(respModel.getDmsg());
					}
				}
			} else {
				System.out.println("Error Connection in unblock users api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal unblock users Resp - " + output);
					respModel = mapper.readValue(output, UnblockUsersRestRespModel.class);
					return prepareResponse.prepareSuccessResponseObject(respModel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("unblockUsers", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to change password
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> changePwd(String request, String userId) {
		ChangePwdRestRespModel respModel = new ChangePwdRestRespModel();
		try {
			Log.info("Kambala change password req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaBaseUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("changePwd");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaChangePwd());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala change password urls-" + baseUrl + props.getKambalaChangePwd());
			URL url = new URL(baseUrl + props.getKambalaChangePwd());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambala change password users Resp - " + output);
					respModel = mapper.readValue(output, ChangePwdRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(respModel.getStat())
							&& respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return prepareResponse.prepareSuccessMessage(respModel.getDmsg());
					} else {
						return prepareResponse.prepareSuccessMessage(respModel.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in change password api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambal change password Resp - " + output);
					respModel = mapper.readValue(output, ChangePwdRestRespModel.class);
					return prepareResponse.prepareSuccessResponseObject(respModel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("change password", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to logout
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> logout(String request, String userId) {
		LogoutRestRespModel respModel = new LogoutRestRespModel();
		try {

			Log.info("Kambala logout req - " + request);
			AppUtils.trustedManagement();
			String baseUrl = props.getKambalaBaseUrl();
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("logout");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setUrl(baseUrl + props.getKambalaLogout());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			Log.info("Kambala logout urls-" + baseUrl + props.getKambalaLogout());
			URL url = new URL(baseUrl + props.getKambalaLogout());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty("Accept", AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambala logout Resp - " + output);
					respModel = mapper.readValue(output, LogoutRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(respModel.getStat())
							&& respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return prepareResponse.prepareSuccessResponseObject(respModel);
					} else {
						return prepareResponse.prepareSuccessMessage(respModel.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in logout api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					Log.info("Kambala logout Resp - " + output);
					respModel = mapper.readValue(output, LogoutRestRespModel.class);
					return prepareResponse.prepareSuccessResponseObject(respModel);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("logout", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to get client details to validate the session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @param source
	 * @return
	 */
	public String getUserDetails(String request, String source) {
		Log.info("User details request" + request);
		try {
			CommonUtils.trustedManagement();
			String baseUrl = "";
//			if (source.equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
//				baseUrl = props.getKambalaMobBaseUrl();
//			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
//				baseUrl = props.getKambalaWebBaseUrl();
//			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_API)) {
//				baseUrl = props.getKambalaApiBaseUrl();
//			}

			if (source.equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
				baseUrl = props.getKambalaWebBaseUrl();
			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
				baseUrl = props.getKambalaWebBaseUrl();
			} else if (source.equalsIgnoreCase(AppConstants.SOURCE_API)) {
				baseUrl = props.getKambalaWebBaseUrl();
			}

			Log.info("User detail urls-" + baseUrl + props.getKambalaMethodUserDetails());
			URL url = new URL(baseUrl + props.getKambalaMethodUserDetails());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			if (responseCode == 401) {
				Log.error("Unauthorized error in client details");
				return AppConstants.UNAUTHORIZED;
			} else if (responseCode == 200) {
				return AppConstants.SUCCESS_STATUS;
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return AppConstants.FAILED_STATUS;
	}

}
