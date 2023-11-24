package in.codifi.admin.ws.model.kc;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserInfoResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private String id;
	@JsonProperty("createdTimestamp")
	private Long createdTimestamp;
	@JsonProperty("username")
	private String username;
	@JsonProperty("enabled")
	private Boolean enabled;
	@JsonProperty("emailVerified")
	private Boolean emailVerified;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("email")
	private String email;
	@JsonProperty("attributes")
	private Attributes attributes;
	@JsonProperty("disableableCredentialTypes")
	private List<Object> disableableCredentialTypes;
	@JsonProperty("requiredActions")
	private List<Object> requiredActions;

}
