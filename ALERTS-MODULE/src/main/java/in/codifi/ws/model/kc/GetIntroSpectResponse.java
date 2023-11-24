package in.codifi.ws.model.kc;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetIntroSpectResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("exp")
	private Integer exp;
	@JsonProperty("iat")
	private Integer iat;
	@JsonProperty("jti")
	private String jti;
	@JsonProperty("iss")
	private String iss;
	@JsonProperty("aud")
	private String aud;
	@JsonProperty("sub")
	private String sub;
	@JsonProperty("typ")
	private String typ;
	@JsonProperty("azp")
	private String azp;
	@JsonProperty("session_state")
	private String sessionState;
	@JsonProperty("name")
	private String name;
	@JsonProperty("given_name")
	private String givenName;
	@JsonProperty("preferred_username")
	private String preferredUsername;
	@JsonProperty("email")
	private String email;
	@JsonProperty("email_verified")
	private Boolean emailVerified;
	@JsonProperty("acr")
	private String acr;
	@JsonProperty("allowed-origins")
	private List<String> allowedOrigins;
	@JsonProperty("realm_access")
	private RealmAccess realmAccess;
	@JsonProperty("resource_access")
	private ResourceAccess resourceAccess;
	@JsonProperty("scope")
	private String scope;
	@JsonProperty("sid")
	private String sid;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("username")
	private String username;
	@JsonProperty("active")
	private Boolean active;
	@JsonProperty("pan")
	private String pan;
	@JsonProperty("mobile")
	private String mobile;
	@JsonProperty("clientRole")
	private List<String> clientRoles;
}
