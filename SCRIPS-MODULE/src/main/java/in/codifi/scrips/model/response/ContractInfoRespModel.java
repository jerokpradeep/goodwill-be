package in.codifi.scrips.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractInfoRespModel {

	private String isin;
	private String freezeQty;
	private List<ContractInfoDetails> scrips;

}
