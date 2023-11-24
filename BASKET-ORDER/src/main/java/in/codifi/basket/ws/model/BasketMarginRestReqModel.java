package in.codifi.basket.ws.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasketMarginRestReqModel {

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
	@JsonProperty("trgprc")
	private String trgprc;
	@JsonProperty("basketlists")
	private List<BasketListModel> basketlists;

}
