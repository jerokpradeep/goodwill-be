package in.codifi.common.ws.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObjectBlnSheetRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("type")
	private String type;
	@JsonProperty("recordcount")
	private Integer recordcount;
	@JsonProperty("resultset")
	private List<BlnSheetResultset> resultset;
	@JsonProperty("errorcode")
	private String errorcode;
	@JsonProperty("errormessage")
	private String errormessage;
	@JsonProperty("errorobject")
	private String errorobject;
}
