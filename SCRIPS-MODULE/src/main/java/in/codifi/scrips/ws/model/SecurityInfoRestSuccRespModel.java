package in.codifi.scrips.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityInfoRestSuccRespModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("request_time")
	private String request_time;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("emsg")
	private String emsg;
	@JsonProperty("exch")
	private String exch;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("cname")
	private String cname;
	@JsonProperty("symname")
	private String symname;
	@JsonProperty("seg")
	private String seg;
	@JsonProperty("instname")
	private String instname;
	@JsonProperty("isin")
	private String isin;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("mult")
	private String mult;
	@JsonProperty("prcftr_d")
	private String prcftr_d;
	@JsonProperty("trdunt")
	private String trdunt;
	@JsonProperty("delunt")
	private String delunt;
	@JsonProperty("token")
	private String token;
	@JsonProperty("varmrg")
	private String varmrg;
	@JsonProperty("exd")
	private String exd;

	@JsonProperty("strprc")
	private String strprc;
	@JsonProperty("optt")
	private String optt;
	@JsonProperty("gp_nd")
	private String gp_nd;
	@JsonProperty("prcunt")
	private String prcunt;
	@JsonProperty("prcqqty")
	private String prcqqty;
	@JsonProperty("frzqty")
	private String frzqty;
	@JsonProperty("gsmind")
	private String gsmind;
	@JsonProperty("elmbmrg")
	private String elmbmrg;
	@JsonProperty("elmsmrg")
	private String elmsmrg;
	@JsonProperty("addbmrg")
	private String addbmrg;
	@JsonProperty("addsmrg")
	private String addsmrg;
	@JsonProperty("splbmrg")
	private String splbmrg;
	@JsonProperty("splsmrg")
	private String splsmrg;
	@JsonProperty("delmrg")
	private String delmrg;
	@JsonProperty("tenmrg")
	private String tenmrg;
	@JsonProperty("tenstrd")
	private String tenstrd;
	@JsonProperty("tenendd")
	private String tenendd;
	@JsonProperty("exestrd")
	private String exestrd;
	@JsonProperty("exeendd")
	private String exeendd;

	@JsonProperty("mkt_t")
	private String mkt_t;
	@JsonProperty("issue_d")
	private String issue_d;
	@JsonProperty("listing_d")
	private String listing_d;
	@JsonProperty("last_trd_d")
	private String last_trd_d;
	@JsonProperty("elmmrg")
	private String elmmrg;
	@JsonProperty("expmrg")
	private String expmrg;
	@JsonProperty("weekly")
	private String weekly;
	@JsonProperty("nontrd")
	private String nontrd;
	@JsonProperty("dname")
	private String dname;

}
