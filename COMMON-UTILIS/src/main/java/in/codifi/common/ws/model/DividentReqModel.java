package in.codifi.common.ws.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DividentReqModel {
	private String exchSeg;
	private Date fromDate;
	private Date toDate;
	private int recordSize;
	private int token;

}
