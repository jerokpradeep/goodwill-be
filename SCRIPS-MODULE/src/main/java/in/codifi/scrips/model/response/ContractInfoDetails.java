package in.codifi.scrips.model.response;

import java.util.Date;
import java.util.List;

import in.codifi.scrips.entity.chartdb.PromptModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractInfoDetails {

	private String exchange;
	private String token;
	private String tradingSymbol;
	private String lotSize;
	private String tickSize;
	private String symbol;
	private String formattedInsName;
	private String pdc;
	private String insType;
	private Date expiry;
	private List<PromptModel> prompt;

}
