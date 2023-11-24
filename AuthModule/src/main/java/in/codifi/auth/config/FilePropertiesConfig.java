package in.codifi.auth.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class FilePropertiesConfig {

	@ConfigProperty(name = "appconfig.file.path.qrcode")
	private String qrCodePath;

}
