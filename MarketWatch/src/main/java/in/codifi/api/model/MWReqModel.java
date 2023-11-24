package in.codifi.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MWReqModel {
	private String source;
	private boolean defaultMw;
	private boolean preDef;
	private boolean advFlag;
	private int mwId;
	private boolean lstsFlag;

}
