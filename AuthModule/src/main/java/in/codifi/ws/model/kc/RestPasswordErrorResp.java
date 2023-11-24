package in.codifi.ws.model.kc;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPasswordErrorResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("error")
	private String error;
	@JsonProperty("error_description")
	private String errorDescription;

}
