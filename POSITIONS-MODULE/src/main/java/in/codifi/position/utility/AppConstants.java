package in.codifi.position.utility;

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

	public static final String ERROR_MIN_CHAR = "Minimum 2 characters required";
	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String NOT_FOUND = "No record found";
	public static final String RECORD_DELETED = "Record Deleted";
	public static final String DELETE_FAILED = "Failed to deleted";
	public static final String NO_DATA = "No Data";
	public static final String TOKEN_NOT_EXISTS = "The token does not exists";
	public static final String INVALID_USER_SESSION = "Invalid user session";
	public static final String GUEST_USER_ERROR = "Guest User";

	public static final String REST_SUCCESS_STATUS = "success";
	public static final String REST_ERROR_STATUS = "error";
	public static final String STATUS_NOT_OK_API = "Not_Ok";

	public static final String SESSION_EXP_API = "Session Expired";

	/**
	 * For Cache
	 */
	public static final String FETCH_DATA_FROM_CACHE = "fetchDataFromCache";

	public static final String HAZEL_KEY_REST_SESSION = "_REST_SESSION";

	public static final String CONTRACT_LOAD_SUCESS = "Contract loaded sucessfully";
	public static final String CONTRACT_LOAD_FAILED = "Failed to load contract";
	public static final String CACHE_LOADED = "Cache loaded sucessfully";

	public static final String JDATA = "jData";
	public static final String JKEY = "jKey";
	public static final String SYMBOL_EQUAL = "=";
	public static final String SYMBOL_AND = "&";

	public static final String REST_STATUS_NOT_OK = "not_ok";
	public static final String REST_STATUS_OK = "Ok";
	public static final String REST_NO_DATA = "no data";
	public static final String NO_RECORD_FOUND = "No records are found";

	public static final String MODULE_POSITIONS = "Positions";
	public static final String CANNOT_CONVERT_CNC = "You do not have any holdings or positions in CNC for this scrip. Please check your holdings or Order book before converting this position.";

	public static final String PRODUCT_TYPE = "Product Type";
	public static final String ORDER_TYPE = "Order Type";
	public static final String PRICE_TYPE = "Price Type";
	public static final String AMO = "AMO";
	public static final String BRACKET = "Bracket";
	public static final String COVER = "Cover";
	public static final String REGULAR = "Regular";
	public static final String REST_BRACKET = "B";
	public static final String REST_COVER = "H";
	public static final String PRODUCT_MIS = "MIS";
	public static final String PRODUCT_CNC = "CNC";
	public static final String PRODUCT_NRML = "NRML";
	public static final String PRODUCT_MTF = "MTF";
	public static final String REST_PRODUCT_MIS = "I";
	public static final String REST_PRODUCT_CNC = "C";
	public static final String REST_PRODUCT_NRML = "M";
	public static final String REST_PRODUCT_MTF = "F";
	public static final String HOST = "Host";
	public static final String PNL_LOT = "PNL_LOT";

}
