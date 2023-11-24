package in.codifi.brokerage.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.brokerage.controller.spec.BrokerageControllerSpec;
import in.codifi.brokerage.model.request.BrokerageReqModel;
import in.codifi.brokerage.model.response.GenericResponse;
import in.codifi.brokerage.service.spec.BrokerageServiceSpec;

@Path("/brokerage")
public class BrokerageController implements BrokerageControllerSpec {

	@Inject
	BrokerageServiceSpec brokerageService;
	
	public RestResponse<GenericResponse> brokerageCalculation(BrokerageReqModel brokerageReqModel){
		return brokerageService.brokerageCalculation(brokerageReqModel);
	}
	
}
