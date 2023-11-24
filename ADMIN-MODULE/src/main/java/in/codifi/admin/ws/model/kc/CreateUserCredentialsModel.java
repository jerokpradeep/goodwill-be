package in.codifi.admin.ws.model.kc;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserCredentialsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String value;
	private Boolean temporary;
}