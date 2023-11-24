package in.codifi.orders.model.transformation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHisRespModel {
	private String orderNo;
	private String userId;
	private String actId;
	private String exchange;
	private String tradingSymbol;
	private String quantity;
	private String transType;
	private String priceType;
	private String orderType;
	private String ret;
	private String token;
	private String pricePrecision;
	private String lotSize;
	private String tickSize;
	private String price;
	private String avgPrice;
	private String disclosedQty;
	private String product;
	private String status;
	private String report;
	private String fillshares;
	private String time;
	private String exchTime;
	private String remarks;
	private String exchOrderNo;
}