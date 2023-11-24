package in.codifi.auth.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.entity.logs.AccessLogModel;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.StringUtil;
import io.quarkus.arc.Priority;
import io.quarkus.logging.Log;

@Provider
@Priority(Priorities.USER)
public class FilterLog implements ContainerRequestFilter, ContainerResponseFilter {

	ObjectMapper objectMapper = null;

	@Inject
	io.vertx.core.http.HttpServerRequest req;

	@Inject
	AccessLogManager accessLogManager;

	@Inject
	JsonWebToken idToken;

	/**
	 * Method to capture and single save request and response
	 * 
	 * @param requestContext
	 * @param responseContext
	 */
	private void caputureInSingleShot(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) {

//		String uId = this.idToken.getClaim("preferred_username").toString();
		String uId = "";
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
					long lagTime = System.currentTimeMillis() - System.currentTimeMillis();
					accLogModel.setInTime((Timestamp) requestContext.getProperty("inTime"));
					accLogModel.setOutTime(new Timestamp(System.currentTimeMillis()));
					accLogModel.setMethod(requestContext.getMethod());
					accLogModel.setModule(AppConstants.MODULE);
					accLogModel.setReqBody(objectMapper.writeValueAsString(requestContext.getProperty("reqBody")));
					Object reponseObj = responseContext.getEntity();
					accLogModel.setResBody(objectMapper.writeValueAsString(reponseObj));
					accLogModel.setSource("");
					accLogModel.setUri(uriInfo.getPath().toString());
					accLogModel.setUserAgent(headers.getFirst(AppConstants.USER_AGENT));
					String userId = objectMapper.writeValueAsString(requestContext.getProperty("userId"));
					accLogModel.setUserId(userId.replaceAll("\"", ""));
					accLogModel.setVendor("");// TODO
					accLogModel.setSession(headers.getFirst(AppConstants.AUTHORIZATION));
					accLogModel.setReqId(requestContext.getProperty("threadId") != null
							? requestContext.getProperty("threadId").toString()
							: "singlecapture");
					accessLogManager.insertAccessLog(accLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pool.shutdown();
				}
			}
		});
	}

	/**
	 * Method to capture log
	 * 
	 * @author Nesan
	 * @param requestContext
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = null;
		try {
			requestContext.setProperty("inTime", new Timestamp(System.currentTimeMillis()));
			byte[] body = requestContext.getEntityStream().readAllBytes();

			InputStream stream = new ByteArrayInputStream(body);
			requestContext.setEntityStream(stream);
			String formedReq = new String(body);
			requestContext.setProperty("reqBody", formedReq);

			String[] securedMethod = AppConstants.SECURED_METHODS;
			authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String path = requestContext.getUriInfo().getPath().trim();
			boolean contains = Arrays.stream(securedMethod).anyMatch(path::equals);
			if (contains) {

				/** Check if the HTTP Authorization header is present **/
				if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
					throw new NotAuthorizedException("Authorization header must be provided");
				}

				String token[] = authorizationHeader.substring("Bearer".length()).trim().split(" ");
				if (token.length < 3) {
					throw new NotAuthorizedException("Invalid Authorization header");
				}
				ObjectMapper mapper = new ObjectMapper();
				JSONObject json = new JSONObject(formedReq);
				String userId = (String) json.get("userId");
				if (StringUtil.isNotNullOrEmpty(userId) && StringUtil.isNotNullOrEmpty(token[0])
						&& userId.equals(token[0])) {
					validateToken(token[0], token[1], token[2]);
				} else {
					throw new NotAuthorizedException("Invalid User");
				}

			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			requestContext
					.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(AppConstants.UNAUTHORIZED).build());
		}

	}

	private void validateToken(String userId, String source, String token) throws Exception {

		String key = userId + "_" + source + AppConstants.HAZEL_KEY_OTP_SESSION;
		if (HazelcastConfig.getInstance().getUserSessionOtp().containsKey(key)) {
			if (!token.equals(HazelcastConfig.getInstance().getUserSessionOtp().get(key))) {
				throw new NotAuthorizedException("Not Authorized");
			}
		} else {
			throw new NotAuthorizedException("Not Authorized");
		}

	}

	/**
	 * Method to capture request and response from the http request
	 * 
	 * @param requestContext
	 * @param responseContext
	 */

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		caputureInSingleShot(requestContext, responseContext);

	}
}