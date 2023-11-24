package in.codifi.common.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class AppConstants {

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

	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();

	public static final String REST_STATUS_NOT_OK = "not_ok";
	public static final String REST_NO_DATA = "no data";
	public static final String NO_SCRIP = "No Scrip";

	public static final String INVALID_PARAMETERS = "Invalid Parameters";
	public static final String NO_RECORDS_FOUND = "No Records Found";
	public static final String VERSION_MSG = "A New Update is available";
	public static final String UPDATE_AVAILABLE = "isUpdateAvailable";

	public static final String INSERTED = "Inserted Successfully";
	public static final String LOADED = "Loaded successfully";
	public static final String DELETED = "Deleted successfully";

	public static final String HAZEL_KEY_INDICES = "INDICES";
	public static final String HAZEL_KEY_ETF = "ETF";
	public static final String HAZEL_KEY_EQSECTOR = "EQSECTOR";
	public static final String HAZEL_KEY_SECTOR_HEATMAP = "SECTOR_HEATMAP";
	public static final String HAZEL_KEY_FUTURE_MONTH = "FUTURE_MONTH";
	public static final String HAZEL_KEY_FUTURES = "FUTURES";
	public static final String HAZEL_KEY_MARKETING_CARDS = "MARKETING_CARDS";
	public static final String HAZEL_KEY_PREFERENCES = "DASHBOARD_PREFERENCES";
	public static final String HAZEL_KEY_FII_INDEX = "FII_INDEX";
	public static final String HAZEL_KEY_VERSION = "version";

	/** DASH BOARD Preferences **/
	public static final String INVESTING = "Investing";
	public static final String TRADING = "Trading";
	public static final String F = "0";
	public static final String INVESTING_MCARD = "mcd";
	public static final String INVESTING_INDICES = "ids";
	public static final String INVESTING_TOP_SECTORS = "tsr";
	public static final String INVESTING_SCANNERS = "sns";
	public static final String TRADING_HEATMAP = "htm";
	public static final String TRADING_FII = "fid";

	public static final String GUEST_USER_ERROR = "Guest User";
	public static final String MODULE_COMMON = "Common";

	public static final String JTENANT_TOKEN_KEY = "jtenanttoken";
	public static final String JTENANT_TOKEN_VALUE = "1";

	public static final String JTENANT_ID = "jtenantid";
	public static final String JTENANT_ID_VALUE = "1404";
	public static final String X_API_KEY_NAME = "x-api-key";

	// Rest
	public static final String REST_STATUS_SUCCESS = "success";
	public static final String REST_STATUS_TRUE = "true";
	public static final String REST_STATUS_OK = "Ok";
	public static final String NO_RECORD_FOUND = "No records are found";
	public static final String NO_DATA_FOUND = "No data found";
	public static final String PRODUCT_TYPE = "Product Type";
	public static final String ORDER_TYPE = "Order Type";
	public static final String PRICE_TYPE = "Price Type";
	public static final String REST_STATUS_ERROR = "error";
	public static final String UPDATED_TIME = "AnalysisUpdatedTime";
	public static final String INTERNAL_ERROR = "Something went wrong. Please try again later";

}
