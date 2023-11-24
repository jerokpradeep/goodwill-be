package in.codifi.holdings.ws.service.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "ltp-api")
public interface LTPRestServiceSpec {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getLTP(@QueryParam("getltps") String getltps);
}
