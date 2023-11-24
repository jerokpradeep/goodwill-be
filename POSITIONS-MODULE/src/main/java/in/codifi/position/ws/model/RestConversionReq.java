package in.codifi.position.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestConversionReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("exch")
	private String exch;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("qty")
	private String qty;
	@JsonProperty("uid")
	private String uid;
	@JsonProperty("actid")
	private String actid;
	@JsonProperty("prd")
	private String prd;
	@JsonProperty("prevprd")
	private String prevprd;
	@JsonProperty("trantype")
	private String trantype;
	@JsonProperty("postype")
	private String postype;
	@JsonProperty("ordersource")
	private String ordersource;
}
