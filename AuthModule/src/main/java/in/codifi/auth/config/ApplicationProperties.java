package in.codifi.auth.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class ApplicationProperties {

	@ConfigProperty(name = "appconfig.kc.appkey")
	private String appKey;
	@ConfigProperty(name = "appconfig.kc.vendorcode")
	private String vendorCode;
	
	@ConfigProperty(name = "appconfig.kc.mob.appkey")
	private String mobAppKey;
	@ConfigProperty(name = "appconfig.kc.mob.vendorcode")
	private String mobVendorCode;
	
	@ConfigProperty(name = "appconfig.kc.web.appkey")
	private String webAppKey;
	@ConfigProperty(name = "appconfig.kc.web.vendorcode")
	private String webVendorCode;

}
