package in.codifi.funds.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
public class EmailProperties {

	@ConfigProperty(name = "appconfig.mail.recipient.ids")
	private String recipientIds;
}
