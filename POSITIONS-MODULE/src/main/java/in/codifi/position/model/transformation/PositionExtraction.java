package in.codifi.position.model.transformation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionExtraction {

	private String formattedInsName;
	private String tradingsymbol;
	private String exchange;
	private String segment;
	private String token;
	private String product;
	private String netQty;
	private String netBuyQty;
	private String netBuyAvgPrice;
	private String netBuyAvg;
	private String netBuyValue;
	private String netSellQty;
	private String netSellAvgPrice;
	private String netSellAvg;
	private String netSellValue;
	private String averagePrice;
	private String ltp;
	private String pnl;
	private String mtm;
	private String pdc;
	private String realizedpnl;
	private String unrealizedpnl;
	private String multiplier;
	private String lotsize;
	private String ticksize;
}
