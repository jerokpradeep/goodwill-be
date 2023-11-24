package in.codifi.alerts.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerMsgRespModel {

	private String msgType;

	private String msg;

	private String note;

	private String msgSubType;

}
