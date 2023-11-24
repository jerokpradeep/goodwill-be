package in.codifi.basket.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketMarginRequest {

	private String exchange;
	private String tradingSymbol;
	private String qty;
	private String price;
	private String product;
	private String transType;
	private String priceType;
	private String triggerPrice;
}
