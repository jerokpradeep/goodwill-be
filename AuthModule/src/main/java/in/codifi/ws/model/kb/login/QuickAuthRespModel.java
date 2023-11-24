package in.codifi.ws.model.kb.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuickAuthRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("request_time")
	private String requestTime;

	@JsonProperty("actid")
	private String actId;

	@JsonProperty("uname")
	private String userName;

	@JsonProperty("prarr")
	private List<ProductArray> productArray = new ArrayList<>();

	@JsonProperty("stat")
	private String stat;

	@JsonProperty("susertoken")
	private String sUserToken;

	@JsonProperty("uid")
	private String uId;

	@JsonProperty("brnchid")
	private String brnchId;

	@JsonProperty("totp_set")
	private String totpSet;

	@JsonProperty("orarr")
	private List<String> orderArray = new ArrayList<>();

	@JsonProperty("exarr")
	private List<String> exchArray = new ArrayList<>();

	@JsonProperty("values")
	private List<String> values = new ArrayList<>();

	@JsonProperty("mws")
	private Mws mws;

	@JsonProperty("brkname")
	private String brkName;

	@JsonProperty("lastaccesstime")
	private String lastAccessTime;

	@JsonProperty("emsg")
	private String emsg;

}
