package in.codifi.orders.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderReqModel {

	private String exchange;
	private String tradingSymbol;
	private String qty;
	private String price;
	private String triggerPrice;
	private String disclosedQty;
	private String product;
	private String tranType;
	private String priceType;
	private String ret;
	private String mktProtection;
	private String remarks;
	private String orderSource;
	private String bookProfitPrice;
	private String bookLossPrice;
	private String trailingPrice;
	private String amo;
	private String tsym2Leg;
	private String tranType2Leg;
	private String qty2Leg;
	private String price2Leg;
	private String tsym3Leg;
	private String tranType3Leg;
	private String qty3Leg;
	private String price3Leg;
	private String algoId;
	private String naicCode;
	private String orderNo;
}
