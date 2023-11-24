package in.codifi.position.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {

	@ConfigProperty(name = "appconfig.kambala.url.position")
	private String positionUrl;
	@ConfigProperty(name = "appconfig.kambala.url.positionconversion")
	private String conversionUrl;

}
