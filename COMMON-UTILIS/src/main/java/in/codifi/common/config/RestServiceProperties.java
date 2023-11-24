package in.codifi.common.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {

	@ConfigProperty(name = "config.analysis.service.baseurl")
	private String analysisBaseUrl;

	@ConfigProperty(name = "config.analysis.service.url.topgainers")
	private String topGainersUrl;

	@ConfigProperty(name = "config.analysis.service.url.fiftytwoweekhigh")
	private String fiftyTwoWeekHigh;

	@ConfigProperty(name = "config.analysis.service.url.fiftyweeklow")
	private String fiftyTwoWeekLow;

}
