package in.codifi.ws.model.kb.login;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("exch")
	private String exch;
	@JsonProperty("token")
	private String token;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("instname")
	private String instname;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("optt")
	private String optt;
	@JsonProperty("weekly")
	private String weekly;

	public String getExch() {
		return exch;
	}

	public void setExch(String exch) {
		this.exch = exch;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTsym() {
		return tsym;
	}

	public void setTsym(String tsym) {
		this.tsym = tsym;
	}

	public String getInstname() {
		return instname;
	}

	public void setInstname(String instname) {
		this.instname = instname;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getLs() {
		return ls;
	}

	public void setLs(String ls) {
		this.ls = ls;
	}

	public String getTi() {
		return ti;
	}

	public void setTi(String ti) {
		this.ti = ti;
	}

	public String getOptt() {
		return optt;
	}

	public void setOptt(String optt) {
		this.optt = optt;
	}

	public String getWeekly() {
		return weekly;
	}

	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}

}
