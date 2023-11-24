package in.codifi.auth.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonErrorResponse {
	private String stat;
	private String emsg;
}
