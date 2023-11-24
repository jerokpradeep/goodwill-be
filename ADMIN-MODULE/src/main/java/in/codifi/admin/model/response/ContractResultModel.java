package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractResultModel {

	private String symbol;
	private String groupName;
	private Object optionType;
	private Object expiryDate;
	private String instrumentType;
	private String exchangeSegment;
	private String formattedInsName;
	private String tickSize;
	private String token;
	private Object tradingSymbol;
	private String instrumentName;
	private String lotSize;
	private String isin;
	private Object strikePrice;

}
