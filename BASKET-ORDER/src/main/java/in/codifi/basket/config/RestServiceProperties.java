package in.codifi.basket.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {

	@ConfigProperty(name = "appconfig.kambala.url.spanmargin")
	private String spanMarginUrl;
	@ConfigProperty(name = "appconfig.kambala.url.basketmargin")
	private String basketMarginUrl;

}