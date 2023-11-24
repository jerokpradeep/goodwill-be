package in.codifi.admin.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
public class ApplicationProperties {

	@ConfigProperty(name = "appconfig.client.file.path")
	private String clientFilePath;
	@ConfigProperty(name = "appconfig.client.file.completedpath")
	private String clientCompletedPath;
	@ConfigProperty(name = "appconfig.client.file.resultpath")
	private String clientResultPath;
	
	//Push notification
	@ConfigProperty(name = "appconfig.push.fcmbaseurl")
	private String fcmBaseUrl;
	@ConfigProperty(name = "appconfig.push.fcm.apikey")
	private String fcmApiKey;
	
	

	@ConfigProperty(name = "appconfig.position.file.path")
	private String positionFilePath;
	@ConfigProperty(name = "appconfig.position.file.completedpath")
	private String positionCompletedPath;
	@ConfigProperty(name = "appconfig.holdings.file.path")
	private String holdingsFilePath;
	@ConfigProperty(name = "appconfig.holdings.file.completedpath")
	private String holdingsCompletedPath;

}
