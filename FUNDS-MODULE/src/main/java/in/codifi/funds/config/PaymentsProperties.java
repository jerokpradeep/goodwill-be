package in.codifi.funds.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class PaymentsProperties {

	@ConfigProperty(name = "appconfig.razorpay.key")
	private String razorpayKey;
	@ConfigProperty(name = "appconfig.razorpay.secret")
	private String razorpaySecret;
	@ConfigProperty(name = "appconfig.razorpay.ifsc.url")
	private String razorpayIfscUrl;

	@ConfigProperty(name = "appconfig.kambala.url.payment.gethstoken")
	private String hsTokenUrl;

}
