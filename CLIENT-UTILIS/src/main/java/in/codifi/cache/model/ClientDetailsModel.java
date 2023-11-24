package in.codifi.cache.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDetailsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String actId;
	private String clientName;
	private String actStatus;
	private String createdDate;
	private String createdTime;
	private String mobNo;
	private String email;
	private String pan;
	private String address;
	private String officeAddress;
	private String city;
	private String state;
	private List<String> exchange;
	private List<String> orders;
	private String branchId;
	private String brokerName;
	private List<Bankdetails> bankdetails;
	private List<String> products;
	private List<String> productTypes;
	private List<String> orderTypes;
	private List<String> priceTypes;

}
