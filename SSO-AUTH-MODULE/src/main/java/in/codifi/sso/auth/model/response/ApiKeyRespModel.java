package in.codifi.sso.auth.model.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiKeyRespModel {

	private String apiKey;
	private Date expiryDate;
	private String message;
	private boolean expired;
	private boolean available;

}
