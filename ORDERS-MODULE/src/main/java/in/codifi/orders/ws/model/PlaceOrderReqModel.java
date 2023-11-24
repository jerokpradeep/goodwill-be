package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceOrderReqModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	@JsonProperty("prc")
	private String prc;
	@JsonProperty("trgprc")
	private String trgprc;
	@JsonProperty("dscqty")
	private String dscqty;
	@JsonProperty("prd")
	private String prd;
	@JsonProperty("trantype")
	private String trantype;
	@JsonProperty("prctyp")
	private String prctyp;
	@JsonProperty("ret")
	private String ret;
	@JsonProperty("mkt_protection")
	private String mktProtection;
	@JsonProperty("remarks")
	private String remarks;
	@JsonProperty("ordersource")
	private String ordersource;
	@JsonProperty("bpprc")
	private String bpprc;
	@JsonProperty("blprc")
	private String blprc;
	@JsonProperty("trailprc")
	private String trailprc;
	@JsonProperty("amo")
	private String amo;
	@JsonProperty("tsym2")
	private String tsym2;
	@JsonProperty("trantype2")
	private String trantype2;
	@JsonProperty("qty2")
	private String qty2;
	@JsonProperty("prc2")
	private String prc2;
	@JsonProperty("tsym3")
	private String tsym3;
	@JsonProperty("trantype3")
	private String trantype3;
	@JsonProperty("qty3")
	private String qty3;
	@JsonProperty("prc3")
	private String prc3;
	@JsonProperty("algo_id")
	private String algoId;
	@JsonProperty("naic_code")
	private String naicCode;

}