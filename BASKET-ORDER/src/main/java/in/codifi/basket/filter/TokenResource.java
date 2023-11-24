/**
 * 
 */
package in.codifi.basket.filter;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.OidcSession;
import io.quarkus.oidc.RefreshToken;

/**
 * @author mohup
 *
 */
@Path("/token")
public class TokenResource {
	/**
	 * Injection point for the ID Token issued by the OpenID Connect Provider
	 */
	@Inject
	@IdToken
	JsonWebToken idToken;

	/**
	 * Injection point for the Access Token issued by the OpenID Connect Provider
	 */
	@Inject
	JsonWebToken accessToken;

	/**
	 * Injection point for the Refresh Token issued by the OpenID Connect Provider
	 */
	@Inject
	RefreshToken refreshToken;

	@Inject
	OidcSession oidcSession;

	@GET
	@Path("/logout")
	public String logout() {
		oidcSession.logout().await().indefinitely();
		return "You are logged out";
	}

	/**
	 * Returns the tokens available to the application. This endpoint exists only
	 * for demonstration purposes, you should not expose these tokens in a real
	 * application.
	 *
	 * @return a HTML page containing the tokens available to the application
	 */
	@GET
	@Produces("text/html")
	public String getTokens() {
		StringBuilder response = new StringBuilder().append("<html>").append("<body>").append("<ul>");

		Object userName = this.idToken.getClaim("preferred_username");
		Set<String> allClaims = this.idToken.getClaimNames();
		for (String claim : allClaims) {
			System.out.println("Claim name: " + claim + " : " + this.idToken.getClaim(claim));
		}

		if (userName != null) {
			response.append("<li>username: ").append(userName.toString()).append("</li>");
		}

		Object scopes = this.accessToken.getClaim("scope");

		if (scopes != null) {
			response.append("<li>scopes: ").append(scopes.toString()).append("</li>");
		}

		response.append("<li>refresh_token: ").append(refreshToken.getToken() != null).append("</li>");

		return response.append("</ul>").append("</body>").append("</html>").toString();
	}
}
