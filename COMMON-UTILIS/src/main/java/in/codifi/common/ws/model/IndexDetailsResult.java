package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndexDetailsResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("nMarketSegmentId")
	private int nMarketSegmentId;
	@JsonProperty("nToken")
	private int token;
	@JsonProperty("sIndex")
	private String index;
	@JsonProperty("sIndexDesc")
	private String indexDesc;
	@JsonProperty("nIsDefaultIndex")
	private int isDefaultIndex;
	@JsonProperty("cIsIndex")
	private String isIndex;

}
