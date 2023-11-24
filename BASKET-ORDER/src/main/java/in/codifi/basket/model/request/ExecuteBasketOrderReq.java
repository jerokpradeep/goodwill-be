package in.codifi.basket.model.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecuteBasketOrderReq {

	private String basketName;
	private int basketId;
	private List<ScripRequestModel> scrips;
}
