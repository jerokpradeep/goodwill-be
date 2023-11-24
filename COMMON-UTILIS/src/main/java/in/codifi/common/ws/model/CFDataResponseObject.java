package in.codifi.common.ws.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CFDataResponseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("type")
	private String type;
	@JsonProperty("recordcount")
	private Integer recordcount;
	@JsonProperty("resultset")
	private List<CFDataResultset> resultset;
	@JsonProperty("errorcode")
	private String errorCode;
	@JsonProperty("errormessage")
	private String errorMessage;
	@JsonProperty("errorobject")
	private String errorObject;

}