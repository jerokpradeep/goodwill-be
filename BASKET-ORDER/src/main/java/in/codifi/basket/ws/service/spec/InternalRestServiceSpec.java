package in.codifi.basket.ws.service.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.basket.model.response.GenericResponse;
import in.codifi.basket.ws.model.OrderDetails;

@RegisterRestClient(configKey = "config-internal")
@RegisterClientHeaders
public interface InternalRestServiceSpec {

	@Path("/orders/execute/basket")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	List<GenericResponse> placeOrder(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			List<OrderDetails> orderDetails);

}
