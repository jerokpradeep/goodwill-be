package in.codifi.holdings.ws.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Success {

	@JsonProperty("stat")
	private String stat;
	@JsonProperty("exch_tsym")
	private List<ExchTsym> exchTsym = new ArrayList<>();
	@JsonProperty("holdqty")
	private String holdqty;
	@JsonProperty("dpQty")
	private String dpQty;
	@JsonProperty("npoadqty")
	private String npoadqty;
	@JsonProperty("colqty")
	private String colqty;
	@JsonProperty("benQty")
	private String benQty;
	@JsonProperty("unplgdqty")
	private String unplgdQty;
	@JsonProperty("brkcolqty")
	private String brkColQty;
	@JsonProperty("btstqty")
	private String btstqty;
	@JsonProperty("btstcolqty")
	private String btstcolqty;
	@JsonProperty("usedqty")
	private String usedqty;
	@JsonProperty("upldprc")
	private String upldprc;
	@JsonProperty("hair_cut")
	private String haircut;
	@JsonProperty("sell_amt")
	private String sellAmt;
	@JsonProperty("trdqty")
	private String trdqty;

}