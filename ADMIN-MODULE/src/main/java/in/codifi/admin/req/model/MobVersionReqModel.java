package in.codifi.admin.req.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobVersionReqModel {

	private int id;
	private String version;
	private String type;
	private String os;
	private int updateAvailable;
	private String createdOn;
}
