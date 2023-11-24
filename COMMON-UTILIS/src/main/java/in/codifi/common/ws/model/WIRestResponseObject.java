package in.codifi.common.ws.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WIRestResponseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("type")
	public String type;
	@JsonProperty("recordcount")
	public int recordcount;
	@JsonProperty("resultset")
	public List<WIRestResultset> resultset;
	@JsonProperty("errorcode")
	public String errorCode;
	@JsonProperty("errormessage")
	public String errorMessage;
	@JsonProperty("errorobject")
	public String errorObject;

}
