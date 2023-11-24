package in.codifi.cache.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdisRecordModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("TxnReqId")
	private String txnReqId;

	@JsonProperty("RevocReqTxnId")
	private String revocReqTxnId;

	@JsonProperty("ResId")
	private String resId;

	@JsonProperty("TxnId")
	private String txnId;

	@JsonProperty("Quantity")
	private String quantity;

	@JsonProperty("ISIN")
	private String isin;

	@JsonProperty("SettleId")
	private String settleId;

	@JsonProperty("ExecDate")
	private String execDate;

	@JsonProperty("Filler1")
	private String filler1;

}
