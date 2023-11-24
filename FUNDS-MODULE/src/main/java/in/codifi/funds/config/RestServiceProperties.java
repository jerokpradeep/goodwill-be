package in.codifi.funds.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {

	@ConfigProperty(name = "appconfig.kambala.url.limits")
	private String limitsUrl;

	@ConfigProperty(name = "appconfig.backoff.login")
	private String boLoginUrl;

	@ConfigProperty(name = "appconfig.backoff.bank.details")
	private String boBankDetails;

	@ConfigProperty(name = "appconfig.url.backoffice.login")
	private String boPayInLogin;

	@ConfigProperty(name = "appconfig.url.backoffice.checkmargin")
	private String boPayoutCheckMargin;

	@ConfigProperty(name = "appconfig.url.backoffice.payout")
	private String boPayOut;

	@ConfigProperty(name = "appconfig.url.backoffice.payin")
	private String boPayIn;
	
	

}