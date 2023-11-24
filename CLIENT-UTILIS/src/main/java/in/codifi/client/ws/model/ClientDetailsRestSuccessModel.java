package in.codifi.client.ws.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDetailsRestSuccessModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("stat")
	public String stat;
	@JsonProperty("emsg")
	public String emsg;
	@JsonProperty("request_time")
	public String requestTime;
	@JsonProperty("actid")
	public String actid;
	@JsonProperty("cliname")
	public String cliname;
	@JsonProperty("act_sts")
	public String actSts;
	@JsonProperty("creatdte")
	public String creatdte;
	@JsonProperty("creattme")
	public String creattme;
	@JsonProperty("m_num")
	public String mNum;
	@JsonProperty("email")
	public String email;
	@JsonProperty("pan")
	public String pan;
	@JsonProperty("addr")
	public String addr;
	@JsonProperty("addroffice")
	public String addroffice;
	@JsonProperty("addrcity")
	public String addrcity;
	@JsonProperty("addrstate")
	public String addrstate;
	@JsonProperty("mandate_id_list")
	public List<Object> mandateIdList;
	@JsonProperty("exarr")
	public List<String> exarr;
	@JsonProperty("bankdetails")
	public List<Bankdetail> bankdetails;
	@JsonProperty("dp_acct_num")
	public List<DpAcctNum> dpAcctNum;

}
