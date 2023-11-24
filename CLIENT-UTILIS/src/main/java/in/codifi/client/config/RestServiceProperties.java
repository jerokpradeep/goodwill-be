package in.codifi.client.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {
	
	@ConfigProperty(name = "config.kambala.url.clientdetails")
	private String clientDetails;

	@ConfigProperty(name = "config.kambala.url.createsession")
	private String wsCreateSession;

	@ConfigProperty(name = "config.kambala.url.invalidatesession")
	private String wsInvalidateSession;

}
