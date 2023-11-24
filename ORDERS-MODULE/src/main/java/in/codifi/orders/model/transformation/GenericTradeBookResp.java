package in.codifi.orders.model.transformation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericTradeBookResp {

	private String orderNo;
	private String userId;
	private String actId;
	private String exchange;
	private String ret;
	private String product;
	private String orderType;
	private String priceType;
	private String fillId;
	private String fillTime;
	private String transType;
	private String tradingSymbol;
	private String qty;
	private String token;
	private String fillshares;
	private String fillqty;
	private String pricePrecision;
	private String lotSize;
	private String tickSize;
	private String price;
	private String prcftr;
	private String fillprc;
	private String exchUpdateTime;
	private String exchOrderId;
	private String formattedInsName;
	private String ltp;
	private String orderTime;
}