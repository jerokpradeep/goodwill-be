package in.codifi.orders.model.transformation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericOrderBookResp {

	private String orderNo;
	private String userId;
	private String actId;
	private String exchange;
	private String tradingSymbol;
	private String qty;
	private String transType;
	private String ret;
	private String token;
	private String multiplier;
	private String lotSize;
	private String tickSize;
	private String price;
	private String rPrice;
	private String avgTradePrice;
	private String disclosedQty;
	private String product;
	private String priceType;
	private String orderType;
	private String orderStatus;
	private String fillShares;
	private String exchUpdateTime;
	private String exchOrderId;
	private String rQty;
	private String formattedInsName;
	private String ltp;
	private String rejectedReason;
	private String triggerPrice;
	private String mktProtection;
	private String target;
	private String stopLoss;
	private String trailingPrice;
	private String snoOrderNumber;
	private String snoFillid;
	private String orderTime;

}
