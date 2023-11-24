package in.codifi.ws.model.kb.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuickAuthReqModel {

	@JsonProperty("uid")
	private String uId;
	@JsonProperty("imei")
	private String imei;
	@JsonProperty("appkey")
	private String appKey;
	@JsonProperty("apkversion")
	private String apkVersion;
	@JsonProperty("vc")
	private String vendorCode;
	@JsonProperty("source")
	private String source;
	@JsonProperty("ipaddr")
	private String ipAddress;

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getApkVersion() {
		return apkVersion;
	}

	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
