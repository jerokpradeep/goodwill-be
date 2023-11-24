package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketWatchResponseModel {

	private int id;
	private String userId;
	private String mwId;
	private String token;
	private String alterToken;
	private String exch;
	private String exchSeg;
	private String tradingSymbol;
	private String formattedInsName;
	private String groupName;
	private String instrumentType;
	private String expiryDate;
	private String lotSize;
	private String optionType;
	private String pdc;
	private String sortingOrder;
	private String strikePrice;
	private String symbol;
	private String tickSize;

}
