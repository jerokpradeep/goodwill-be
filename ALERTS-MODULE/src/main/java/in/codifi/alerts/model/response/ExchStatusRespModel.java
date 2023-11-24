package in.codifi.alerts.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchStatusRespModel {

	private String exchange;
	private String status;
	private String type;
}
