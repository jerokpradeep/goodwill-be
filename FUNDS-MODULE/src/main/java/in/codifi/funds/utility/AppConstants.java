package in.codifi.funds.utility;

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
	public static final String HOST = "Host";

	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();
	public static final int SUCCESS = 1;

	public static final String ERROR_MIN_CHAR = "Minimum 2 characters required";
	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String NOT_FOUND = "No record found";
	public static final String RECORD_DELETED = "Record Deleted";
	public static final String DELETE_FAILED = "Failed to deleted";
	public static final String NO_DATA = "No Data";
	public static final String TOKEN_NOT_EXISTS = "The token does not exists";
	public static final String INVALID_USER_SESSION = "Invalid user session";
	public static final String GUEST_USER_ERROR = "Guest User";
	public static final String ACTIVE_USER = "ACTIVE_USER";

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
	public static final String REST_STATUS_OK = "Ok";
	public static final String REST_NO_DATA = "no data";
	public static final String NO_RECORD_FOUND = "No records are found";

	public static final String MODULE_FUNDS = "Funds";

	public static final String PAYMENT_FAILED_ID_NULL = "Payment Creation Failed referId is null!";
	public static final String AMOUNT_ZERO = "Amount is Zero";
	public static final String NOTES = "notes";
	public static final String UPI = "upi";
	public static final String NET_BANKING = "netbanking";
	public static final String AMOUNT = "amount";
	public static final String CURRENCY = "currency";
	public static final String ONHOLD = "on_hold";
	public static final String RECEIPT = "receipt";
	public static final String BANK_ACCOUNT = "bank_account";
	public static final String METHOD = "method";
	public static final String ACCOUNT_NUMBER = "account_number";
	public static final String ACCOUNT_NAME = "name";
	public static final String ACCOUNT_IFSC = "ifsc";
	public static final String EMPTY_PARAMETER = "The parameter is null";
	public static final String PAYMENT_CREATION_FAILED = "Payment Creation Failed Check Server!";
	public static final String INTERNAL_ERROR = "Something went wrong. Please try again later";
	public static final String VERIFY_SUCCEED = "Verified and updated sucessfully";
	public static final String VERIFY_NOT_SUCCEED = "Verify not succeed";
	public static final String CANNOT_GET_BANK_DETAILS = "Cannot get bank details";

	// ** Email **//
	public static final String PAYOUT_SUBJECT = "Pay out Failure : AliceBlue";
	public static final String PAYMENT_SUBJECT = "Pay In Failure : AliceBlue";

	// ** RAZORPAY **//
	public static final String RAZORPAY_NOTES = "notes";
	public static final String RAZORPAY_UPI = "upi";
	public static final String RAZORPAY_NET_BANKING = "netbanking";
	public static final String RAZORPAY_AMOUNT = "amount";
	public static final String RAZORPAY_CURRENCY = "currency";
	public static final String RAZORPAY_RECEIPT = "receipt";
	public static final String RAZORPAY_BANK_ACCOUNT = "bank_account";
	public static final String RAZORPAY_METHOD = "method";
	public static final String RAZORPAY_CURRENCY_INR = "INR";
	public static final String RAZORPAY_ACCOUNT_NUMBER = "account_number";
	public static final String RAZORPAY_ACCOUNT_NAME = "name";
	public static final String RAZORPAY_ACCOUNT_IFSC = "ifsc";
	public static final String RAZORPAY_CLIENT_CODE = "clientcode";
	public static final String RAZORPAY_ORDERID = "razorpay_order_id";
	public static final String RAZORPAY_PAYMENTID = "razorpay_payment_id";
	public static final String RAZORPAY_SIGNATURE = "razorpay_signature";

}
