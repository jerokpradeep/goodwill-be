package in.codifi.admin.config;

import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Singleton
@Getter
@Setter
public class KeyCloakConfig {

	@ConfigProperty(name = "auth.org.server.role.active.id")
	private String activeRoleId;

	@ConfigProperty(name = "auth.org.server.role.active.name")
	private String activeRoleName;

	@ConfigProperty(name = "auth.org.server.role.dormant.name")
	private String dormatRoleName;

	@ConfigProperty(name = "auth.org.server.role.dormant.id")
	private String dormantRoleId;

	@ConfigProperty(name = "auth.org.server.cleint.chola.id")
	private String clientCholaId;

	@ConfigProperty(name = "quarkus.oidc.auth-server-url")
	private String authServerUrl;

	@ConfigProperty(name = "quarkus.oidc.client-id")
	private String clientId;

	@ConfigProperty(name = "quarkus.oidc.credentials.secret")
	private String clientSecret;

	@ConfigProperty(name = "auth.org.grant.type")
	private String grantType;

	@ConfigProperty(name = "auth.org.server.client-id")
	private String adminClientId;

	@ConfigProperty(name = "auth.org.server.client-secret")
	private String adminSecret;

	@ConfigProperty(name = "auth.org.server.grant-type")
	private String adminGrantType;

}
