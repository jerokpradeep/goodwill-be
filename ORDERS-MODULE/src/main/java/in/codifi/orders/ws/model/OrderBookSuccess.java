package in.codifi.orders.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookSuccess {

	@JsonProperty("stat")
	private String stat;
	@JsonProperty("norenordno")
	private String norenordno;
	@JsonProperty("kidid")
	private String kidid;
	@JsonProperty("uid")
	private String uid;
	@JsonProperty("actid")
	private String actid;
	@JsonProperty("exch")
	private String exch;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("qty")
	private String qty;
	@JsonProperty("ordenttm")
	private String ordenttm;
	@JsonProperty("trantype")
	private String trantype;
	@JsonProperty("prctyp")
	private String prctyp;
	@JsonProperty("ret")
	private String ret;
	@JsonProperty("token")
	private String token;
	@JsonProperty("mult")
	private String mult;
	@JsonProperty("prcftr")
	private String prcftr;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("prc")
	private String prc;
	@JsonProperty("rprc")
	private String rprc;
	@JsonProperty("avgprc")
	private String avgprc;
	@JsonProperty("dscqty")
	private String dscqty;
	@JsonProperty("prd")
	private String prd;
	@JsonProperty("status")
	private String status;
	@JsonProperty("st_intrn")
	private String stIntrn;
	@JsonProperty("fillshares")
	private String fillshares;
	@JsonProperty("norentm")
	private String norentm;
	@JsonProperty("exch_tm")
	private String exchTm;
	@JsonProperty("exchordid")
	private String exchordid;
	@JsonProperty("rqty")
	private String rqty;
	@JsonProperty("rejreason")
	private String rejreason;
	@JsonProperty("mkt_protection")
	private String mktProtection;
	@JsonProperty("cancelqty")
	private String cancelQty;
	@JsonProperty("remarks")
	private String remarks;
	@JsonProperty("trgprc")
	private String trgPrc;
	@JsonProperty("bpprc")
	private String bpPrc;
	@JsonProperty("blprc")
	private String blPrc;
	@JsonProperty("trailprc")
	private String trailPrc;
	@JsonProperty("amo")
	private String amo;
	@JsonProperty("dname")
	private String dname;
	@JsonProperty("rtrgprc")
	private String rTrgPrc;
	@JsonProperty("rblprc")
	private String rBlPrc;
	@JsonProperty("rorgqty")
	private String rorgQty;
	@JsonProperty("rorgprc")
	private String rorgPrc;
	@JsonProperty("orgtrgprc")
	private String orgTrgPrc;
	@JsonProperty("orgblprc")
	private String orgBlPrc;
	@JsonProperty("sno_fillid")
	private String snoFillid;
	@JsonProperty("snonum")
	private String snoNum;

}