package in.codifi.scrips.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestProperties {
	@ConfigProperty(name = "config.kambala.url.securityinfo")
	private String getSecurityInfo;
}
