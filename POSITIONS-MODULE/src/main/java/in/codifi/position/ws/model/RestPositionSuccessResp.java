package in.codifi.position.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPositionSuccessResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("uid")
	private String uid;
	@JsonProperty("actid")
	private String actid;
	@JsonProperty("exch")
	private String exch;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("prd")
	private String prd;
	@JsonProperty("token")
	private String token;
	@JsonProperty("frzqty")
	private String frzqty;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("mult")
	private String mult;
	@JsonProperty("prcftr")
	private String prcftr;
	@JsonProperty("daybuyqty")
	private String daybuyqty;
	@JsonProperty("daysellqty")
	private String daysellqty;
	@JsonProperty("daybuyamt")
	private String daybuyamt;
	@JsonProperty("daybuyavgprc")
	private String daybuyavgprc;
	@JsonProperty("daysellamt")
	private String daysellamt;
	@JsonProperty("daysellavgprc")
	private String daysellavgprc;
	@JsonProperty("cfbuyqty")
	private String cfbuyqty;
	@JsonProperty("cfsellqty")
	private String cfsellqty;
	@JsonProperty("cfbuyamt")
	private String cfbuyamt;
	@JsonProperty("cfsellamt")
	private String cfsellamt;
	@JsonProperty("cfbuyavgprc")
	private String cfbuyavgprc;
	@JsonProperty("cfsellavgprc")
	private String cfsellavgprc;
	@JsonProperty("openbuyqty")
	private String openbuyqty;
	@JsonProperty("opensellqty")
	private String opensellqty;
	@JsonProperty("openbuyamt")
	private String openbuyamt;
	@JsonProperty("openbuyavgprc")
	private String openbuyavgprc;
	@JsonProperty("opensellamt")
	private String opensellamt;
	@JsonProperty("opensellavgprc")
	private String opensellavgprc;
	@JsonProperty("dayavgprc")
	private String dayavgprc;
	@JsonProperty("netqty")
	private String netqty;
	@JsonProperty("netavgprc")
	private String netavgprc;
	@JsonProperty("upldprc")
	private String upldprc;
	@JsonProperty("netupldprc")
	private String netupldprc;
	@JsonProperty("lp")
	private String lp;
	@JsonProperty("urmtom")
	private String urmtom;
	@JsonProperty("bep")
	private String bep;
	@JsonProperty("totbuyamt")
	private String totbuyamt;
	@JsonProperty("totsellamt")
	private String totsellamt;
	@JsonProperty("totbuyavgprc")
	private String totbuyavgprc;
	@JsonProperty("totsellavgprc")
	private String totsellavgprc;
	@JsonProperty("rpnl")
	private String rpnl;

}