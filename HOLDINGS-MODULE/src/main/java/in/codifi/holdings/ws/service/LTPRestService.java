package in.codifi.holdings.ws.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import in.codifi.holdings.ws.service.spec.LTPRestServiceSpec;

@ApplicationScoped
public class LTPRestService {

	@Inject
	@RestClient
	LTPRestServiceSpec ltpRestServiceSpec;

	public String getLTP(String request) {
		String respone = "";
		try {
			respone = ltpRestServiceSpec.getLTP(request);
		} catch (Exception e) {

		}
		return respone;
	}
}
