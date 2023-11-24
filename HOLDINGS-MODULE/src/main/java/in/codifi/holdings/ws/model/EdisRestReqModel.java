package in.codifi.holdings.ws.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdisRestReqModel {

	private String uid;
	private String actid;
	private String settle_t;
	private List<PartsRestReqModel> parts;

	@Getter
	@Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public class PartsRestReqModel {
		private String isin;
		private String qty;
	}

}
