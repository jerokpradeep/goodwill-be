package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistorySuccessModel implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonPropertyOrder({ "stat", "norenordno", "uid", "actid", "exch", "tsym", "qty", "trantype", "prctyp", "ret",
			"token", "pp", "ls", "ti", "prc", "avgprc", "dscqty", "prd", "status", "rpt", "fillshares", "norentm",
			"exch_tm", "remarks", "exchordid" })

	@JsonProperty("stat")
	public String stat;
	@JsonProperty("norenordno")
	public String norenordno;
	@JsonProperty("uid")
	public String uid;
	@JsonProperty("actid")
	public String actid;
	@JsonProperty("exch")
	public String exch;
	@JsonProperty("tsym")
	public String tsym;
	@JsonProperty("qty")
	public String qty;
	@JsonProperty("trantype")
	public String trantype;
	@JsonProperty("prctyp")
	public String prctyp;
	@JsonProperty("ret")
	public String ret;
	@JsonProperty("token")
	public String token;
	@JsonProperty("pp")
	public String pp;
	@JsonProperty("ls")
	public String ls;
	@JsonProperty("ti")
	public String ti;
	@JsonProperty("prc")
	public String prc;
	@JsonProperty("avgprc")
	public String avgprc;
	@JsonProperty("dscqty")
	public String dscqty;
	@JsonProperty("prd")
	public String prd;
	@JsonProperty("status")
	public String status;
	@JsonProperty("rpt")
	public String rpt;
	@JsonProperty("fillshares")
	public String fillshares;
	@JsonProperty("norentm")
	public String norentm;
	@JsonProperty("exch_tm")
	public String exchTm;
	@JsonProperty("remarks")
	public String remarks;
	@JsonProperty("exchordid")
	public String exchordid;
	@JsonProperty("amo")
	public String amo;

}