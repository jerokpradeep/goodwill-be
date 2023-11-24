package in.codifi.holdings.ws.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonPoaHoldingsSuccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("stat")
	public String stat;
	@JsonProperty("exch_tsym")
	public List<ExchTsymList> exchTsym;
	@JsonProperty("totqty")
	public String totqty;
	@JsonProperty("approvedqty")
	public String approvedqty;
	@JsonProperty("upldprc")
	public String upldprc;
	@JsonProperty("pan")
	public String pan;
	@JsonProperty("BOID")
	public String boid;
	@JsonProperty("t1qty")
	public String t1qty;
	@JsonProperty("actid")
	public String actid;
	@JsonProperty("isin")
	public String isin;
	@JsonProperty("settle_t")
	public String settlementType;
	@JsonProperty("prd")
	public String prd;

}
