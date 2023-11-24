package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMarginReqModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Mandatory fields **/
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
	@JsonProperty("prd")
	private String prd;
	@JsonProperty("trantype")
	private String trantype;
	@JsonProperty("prctyp")
	private String prctyp;

	/** Not Mandatory fields **/
	@JsonProperty("trgprc")
	private String trgprc;
	@JsonProperty("blprc")
	private String blprc;
	@JsonProperty("rorgqty")
	private String rorgqty;
	@JsonProperty("fillshares")
	private String fillshares;
	@JsonProperty("rorgprc")
	private String rorgprc;
	@JsonProperty("orgtrgprc")
	private String orgtrgprc;
	@JsonProperty("norenordno")
	private String norenordno;
	@JsonProperty("snonum")
	private String snonum;

}