package in.codifi.holdings.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {

	@ConfigProperty(name = "appconfig.kambala.url.holdings")
	private String holdingsUrl;

	@ConfigProperty(name = "appconfig.kambala.url.edis")
	private String edisInitialize;

	@ConfigProperty(name = "appconfig.kambala.url.holdings.nonpoa")
	private String nonPoaHoldingsUrl;

	@ConfigProperty(name = "appconfig.kambala.url.holdings.gethstoken")
	private String hsTokenUrl;

//	@ConfigProperty(name = "appconfig.edis.redirecturl")
//	private String redirectUrl;

}