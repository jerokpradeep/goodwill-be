package in.codifi.api.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheMwAdvDetailsModel implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String mwName;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String userId;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public int mwId;
	public String segment;
	public String token;
	public String exchange;
	public String tradingSymbol;
	public Date expiry;
	public int sortOrder;
	public String pdc;
	public String symbol;
	public String formattedInsName;
	public String companyName;
	public String lotSize;
	public String tickSize;

}
