package in.codifi.auth.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotpResponseModel {

	private boolean totpEnabled;
	private String scanImge;
	private String secKey;
}
