package in.codifi.admin.req.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogReqModel {
	private String fromDate;
	private String toDate;
	private String userId;
	private int pageNo;
	private int pageSize;

}
