package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostionAvgPriceResponseModel {

	private int id;
	private String clientId;
	private String exchange;
	private String instrumentName;
	private String symbol;
	private String expiryDate;
	private String strikePrice;
	private String optionType;
	private String netQty;
	private String netRate;
	private String token;
	private String createdOn;
	private String totalCount;

}
