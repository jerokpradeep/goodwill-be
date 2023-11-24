package in.codifi.ws.model.kc;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTokenResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expires_in")
	private Integer expiresIn;
	@JsonProperty("refresh_expires_in")
	private Integer refreshExpiresIn;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("not-before-policy")
	private Integer notBeforePolicy;
	@JsonProperty("session_state")
	private String sessionState;
	@JsonProperty("scope")
	private String scope;
	@JsonProperty("error")
	private String error;
	@JsonProperty("error_description")
	private String errorDescription;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Integer getRefreshExpiresIn() {
		return refreshExpiresIn;
	}

	public void setRefreshExpiresIn(Integer refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Integer getNotBeforePolicy() {
		return notBeforePolicy;
	}

	public void setNotBeforePolicy(Integer notBeforePolicy) {
		this.notBeforePolicy = notBeforePolicy;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}
