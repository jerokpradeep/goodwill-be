package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class HealthResultset {

	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("TotalScore")
	private String totalScore;
	@JsonProperty("HealthStatus")
	private String healthStatus;

}
