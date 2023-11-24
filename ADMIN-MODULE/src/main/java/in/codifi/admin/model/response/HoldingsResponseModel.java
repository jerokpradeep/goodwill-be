package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoldingsResponseModel {

	private int id;
	private String userId;
	private String buyValue;
	private String buyAvg;
	private String isin;
	private String qty;
	private String symbol;
	private String bseToken;
	private String nseToken;
	private String holdingType;
	private String poaStatus;
	private int authFlag;
	private String cache;
	private String createdOn;
	private String table;

}
