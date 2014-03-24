package com.listerly.resources.auth;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listerly.services.AuthenticationServiceProvider;

@Path("/authenticate/")
public class AuthenticationResource {
	private static Logger log = Logger.getLogger(AuthenticationResource.class.getName());	

	@Inject AuthenticationServiceProvider authServiceProvider;
	
	@GET
	@Path("facebook") 
	public Response startFacebookAuthentication(@HeaderParam("referer") String referer, @Context HttpServletRequest req) {
		req.getSession(true).setAttribute("referer", referer);
		log.fine("Starting facebook authentication");
		OAuthService service = authServiceProvider.getService("facebook");
		String authorizationUrl = service.getAuthorizationUrl(null);
		URI uri = UriBuilder.fromUri(authorizationUrl).build();
		Response response = Response.seeOther(uri).build();
		return response;
	}
	
	@GET
	@Path("facebook/callback/")
	public Response facebookAuthenticationCallback(@QueryParam("code") String code, 
			@Context HttpServletRequest req) {
		final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";

		log.fine("Callback for facebook authentication");
		OAuthService service = authServiceProvider.getService("facebook");
	    Verifier verifier = new Verifier(code);
	    Token accessToken = service.getAccessToken(null, verifier);
	    log.finest("Now we're going to access a protected resource...");
	    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
	    service.signRequest(accessToken, request);
	    org.scribe.model.Response response = request.send();
	    log.finest("Got it! Lets see what we found...");
	    log.info("Code: " + response.getCode());
	    log.info(response.getBody());

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode node;
		try {
			node = mapper.readTree(response.getBody());
		    String id = node.get("id").asText();
		    String name = node.get("name").asText();
		    String email = node.get("email").asText();
		    log.info(String.format("Got back a response with id %s, email: %s, name: %s", id, name, email));
		    req.getSession().setAttribute("user", id);
		} catch (JsonProcessingException e) {
			log.log(Level.WARNING, "Exception while processing response from Facebook", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Exception while processing response from Facebook", e);
		}
		
		Object attribute = req.getSession().getAttribute("referer");
		log.info("Finished Facebook authentication. Sending back to " + attribute.toString());
		URI uri = UriBuilder.fromUri(attribute.toString()).build();
		Response resp = Response.seeOther(uri).build();
		return resp;

	}
}
