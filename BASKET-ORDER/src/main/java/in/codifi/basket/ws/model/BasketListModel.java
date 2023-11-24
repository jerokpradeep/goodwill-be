package in.codifi.basket.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class BasketListModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	@JsonProperty("trgprc")
	private String trgprc;

}
