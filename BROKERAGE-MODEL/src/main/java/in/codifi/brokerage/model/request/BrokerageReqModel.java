package in.codifi.brokerage.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerageReqModel {
	private String user;
	private String segment;
	private String price;
	private String qty;
	private String token;
	private String plan;
	private String type;
	private String lotSize;
	private String transactionType;
	private String instrumentType;

}
