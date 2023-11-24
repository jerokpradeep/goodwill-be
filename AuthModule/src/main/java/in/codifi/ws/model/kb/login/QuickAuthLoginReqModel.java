package in.codifi.ws.model.kb.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuickAuthLoginReqModel {
	@JsonProperty("apkversion")
	private String apkVersion;
	@JsonProperty("uid")
	private String uId;
	@JsonProperty("pwd")
	private String pwd;
	@JsonProperty("factor2")
	private String factor2;
	@JsonProperty("vc")
	private String vendorCode;
	@JsonProperty("appkey")
	private String appKey;
	@JsonProperty("imei")
	private String imei;
	@JsonProperty("source")
	private String source;

}
