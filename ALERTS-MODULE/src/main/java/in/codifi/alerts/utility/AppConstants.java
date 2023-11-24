package in.codifi.alerts.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class AppConstants {

	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";
	public static final String POST_METHOD = "POST";
	public static final String UTF_8 = "utf-8";
	public static final String ACCEPT = "Accept";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String REST_STATUS_OK = "Ok";

	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();

	public static final String REST_NO_DATA = "no data";

	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String NO_DATA = "No data";
	public static final String INVALID_ALERT = "Invalid Alert";

	// Push notification
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String AUTHORIZATION = "Authorization";
	public static final String KEY_EQUAL = "key=";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	public static final String FLUTTER_NOTIFICATION_CLICK = "FLUTTER_NOTIFICATION_CLICK";
	public static final String COLLAPSE_KEY = "1234";
	public static final String CUSTOM_NOTIFICATION_CODIFI = "custom_notification_codifi";
	public static final String TITLE_COLOR = "#2a6d57";
	public static final String ALERT_TRIGGERED = "Alert Triggered - ";
	public static final String INFO = "Info";

	// mail
	public static final String TEXT_HTML = "text/html";

	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_USER = "mail.smtp.user";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	public static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
	public static final String TRUE = "true";
	public static final String TLSV1_2 = "TLSv1.2";
	public static final String EMAIL_EXCEPTION = "sendEmail--either userName or emailId is null";

	public static final String GUEST_USER_ERROR = "Guest User";

	public static final String MODULE_ALERTS = "Alerts";

	public static final String JDATA = "jData";
	public static final String JKEY = "jKey";
	public static final String SYMBOL_EQUAL = "=";
	public static final String SYMBOL_AND = "&";
	public static final String HAZEL_KEY_REST_SESSION = "_REST_SESSION";

}
