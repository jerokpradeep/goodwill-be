package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsetDIIModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("Category")
	private String category;
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("BuyValue")
	private String buyValue;
	@JsonProperty("SellValue")
	private String sellValue;
	@JsonProperty("NetValue")
	private String netValue;

}
