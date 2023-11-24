package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSymbolRespModel {
	private String exchSegment;
	private String groupName;
	private String symbol;
	private String instrumentName;
	private String token;

}
