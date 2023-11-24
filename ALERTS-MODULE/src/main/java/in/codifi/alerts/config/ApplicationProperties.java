package in.codifi.alerts.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class ApplicationProperties {

	@ConfigProperty(name = "config.app.push.fcm.baseurl")
	private String fcmBaseUrl;

	@ConfigProperty(name = "config.app.push.fcm.apikey")
	private String fcmApiKey;

	@ConfigProperty(name = "config.app.alert.baseurl")
	private String alertBaseUrl;

	@ConfigProperty(name = "config.app.alert.vendorname")
	private String alertVendorName;

	@ConfigProperty(name = "config.kambala.url.exchmsg")
	private String exchMsgUrl;

	@ConfigProperty(name = "config.kambala.url.brokermsg")
	private String brokerageMsgUrl;

	@ConfigProperty(name = "config.kambala.url.exchstatus")
	private String exchStatusUrl;

}
