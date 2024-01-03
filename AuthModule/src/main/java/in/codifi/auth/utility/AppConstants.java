package in.codifi.auth.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class AppConstants {

	public static final String MODULE = "Auth";

	public static final String APPLICATION_JSON = "application/json";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String AUTHORIZATION = "Authorization";
	public static final String UTF_8 = "utf-8";
	public static final String ACCEPT = "Accept";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String HOST = "Host";
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";
	public static final String PUT_METHOD = "PUT";
	public static final String DELETE_METHOD = "DELETE";
	public static final String USER_AGENT = "User-Agent";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	public static final String STATUS_OK_API = "Ok";

	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();

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
	public static final String MULTIPLE_USER_LINKED = " linked with multiple client Id's. Kindly login with client code";
	public static final String USER_NOT_VERIFIED = "User not verified";
	public static final String API_REQUEST_COUNT = "2000";
	public static final String INVALID_VENDOR = "Invalid vendor name or vendor key";

	public static final String HAZEL_KEY_USER_DETAILS = "_USER_DETAILS";
	public static final String HAZEL_KEY_REST_SESSION = "_REST_SESSION";
	public static final String HAZEL_KEY_OTP_SESSION = "_OTP_SESSION";
	public static final String HAZEL_KEY_OTP = "_OTP";
	public static final String HAZEL_KEY_OTP_RESEND = "_RESEND";
	public static final String HAZEL_KEY_OTP_HOLD = "_HOLD";
	public static final String HAZEL_KEY_OTP_RETRY_COUNT = "_RETRY_COUNT";
	public static final String HAZEL_KEY_OTP_RESET = "RESET_";
	public static final String HAZEL_KEY_PWD_RETRY_COUNT = "_PWD_RETRY_COUNT";
	public static final String MESSAGE_SETTINGS = "messageSelection";
	public static final String MAIL_SETTINGS = "mailSelection";
	public static final String SYMBOL_PIPE = "|";

	public static final String JDATA = "jData=";
	public static final String SYMBOL_AND = "&";
	public static final String JKEY = "jKey=";

	public static final String SYMBOL_EQUAL = "=";

	public static final String REST_STATUS_OK = "Ok";
	public static final String REST_STATUS_NOT_OK = "Not_Ok";
	public static final String REST_NO_DATA = "no data";

	public static final String ATTRIBUTE_MOBILE = "mobile";
	public static final String ATTRIBUTE_PAN = "pan";
	public static final String STATUS_NOT_OK_API = "Not_ok";

	public static final String OTP_MSG = " is Your OTP for Registration with Goodwill";
	public static final String OTP_SENT = "OTP sent to registered mobile No.";
	public static final String OTP_EXCEED = "OTP interval exceeded.";
	public static final String OTP_INVALID = "Invalid OTP";
	public static final String CANNOT_SEND_OTP = "Can't send OTP, Please contact Adminstrator";
	public static final String RESEND_FAILED = "Retry after 30 seconds";
	public static final String OTP_LIMIT_EXCEED = "You have exceeded maximun limit.Please try again after 5 mins.";

	public static final String SOURCE_WEB = "WEB";
	public static final String SOURCE_MOB = "MOB";
	public static final String SOURCE_API = "API";
	public static final String SOURCE_SSO = "SSO";

	public static final String[] SECURED_METHODS = { "/access/pwd/reset", "/access/otp/send", "access/otp/validate",
			"/access/scanner/generate", "/access/scanner/get", "/access/topt/enable", "/access/topt/verify" };

	public static final String COMPANY_NAME = "Goodwill";
	public static final String INVALID_AUTH_CODE = "Invalid auth code";
	public static final String TOTP = "TOTP";

	public static final String INSERTED = "Inserted Successfully";
	public static final String DEFAULT_USERS = "defaultUsers";

	public static final String KC_DEAFULT_PASSWORD = "C@difi@202#";

	public static final String FAILED_LOGIN = "Failed to Login - Please contact admin";

	public static final String NEW_PWD_GENERATED = "New password is send through mail / sms";

	public static final String USER_BLOCKED_ADMIN = "Your account has been blocked by adminstrator, please contact admin ( Unblock Accout option wont work )";

	public static final String HAZEL_KEY_PWD = "_PWD";


}