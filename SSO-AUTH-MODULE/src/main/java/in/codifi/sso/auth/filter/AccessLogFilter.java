package in.codifi.sso.auth.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.sso.auth.entity.logs.AccessLogModel;
import in.codifi.sso.auth.repository.AccessLogManager;
import in.codifi.sso.auth.utility.AppConstants;
import io.quarkus.arc.Priority;

@Provider
@Priority(Priorities.USER)
public class AccessLogFilter implements ContainerRequestFilter, ContainerResponseFilter {

	ObjectMapper objectMapper = null;

	@Inject
	io.vertx.core.http.HttpServerRequest req;

	@Inject
	AccessLogManager accessLogManager;

	@Inject
	JsonWebToken idToken;

	@Context
	HttpServletRequest request;

	/**
	 * Method to capture and single save request and response
	 * 
	 * @param requestContext
	 * @param responseContext
	 */

	private void caputureInSingleShot(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) {

		String uId = "";
		String clientId = "";

		if (this.idToken.containsClaim("preferred_username")) {
			uId = this.idToken.getClaim("preferred_username").toString();
		}
		if (this.idToken.containsClaim("ucc")) {
			clientId = this.idToken.getClaim("ucc").toString();
		}
		String userId = uId;
		String ucc = clientId;

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					objectMapper = new ObjectMapper();

					AccessLogModel accLogModel = new AccessLogModel();
					UriInfo uriInfo = requestContext.getUriInfo();
					MultivaluedMap<String, String> headers = requestContext.getHeaders();
					accLogModel.setContentType(headers.getFirst(AppConstants.CONTENT_TYPE));
					accLogModel.setDeviceIp(headers.getFirst("X-Forwarded-For"));
					accLogModel.setDomain(headers.getFirst("Host"));
					accLogModel.setInTime((Timestamp) requestContext.getProperty("inTime"));
					accLogModel.setOutTime(new Timestamp(System.currentTimeMillis()));
					accLogModel.setMethod(requestContext.getMethod());
					accLogModel.setModule(AppConstants.MODULE_SSO_AUTH);
					accLogModel.setReqBody(objectMapper.writeValueAsString(requestContext.getProperty("reqBody")));
					Object reponseObj = responseContext.getEntity();
					accLogModel.setResBody(objectMapper.writeValueAsString(reponseObj));
					accLogModel.setSource("");// TODO
					accLogModel.setUri(uriInfo.getPath().toString());
					accLogModel.setUserAgent(headers.getFirst("User-Agent"));
					accLogModel.setUserId(userId);
					accLogModel.setUcc(ucc);
					accLogModel.setLagTime(0);
					accLogModel.setVendor("");// TODO
					accLogModel.setSession(headers.getFirst(AppConstants.AUTHORIZATION));
					accLogModel.setReqId(requestContext.getProperty("threadId") != null
							? requestContext.getProperty("threadId").toString()
							: "singlecapture");
					accessLogManager.insertAccessLog(accLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					pool.shutdown();
				}
			}
		});
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		caputureInSingleShot(requestContext, responseContext);

	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			requestContext.setProperty("inTime", new Timestamp(System.currentTimeMillis()));
			byte[] body = requestContext.getEntityStream().readAllBytes();

			InputStream stream = new ByteArrayInputStream(body);
			requestContext.setEntityStream(stream);
			String formedReq = new String(body);
			requestContext.setProperty("reqBody", formedReq);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
