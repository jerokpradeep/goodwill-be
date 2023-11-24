package in.codifi.admin.ws.model.kc;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("ucc")
	private String ucc;

	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("pan")
	private String pan;

	@JsonProperty("maritalStatus")
	private String maritalStatus;

	@JsonProperty("gender")
	private String gender;

}
