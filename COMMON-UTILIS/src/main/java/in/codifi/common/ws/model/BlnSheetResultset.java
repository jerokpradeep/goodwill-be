package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlnSheetResultset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("ColumnName")
	private String columnName;
	@JsonProperty("CurrentYear")
	private String currentYear;
	@JsonProperty("Year1")
	private String year1;
	@JsonProperty("Year2")
	private String year2;

}
