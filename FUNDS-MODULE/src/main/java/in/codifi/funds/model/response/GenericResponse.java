package in.codifi.funds.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private String message;
	private Object result;
}
