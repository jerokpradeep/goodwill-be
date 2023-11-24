package in.codifi.holdings.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EdisReqModel {

	private String settlementType;
	private String isin;
	private String qty;
}
