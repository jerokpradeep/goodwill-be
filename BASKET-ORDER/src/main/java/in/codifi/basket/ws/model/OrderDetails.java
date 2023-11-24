package in.codifi.basket.ws.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetails {

	private String exchange;
	private String tradingSymbol;
	private String qty;
	private String price;
	private String orderType;
	private String product;
	private String priceType;
	private String transType;
	private String ret;
	private String triggerPrice;
	private String disclosedQty;
	private String mktProtection;
	private String target;// bookProfitPrice
	private String stopLoss;// bookLossPrice
	private String trailingStopLoss;
	private String orderNo;
	private String source;
}
