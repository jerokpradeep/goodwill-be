package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovingAverageResultSet  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("avg_15days")
	private String avgFifteenDays;
	@JsonProperty("avg_20days")
	private String avgTwentyDays;
	@JsonProperty("avg_30days")
	private String avgThirtyDays;
	@JsonProperty("avg_50days")
	private String avgFiftyDays;
	@JsonProperty("avg_150days")
	private String avgHunFiftydays;
	@JsonProperty("avg_100days")
	private String avgHundredDays;
	@JsonProperty("avg_200days")
	private String avgTwoHundredDays;

}
