package in.codifi.brokerage.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerageRespModel {

	private String turnOver;
	private String brokerage;
	private String stt;
	private String transactionCharge;
	private String sebi;
	private String stampCharges;
	private String cmCharges;
	private String gst;
	private String ipft;
	private String total;

}
