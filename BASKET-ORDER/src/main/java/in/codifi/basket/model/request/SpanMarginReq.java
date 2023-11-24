package in.codifi.basket.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpanMarginReq {

	private String token;
	private String exchange;
	private String price;
	private String qty;
	private String transType;

}
