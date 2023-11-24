package in.codifi.client.transformation;

import java.util.List;

import in.codifi.cache.model.Bankdetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDetailsRespModel {

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
	private List<Object> mandateIdList;
	private List<String> exchange;
	private List<Bankdetails> bankdetails;
	private List<DpAccountNum> dpAccountNumber;
	private List<String> orders;
	private String branchId;
	private String brokerName;
	private List<String> products;
	private List<String> productTypes;
	private List<String> orderTypes;
	private List<String> priceTypes;

}
