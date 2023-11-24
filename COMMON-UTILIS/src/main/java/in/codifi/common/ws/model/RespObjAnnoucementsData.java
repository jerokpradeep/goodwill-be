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
public class RespObjAnnoucementsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("type")
	private String type;
	@JsonProperty("recordcount")
	private int recordcount;
	@JsonProperty("resultset")
	private List<AnnoucementsDataResultSet> resultSet;
	@JsonProperty("errorcode")
	private String errorCode;
	@JsonProperty("errormessage")
	private String errorMessage;
	@JsonProperty("errorobject")
	private String errorObject;

}
