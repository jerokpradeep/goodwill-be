package in.codifi.sso.auth.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class AppConstants {

	public static final String MODULE_SSO_AUTH = "SSO Auth";

	public static final String APPLICATION_JSON = "application/json";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String AUTHORIZATION = "Authorization";
	public static final String UTF_8 = "utf-8";
	public static final String ACCEPT = "Accept";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";
	public static final String PUT_METHOD = "PUT";
	public static final String DELETE_METHOD = "DELETE";
	public static final String USER_AGENT = "User-Agent";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";
	public static final int CODE_200 = 200;
	public static final int CODE_400 = 400;
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();
	public static final String TOKEN_VERIFIED = "Token verified";
	public static final String TOKEN_NOT_VERIFIED = "Token not found or not verified";
	public static final String YES = "Yes";
	public static final String NO = "No";

	/** Below variable are used for odin default x-auth-key constants **/
	public static final String X_AUTH_KEY = "x-auth-key";
	public static final String X_AUTH_KEY_VALUE = "jHFGURSDdoTAT0DhhliA2QxYyJVFF3ULVUIYhK2LAFC4Nzh8qH81YQFA7lIXbIGg";

	public static final String GUEST_USER_ERROR = "Guest User";
	public static final String ALL = "ALL";
	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String SOMETHING_WENT_WRONG = "someting went wrong";
	public static final String INVALID_USER_SESSION = "Invalid user session";
	public static final String USER_NOR_EXISTS = "User does not exists";
	public static final String INVALID_CREDENTIALS = "Invalid userId or password";
	public static final String FAILED_STATUS_VALIDATE = "Failed to validate. Please try again later";
	public static final String PARAM_EITHER = "Verify either by userId or ucc";
	public static final String PASSWORD_CHANGED_SUCCESS = "Password changed sucessfully";
	public static final String ERROR_NOT_REGISTERED_MOBIL = "Please enter registered mobile no";
	public static final String INVALID_USER = "Invalid User";
	public static final String INVALID_PAN = "Invalid PAN";
	public static final String USER_UNBLOCK_SUCCESS = "User unblocked sucessfully";
	public static final String USER_BLOCKED = "User blocked";
	public static final String INVALID_PASSWORD = "Enter valid password";
	public static final String TOTP_ALREADY_ENABLED = "T-OTP Already Enabled";
	public static final String INVALID_TOPT = "Invalid totp";
	public static final String INTERNAL_ERROR = "Something went wrong. Please try again later";
	public static final String TOTP_NOT_ENABLED = "T-OTP Not Enabled";
	public static final String NO_RECORDS_FOUND = "No Records Found";
	public static final String INVALID_OTP = "Invalid otp";

	public static final String UCC = "ucc";

	public static final String OTP_SENT = "An OTP has been sent to your registered Mobile Number";
	public static final String OTP_EXCEED = "OTP interval exceeded.";
	public static final String OTP_INVALID = "Invalid OTP";
	public static final String CANNOT_SEND_OTP = "Can't send OTP, Please contact Adminstrator";
	public static final String RESEND_FAILED = "Retry after 30 seconds";
	public static final String OTP_LIMIT_EXCEED = "You have exceeded maximun limit.Please try again after 5 mins.";
	public static final String OTP_MSG = " is your verification code as requested online, this code is valid for next 5 minutes. Regards-AliceBlue";
	public static final String CANNOT_VALIDATE = "Not able to validate, Please contact Adminstrator";

	public static final String HAZEL_KEY_USER_DETAILS = "_USER_DETAILS";
	public static final String HAZEL_KEY_REST_SESSION = "_REST_SESSION";
	public static final String HAZEL_KEY_OTP_SESSION = "_OTP_SESSION";
	public static final String HAZEL_KEY_OTP = "_OTP";
	public static final String HAZEL_KEY_OTP_RESEND = "_RESEND";
	public static final String HAZEL_KEY_OTP_HOLD = "_HOLD";
	public static final String HAZEL_KEY_OTP_RETRY_COUNT = "_RETRY_COUNT";
	public static final String HAZEL_KEY_OTP_RESET = "RESET_";
	public static final String HAZEL_KEY_PWD_RETRY_COUNT = "_PWD_RETRY_COUNT";
	public static final String COMPANY_NAME = "ALICEBLUE";

	// Rest URL
	public static final String JDATA = "jData";
	public static final String JKEY = "jKey";
	public static final String SYMBOL_EQUAL = "=";
	public static final String SYMBOL_AND = "&";

//	// Rest connection
//	public static final String BASE_URL = "http://103.210.195.138/NestHtml5MobileTech/rest/";
//	public static final String LIMITS = "Limits";
//	public static final String JSESSONID = "jsessionid";
//
	// Rest
	public static final String REST_STATUS_SUCCESS = "success";
	public static final String REST_STATUS_NOT_OK = "not_ok";
	public static final String REST_STATUS_OK = "Ok";
	public static final String REST_NO_DATA = "no data";
	public static final String NO_RECORD_FOUND = "No records are found";
	public static final String PRODUCT_TYPE = "Product Type";
	public static final String REST_STATUS_ERROR = "error";

	// Request
	public static final String COLON = ":";
	public static final String TYPE = "type";
	public static final String QUESTION_MARK = "?";
	public static final String INTROPSTATUS = "intropStatus";
	public static final String ONE = "1";

	public static final String X_API_KEY = "gZkbrXhn8A4UKEjN799BC9KOWbPzvSSq8Ta1Np0O";
	public static final String X_API_KEY_NAME = "x-api-key";

	public static final String CONTENT_LENGTH_KEY = "Content-Length";
	public static final String CONTENT_LENGTH_VALUE = "<calculated when request is sent>";
	public static final String MODULE_LOGIN = "Login";
	public static final String[] VERIFY_TOKEN_METHODS = { "/auth/verifytoken" };
	public static final String[] SECURED_METHODS = { "/access/pwd/reset", "/access/otp/send", "access/otp/validate",
			"/access/scanner/generate", "/access/scanner/get", "/access/topt/enable", "/access/topt/verify" };
	public static final String VERIFYTOKEN_PATH = "/auth/verifytoken";

	public static final String TOTP = "TOTP";

	public static final String CON_FAILED = "Connection failed, Please try after some time";
	public static final String JSESSONID = "jsessionid";
	public static final String SESSION_EXP = "Session Expired";
	public static final String KC_PASSWORD = "password";
	public static final String KC_DEAFULT_PASSWORD = "St0ne@ge@202!";

	
	public static final String INVALID_AUTH_CODE = "Invalid auth code";
	
	public static final String MAIL_SETTINGS = "mailSelection";
	
	public static final String EMAIL_API_SUBJECT = "API key for Alice Blue";
	public static final String API_NOT_AVAILABLE = "API key not available. Please generate API key";
	public static final String APIKEY_EXPIRED = "Your API key has been expired. Please contact System Administrator";
	public static final String API_EXIST = "API key already exist";
	public static final String APIKEY_GENERATED = "Your unique API key has been generated successfully.";
}
