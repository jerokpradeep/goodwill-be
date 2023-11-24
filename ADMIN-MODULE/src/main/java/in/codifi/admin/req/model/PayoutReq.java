package in.codifi.admin.req.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;
	private Date toDate;

}
