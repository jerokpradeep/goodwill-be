package in.codifi.admin.req.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractMasterReqModel {
	private String symbol;
	private String exch;
	private String expiry;
	private String group;

}
