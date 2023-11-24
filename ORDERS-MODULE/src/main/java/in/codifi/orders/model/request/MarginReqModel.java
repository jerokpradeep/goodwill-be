package in.codifi.orders.model.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarginReqModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exchange;
	private String tradingSymbol;
	private String qty;
	private String price;
	private String product;
	private String transType;
	private String priceType;
	private String orderType;

	/** Applicable if order type is SL-LMT/SL-MKT **/
	private String triggerPrice;

	/** Applicable only for Modify order **/
	private String openOrderQty;
	private String fillshares;
	private String openOrderprice;
	private String openOrderTriggerPrice;
	/** Applicable for Modify order if order is COVER/BRACKET **/
	private String orderNo;
	private String snonum;
	private String stopLoss;

}
