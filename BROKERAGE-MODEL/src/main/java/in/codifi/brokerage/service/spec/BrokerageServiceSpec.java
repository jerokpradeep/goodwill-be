package in.codifi.brokerage.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.brokerage.model.request.BrokerageReqModel;
import in.codifi.brokerage.model.response.GenericResponse;

public interface BrokerageServiceSpec {

	/**
	 * Method to
	 *
	 * @author Admin
	 *
	 * @return
	 */
	RestResponse<GenericResponse> brokerageCalculation(BrokerageReqModel brokerageReqModel);

}
