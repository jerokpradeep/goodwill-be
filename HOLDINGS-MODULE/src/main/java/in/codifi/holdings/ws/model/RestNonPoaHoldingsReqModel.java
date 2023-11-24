package in.codifi.holdings.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestNonPoaHoldingsReqModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uid;
	private String actid;
	private String prd;

}