package in.codifi.basket.ws.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import in.codifi.basket.entity.logs.RestAccessLogModel;
import in.codifi.basket.model.response.GenericResponse;
import in.codifi.basket.repository.AccessLogManager;
import in.codifi.basket.utils.AppConstants;
import in.codifi.basket.utils.PrepareResponse;
import in.codifi.basket.ws.model.OrderDetails;
import in.codifi.basket.ws.service.spec.InternalRestServiceSpec;
import io.quarkus.logging.Log;

@ApplicationScoped
public class InternalRestService {

	@Inject
	@RestClient
	InternalRestServiceSpec internalRestServiceSpec;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	AccessLogManager accessLogManager;

	public List<GenericResponse> executeOrders(List<OrderDetails> orderDetails, String accessToken) {
		List<GenericResponse> response = null;
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		try {
			String token = "Bearer " + accessToken;
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			response = internalRestServiceSpec.placeOrder(token, orderDetails);
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("executeOrders");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(orderDetails.toString());
			accessLogModel.setUserId(token);
			accessLogModel.setResBody(response.toString());
		} catch (ClientWebApplicationException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			int statusCode = e.getResponse().getStatus();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

		} finally {
			insertRestAccessLogs(accessLogModel);
		}
		return response;

	}

	/**
	 * 
	 * Method to insert rest service access logs
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param accessLogModel
	 */
	public void insertRestAccessLogs(RestAccessLogModel accessLogModel) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					accessLogManager.insertRestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}
}
