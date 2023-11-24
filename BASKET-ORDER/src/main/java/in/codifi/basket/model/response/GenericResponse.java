package in.codifi.basket.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse {

	private String status;
	private String message;
	private Object result;

}
