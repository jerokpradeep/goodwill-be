package in.codifi.basket.utils;

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

	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";

	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String INVALID_USER_SESSION = "Invalid user session";
	public static final String NO_RECORDS_FOUND = "No records found";
	public static final String BASKET_NAME_ALREADY_EXIST = "Basket name already exist";
	public static final String INVALID_BASKET = "Invalid basket";
	public static final String BASKET_EXECUTED = "Basket exeuted successfully";
	public static final String RECORD_DELETED = "Record Deleted";
	public static final String DELETE_FAILED = "Failed to deleted";

	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();

	public static final String HAZEL_KEY_REST_SESSION = "_REST_SESSION";

	public static final String REST_STATUS_NOT_OK = "not_ok";
	public static final String REST_STATUS_OK = "Ok";
	public static final String REST_NO_DATA = "no data";
	public static final String NO_RECORD_FOUND = "No records are found";

	public static final String MODULE_BASKET = "Basket";
	public static final String MODULE = "Basket";
	public static final String PRODUCT_TYPE = "Product Type";
	public static final String ORDER_TYPE = "Order Type";
	public static final String PRICE_TYPE = "Price Type";

}
