package in.codifi.brokerage.utility;

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
	public static final String DTO_NULL = "The Request Param is null";
	public static final String USER_NULL = "User is null";
	public static final String PLAN_NULL = "User Plan is null";
	public static final String SEGMENT_NULL = "Segment is null";
	public static final String TYPE_NULL = "Type is null";
	public static final String TRANSACTION_TYPE_NULL = "Transaction Type is null";
	public static final String PRICE_NULL = "Price is null";
	public static final String LOTSIZE_NULL = "Lot Size is null";
	public static final String QUANTITY_NULL = "Qty is null";
	public static final String TOKEN_NULL = "Token is null";
	public static final String INSTRUMENT_TYPE_NULL = "Instrument Type is null";
	public static final String CONST_LOT = "Lot";
	public static final String CONST_TURN_OVER = "TurnOver";
	public static final String CONST_ORDER = "Order";
	public static final String CONST_LOW = "low";
	public static final String BUY = "Buy";
	public static final String SELL = "Sell";
	public static final String PRICE_INVALID = "Price is not valid. Please enter a valid price with only one decimal point.";

}
