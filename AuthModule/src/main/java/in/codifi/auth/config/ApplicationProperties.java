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

}
