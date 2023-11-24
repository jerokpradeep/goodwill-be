package in.codifi.orders.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestServiceProperties {

	@ConfigProperty(name = "appconfig.kambala.url.placeorder")
	private String placeOrderUrl;

	@ConfigProperty(name = "appconfig.kambala.url.modifyorder")
	private String modifyOrderurl;

	@ConfigProperty(name = "appconfig.kambala.url.cancelorder")
	private String cancelOrderUrl;

	@ConfigProperty(name = "appconfig.kambala.url.ordermargin")
	private String orderMarginUrl;

	@ConfigProperty(name = "appconfig.kambala.url.orderbook")
	private String orderBookUrl;

	@ConfigProperty(name = "appconfig.kambala.url.tradebook")
	private String tradeBookUrl;

	@ConfigProperty(name = "appconfig.kambala.url.orderhistory")
	private String orderHistoryUrl;

	@ConfigProperty(name = "appconfig.kambala.url.exitsno")
	private String exitSnoOrderUrl;
}