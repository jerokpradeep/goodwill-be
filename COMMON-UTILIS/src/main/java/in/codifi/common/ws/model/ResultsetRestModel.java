package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultsetRestModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("SNo")
	private String sNo;
	@JsonProperty("ColumnName")
	private String columnName;
	@JsonProperty("CurrentFinYr")
	private String currentFinYr;
	@JsonProperty("YOY_1")
	private String yoy1;
	@JsonProperty("YOY_2")
	private String yoy2;
	@JsonProperty("YOY_1_PER")
	private String yoy1Per;
	@JsonProperty("YOY_2_PER")
	private String yoy2Per;

}
