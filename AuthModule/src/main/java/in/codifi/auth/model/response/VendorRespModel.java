package in.codifi.auth.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorRespModel {

	private String redirectUrl;
	private boolean isAuthorized;
}
