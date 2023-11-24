package in.codifi.position.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionConversionReq {

	private String exchange;
	private String tradingSymbol;
	private String qty;
	private String product;
	private String prevProduct;
	private String transType;
	private String posType;
	private String token;
	private String productToCode;
	private String orderSource;
}
