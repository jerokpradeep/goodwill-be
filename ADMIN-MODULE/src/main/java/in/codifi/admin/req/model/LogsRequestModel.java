package in.codifi.admin.req.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogsRequestModel {

	private String userId;

	private String uri;

	private String fromDate;

	private String toDate;

	private int pageNo;

	private int pageSize;

}
