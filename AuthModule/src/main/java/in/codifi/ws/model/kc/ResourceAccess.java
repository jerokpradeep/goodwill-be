package in.codifi.ws.model.kc;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceAccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("account")
	private ResoueceAccessAccount account;

//	//** nidhihq is an client name in keycloak **//
//	@JsonProperty("nidhihq")
//	private ResoueceAccessAccount clientRoles;

}
