package in.codifi.api.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
public class ApplicationProperties {
	@ConfigProperty(name = "config.analysis.service.url.topgainers")
	private String topGainers;

	@ConfigProperty(name = "config.analysis.service.url.fiftytwoweekhigh")
	private String fiftytwoWeekHigh;

	@ConfigProperty(name = "config.analysis.service.url.fiftytwoweeklow")
	private String fiftytwoWeekLow;

}
