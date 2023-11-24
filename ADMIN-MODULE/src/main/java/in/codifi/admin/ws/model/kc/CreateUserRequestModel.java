package in.codifi.admin.ws.model.kc;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequestModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("username")
	private String username;

	@JsonProperty("email")
	private String email;

	@JsonProperty("enabled")
	private Boolean enabled;

	@JsonProperty("emailVerified")
	private Boolean emailVerified;

	@JsonProperty("credentials")
	private List<CreateUserCredentialsModel> credentials;

	@JsonProperty("attributes")
	private UserAttribute attributes;

//	@JsonProperty("clientRoles")
//	public ClientRoles clientRoles;

//	@JsonProperty("resource_access")
//	private ResourceAccess resourceAccess;

}
