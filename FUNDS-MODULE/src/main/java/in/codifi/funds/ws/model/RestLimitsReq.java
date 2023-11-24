package in.codifi.funds.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestLimitsReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uid;
	private String actid;
	private String seg;

}
