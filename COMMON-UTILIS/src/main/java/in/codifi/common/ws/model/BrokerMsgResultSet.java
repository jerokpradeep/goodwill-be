package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrokerMsgResultSet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("MsgSrNo")
	private String msgSrNo;
	@JsonProperty("SendMsgTime")
	private String sendMsgTime;
	@JsonProperty("Subject")
	private String subject;
	@JsonProperty("Message")
	private String message;
	@JsonProperty("MsgType")
	private String msgType;

}