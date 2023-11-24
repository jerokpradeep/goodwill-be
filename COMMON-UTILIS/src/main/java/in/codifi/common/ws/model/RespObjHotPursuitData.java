package in.codifi.common.ws.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespObjHotPursuitData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("type")
	private String type;
	@JsonProperty("recordcount")
	private String recordCount;
	@JsonProperty("resultset")
	private List<HotPursuitResultSet> resultSet;
	@JsonProperty("errorcode")
	private String errorCode;
	@JsonProperty("errormessage")
	private String errorMessage;
	@JsonProperty("errorobject")
	private String errorObject;

}
