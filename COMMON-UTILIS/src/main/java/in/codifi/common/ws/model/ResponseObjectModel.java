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
public class ResponseObjectModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("type")
	private String type;
	@JsonProperty("recordcount")
	private Integer recordcount;
	@JsonProperty("resultset")
	private List<ResultsetModel> resultset;
	@JsonProperty("errorcode")
	private String errorcode;
	@JsonProperty("errormessage")
	private String errormessage;
	@JsonProperty("errorobject")
	private String errorobject;

}
