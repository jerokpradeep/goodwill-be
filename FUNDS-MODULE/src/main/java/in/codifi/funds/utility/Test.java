package in.codifi.funds.utility;

import org.json.simple.JSONArray;

import in.codifi.funds.model.request.PaymentReqModel;
import in.codifi.funds.ws.service.BackOfficeRestService;

public class Test {
	public static void main(String[] args) {
		BackOfficeRestService backOfficeRestService = new BackOfficeRestService();
		PaymentReqModel model = new PaymentReqModel();
		model.setAmount(1);
		model.setBankActNo("123456789");
		model.setIfscCode("ifscCOde");
		model.setSegment("MCX");
		model.setPayoutReason("NA");
		JSONArray boPayOutResponse = (JSONArray) backOfficeRestService.loginBackOfficeBopayOut("1234",
				model.getBankActNo(), model.getIfscCode(), model.getSegment(), model.getAmount(),
				model.getPayoutReason(), 0);
	}
}
