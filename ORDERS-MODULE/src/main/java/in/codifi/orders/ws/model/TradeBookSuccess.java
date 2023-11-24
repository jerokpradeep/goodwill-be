package in.codifi.orders.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeBookSuccess {

	@JsonProperty("stat")
	private String stat;
	@JsonProperty("norenordno")
	private String norenordno;
	@JsonProperty("uid")
	private String uid;
	@JsonProperty("actid")
	private String actid;
	@JsonProperty("exch")
	private String exch;
	@JsonProperty("prctyp")
	private String prctyp;
	@JsonProperty("ret")
	private String ret;
	@JsonProperty("prd")
	private String prd;
	@JsonProperty("flid")
	private String flid;
	@JsonProperty("fltm")
	private String fltm;
	@JsonProperty("trantype")
	private String trantype;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("qty")
	private String qty;
	@JsonProperty("token")
	private String token;
	@JsonProperty("fillshares")
	private String fillshares;
	@JsonProperty("flqty")
	private String flqty;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("prc")
	private String prc;
	@JsonProperty("prcftr")
	private String prcftr;
	@JsonProperty("flprc")
	private String flprc;
	@JsonProperty("norentm")
	private String norentm;
	@JsonProperty("exch_tm")
	private String exchTm;
	@JsonProperty("exchordid")
	private String exchordid;
}