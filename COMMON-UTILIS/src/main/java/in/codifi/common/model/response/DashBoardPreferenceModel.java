package in.codifi.common.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashBoardPreferenceModel {

	private String key;
	private String show;
	private String isEnabled;
	private String sortOrder;
}
