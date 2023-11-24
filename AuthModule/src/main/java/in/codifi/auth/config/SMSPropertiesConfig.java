package in.codifi.auth.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SMSPropertiesConfig {

	@ConfigProperty(name = "appconfig.sms.url")
	private String smsUrl;
	@ConfigProperty(name = "appconfig.sms.feedid")
	private String smsFeedId;
	@ConfigProperty(name = "appconfig.sms.senderid")
	private String smsSenderId;
	@ConfigProperty(name = "appconfig.sms.username")
	private String smsUserName;
	@ConfigProperty(name = "appconfig.sms.password")
	private String smsPassword;
	@ConfigProperty(name = "appconfig.sms.otp.minutes.interval")
	private int otpInterval;

	public Long getOtpIntervalInSeconds() {
		int interval = getOtpInterval();
		return Long.valueOf(interval * 60);
	}

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}

	public String getSmsFeedId() {
		return smsFeedId;
	}

	public void setSmsFeedId(String smsFeedId) {
		this.smsFeedId = smsFeedId;
	}

	public String getSmsSenderId() {
		return smsSenderId;
	}

	public void setSmsSenderId(String smsSenderId) {
		this.smsSenderId = smsSenderId;
	}

	public String getSmsUserName() {
		return smsUserName;
	}

	public void setSmsUserName(String smsUserName) {
		this.smsUserName = smsUserName;
	}

	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}

	public int getOtpInterval() {
		return otpInterval;
	}

	public void setOtpInterval(int otpInterval) {
		this.otpInterval = otpInterval;
	}

}
