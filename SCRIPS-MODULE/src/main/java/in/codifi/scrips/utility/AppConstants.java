package in.codifi.scrips.utility;

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
	public static final String REST_NO_DATA = "no data";

	/* jsch constants - SFTP */
	public static final String CONTRACT_FILE_NMAE = "tbl_global_contract_master_details_";
	public static final String STOCK_RETURN_FILE_NAME = "tbl_global_returnofscrip_";
	public static final String MTF_FILE_NAME = "tbl_mtf_approved_data_";
	public static final String SQL = ".sql";
	public static final String STRICTHOSTKEYCHECKING = "StrictHostKeyChecking";
	public static final String NO = "no";
	public static final String SFTP = "sftp";
	public static final String LOCALHOST = "127.0.0.1";
	public static final int PORT_3308 = 3308;
	public static final int PORT_3306 = 3306;
	public static final String FOLDERSUCCESS = "success/";
	public static final String FOLDERFAILED = "failed/";
	public static final String PNL_LOT = "PNL_LOT";

	public static final String MODULE_SCRIPS = "Scrips";
	public static final String LOADED = "Loaded Successfully";
	public static final String NO_RECORDS_FOUND = "No Records Found";
	public static final String MTF_LOAD_SUCCESS = "Mtf Loaded Successfully";
	public static final String MTF_LOAD_FAILED = "Failed to load mtf file";

}
