package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyOrderReqModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("uid")
	private String uid;
	@JsonProperty("exch")
	private String exch;
	@JsonProperty("norenordno")
	private String norenordno;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("prctyp")
	private String prctyp;
	@JsonProperty("prc")
	private String prc;
	@JsonProperty("qty")
	private String qty;
	@JsonProperty("ret")
	private String ret;
	@JsonProperty("mkt_protection")
	private String mktProtection;
	@JsonProperty("trgprc")
	private String trgprc;
	@JsonProperty("dscqty")
	private String dscqty;
	@JsonProperty("bpprc")
	private String bpprc;
	@JsonProperty("blprc")
	private String blprc;
	@JsonProperty("trailprc")
	private String trailprc;
}