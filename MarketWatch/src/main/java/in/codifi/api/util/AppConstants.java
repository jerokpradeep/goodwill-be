package in.codifi.api.util;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class AppConstants {
	public static final String INSERT_ACCESS_LOG = "accessLog";
	public static final String LOAD_NEW_MW = "loadNewMw";

	public static final String APPLICATION_JSON = "application/json";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";
	public static final String PUT_METHOD = "PUT";
	public static final String DELETE_METHOD = "DELETE";
	public static final String UTF_8 = "utf-8";
	public static final String ACCEPT = "Accept";

	// Sucess and failed message
	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STAT_NOT_OK = "Not ok";
	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();

	// Markert Watch Success and failed Message
	public static final String LIMIT_REACHED_MW = "You have reached the maximum length of Match watch";
	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String INTERNAL_ERROR = "Something went wrong, Please try after some time";
	public static final String MARKET_WATCH_CREATED = "Market Watch Created Successfully";
	public static final String NO_MW = "No Data";
	public static final String MW_LIST_SIZE = "marketWatchListSize";
	public static final String MW_NAME_UPDATED = "Market watch name updated successfully";
	public static final String MW_IS_FULL = "Market Watch is Full";
	public static final String SYMBOL_ALREADY_EXISTIS = "Symbol already exixts";
	public static final String NOT_ABLE_TO_ADD_CONTRACT = "Not able to add contract";
	public static final String RECORD_DELETED = "Record Deleted";
	public static final String DELETE_FAILED = "Failed to deleted";
	public static final Object LOAD_SUCCESS = "Data loaded Sucessfully";

	public static final String NO_RECORDS_FOUND = "No Records Found";
	public static final String INVALID_USER_SESSION = "Invalid User Session";

	// Time to delete the Expired Scrips
	public static final String EXP_HOUR = "exp_hour";
	public static final String EXP_MINUTE = "exp_minute";
	public static final String TIME_DIFF = "timeDiff";

	// For Mail
	// For Sending Email
	public static final String FROM = "from";
	public static final String HOST = "host";
	public static final String USER_NAME = "username";
	public static final String PSW = "password";
	public static final String PORT = "port";
	public static final String EMAIL_ID1 = "email_id1";
	public static final String EMAIL_ID2 = "email_id2";
	public static final String EMAIL_ID3 = "email_id3";
	public static final String EMAIL_ID4 = "email_id4";

	// Base URL for Getting LTP
	public static final String GET_LTP_BASE_URL = "getLtpBaseUrl";
	public static final String UPDATE_QUERIES = "updateQueries";
	public static final String RUNNING_STATUS = "runningStatus";

	public static final String MARKET_WATCH_LIST_NAME = "MWList";
	public static final int MW_SIZE = 5;
	public static final String PREDEFINED_MW = "pre-defined_MW_List";

	public static final String TEMP_USER_ID = "CD-ADMIN";

	public static final String MODULE_MW = "Marketwatch";
	public static final String COLOR_CODE_GREEN = "F6FDF4";
	public static final String COLOR_CODE_RED = "FFE0DD";

}