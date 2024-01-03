package in.codifi.auth.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class RestPropertiesConfig {

	@ConfigProperty(name = "appconfig.kambala.vendor-code-mob")
	private String mobileVendorCode;

	@ConfigProperty(name = "appconfig.kambala.vendor-code-web")
	private String webVendorCode;

	@ConfigProperty(name = "appconfig.kambala.vendor-code-api")
	private String apiVendorCode;

	@ConfigProperty(name = "appconfig.kambala.vendor-key-mob")
	private String mobileVendorKey;

	@ConfigProperty(name = "appconfig.kambala.vendor-key-web")
	private String webVendorKey;

	@ConfigProperty(name = "appconfig.kambala.vendor-key-api")
	private String apiVendorKey;

	@ConfigProperty(name = "appconfig.kambala.web.baseurl")
	private String kambalaWebBaseUrl;

	@ConfigProperty(name = "appconfig.kambala.mob.baseurl")
	private String kambalaMobBaseUrl;

	@ConfigProperty(name = "appconfig.kambala.api.baseurl")
	private String kambalaApiBaseUrl;

	@ConfigProperty(name = "appconfig.kambala.apk-version")
	private String kambalaApkVersion;

	@ConfigProperty(name = "appconfig.kambala.method.auth")
	private String kambalaMethodAuth;

	@ConfigProperty(name = "appconfig.kambala.method.userdetails")
	private String kambalaMethodUserDetails;

	@ConfigProperty(name = "appconfig.kambala.source")
	private String kambalaSource;

	@ConfigProperty(name = "appconfig.kambala.baseurl")
	private String kambalaBaseUrl;

	@ConfigProperty(name = "appconfig.kambala.forgotpwd")
	private String kambalaForgotPwd;

	@ConfigProperty(name = "appconfig.kambala.forgotpwdotp")
	private String kambalaForgotPwdOtp;

	@ConfigProperty(name = "appconfig.kambala.unblockusers")
	private String kambalaUnblockUsers;

	@ConfigProperty(name = "appconfig.kambala.changepwd")
	private String kambalaChangePwd;

	@ConfigProperty(name = "appconfig.kambala.logout")
	private String kambalaLogout;

	@ConfigProperty(name = "appconfig.kambala.weblogin")
	private String kambalaWebLoginUrl;

	@ConfigProperty(name = "appconfig.kambala.moblogin")
	private String kambalaMobLoginUrl;

}
