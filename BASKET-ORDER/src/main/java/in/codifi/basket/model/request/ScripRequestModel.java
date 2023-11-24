package in.codifi.basket.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScripRequestModel {

	private long id;

	private String sortOrder;

	private String exchange;

	private String token;

	private String qty;

	private String tradingSymbol;

	private String price;

	private String product;

	private String transType;

	private String priceType;

	private String orderType;

	private String ret;

	private String triggerPrice;

	private String disClosedQty;

	private String mktProtection;

	private String target;

	private String stopLoss;

	private String trailingStopLoss;

	private String source;

	private String createdBy;

}
