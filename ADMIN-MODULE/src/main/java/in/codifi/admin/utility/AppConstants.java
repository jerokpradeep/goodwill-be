package in.codifi.admin.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class AppConstants {

	public static final String FAILED_STATUS = "Failed";
	public static final String STATUS_OK = "Ok";
	public static final String STATUS_NOT_OK = "Not ok";
	public static final String SUCCESS_STATUS = "Success";
	public static final String FAILED_CODE = "400";
	public static final String SUCCESS_CODE = "200";

	public static final String INVALID_PARAMETERS = "Invalid Parameters";
	public static final String INVALID_EXCH = "Invalid Exch";
	public static final String NO_RECORDS_FOUND = "No Records Found";
	public static final String VERSION_MSG = "A New Update is available";
	public static final String UPDATE_AVAILABLE = "isUpdateAvailable";

	public static final List<JSONObject> EMPTY_ARRAY = new ArrayList<>();

	public static final String EQ_SECTOR_MASTER = "EQ_SECTOR_MASTER";
	public static final String EQ_SECTOR_LIST = "EQ_SECTOR_LIST";

	public static final String HEATMAP = "heatMap";
	public static final String HEATMAP_SCRIP = "heatMapScrip";
	public static final String FUTURE_SECTOR = "futureSector";
	public static final String FUTURE_SECTOR_SCRIP = "futureSectorScrip";
	public static final String VERSION = "version";
	public static final String NO_SCRIP = "No Scrip";
	public static final String HEATMAP_SCRIP_SECID = "heatMapScripSecId";
	public static final String FUTURE_SCRIP_SECTORID = "futureScripSectorId";
	public static final String Etf_Master = "etfMaster";
	public static final String Indices_Master = "indicesMaster";
	public static final String Marketing_Entity = "masketingEntity";
	public static final String Futures_Master = "futuresMasterEntity";
	public static final String Futures_Entity = "futuresEntity";
	public static final String Indices_Details = "indicesDetails";
	public static final String RELOADED_SUCCESSFULLY = "reloaded successfully";
	public static final String CURRENT_MONTH_FUTURE = "CURRENT_MONTH_FUTURE";
	public static final String INSERTED = "Inserted Successfully";
	public static final String LOADED = "Loaded successfully";
	public static final String DELETED = "Deleted successfully";
	public static final String TABLES_CREATED = "Tables Created Successfully";
	public static final String INVALID_FILE_TYPE = "Invalid File type";
	public static final String INVALID_REQUEST_ADMIN = "Invalid request";
	public static final String INTERNAL_ERROR = "Something went wrong, Please trygain after some time";
	public static final String EMPTY_PARAMETERS = "Parameter is null";
	public static final String FILE_UPLOADED = "File Uploaded";
	public static final String EXCEL_FILE_FORMATS = ".xls";
	public static final String TEXT_FILE_FORMATS = ".txt";
	public static final String HOLDINGS_FILE_FORMATS = ".TXT";
	public static final String INSERTED_FAILED = "Inserted Failed";
	public static final String T1TYPE = "T1";
	
	public static final String ALL = "all";
	public static final String INDIVIDUAL = "individual";
	public static final String UPDATED = "Updated";
	//push notification
	public static final String NO_DEVICE_IS_FOR_USER = "No device id found for the given user ";
	
	
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final String ATTRIBUTE_MOBILE = "mobile";
	public static final String ATTRIBUTE_MAIL = "email";
	public static final String ATTRIBUTE_PAN = "pan";
	public static final String INVALID_PARAMETER = "Invalid Parameter";
	public static final String USER_BLOCKED = "User blocked";
	public static final String PLEASE_ENTER_VALID_CREDENTIALS = "Please Enter Valid Credentials";
	public static final String MULTIPLE_USER_LINKED = " linked with multiple client Id's. Kindly login with client code";
	public static final String INVALID_CREDENTIALS = "Invalid userId or password";
	
	public static final String USER_UPDATED = "User details updated";
	public static final String USER_NOT_UPDATED = "User details not updated";
	public static final String ADMIN_USER = "Admin";

}
