package com.listerly.services.authentication;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;

@Singleton
public class FacebookAuthenticationService implements AuthenticationService {
	private static Logger log = Logger.getLogger(FacebookAuthenticationService.class.getName());	

	private static final String kFACEBOOK_API_KEY = "1413462062246104";
	private static final String kFACEBOOK_API_SECRET = "f57327ea04814261007164d654125e2b";
	private static final String kFACEBOOK_SCOPES = "basic_info,email";
	private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	private static final String CALLBACK_URL = "https://listerly-dev.appspot.com/authenticate/facebook/callback/";

	private OAuthService service;
	
	@Inject UserDAO userDAO;
	
	public FacebookAuthenticationService() {
		service = new ServiceBuilder()
			.provider(FacebookApi.class)
			.apiKey(kFACEBOOK_API_KEY)
			.apiSecret(kFACEBOOK_API_SECRET)
			.scope(kFACEBOOK_SCOPES)
			.callback(CALLBACK_URL)
			.build();
	}
	
	public URI getAuthenticationRedirectUrl() {
		String authorizationUrl = service.getAuthorizationUrl(null);
		URI uri = UriBuilder.fromUri(authorizationUrl).build();
		return uri;
	}
	
	public IUser getAuthenticatedUser(String code) {
		log.fine("Callback for facebook authentication");

	    Verifier verifier = new Verifier(code);
	    Token accessToken = service.getAccessToken(null, verifier);
	    log.finest("Now we're going to access a protected resource...");
	    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
	    service.signRequest(accessToken, request);
	    org.scribe.model.Response response = request.send();
	    log.finest("Got it! Lets see what we found...");
	    log.finest("Code: " + response.getCode());
	    log.finest(response.getBody());

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode node;
		try {
			node = mapper.readTree(response.getBody());
		    String id = node.get("id").asText();
		    String name = node.get("name").asText();
		    String email = node.get("email").asText();
		    log.info(String.format("Got back a response with id %s, email: %s, name: %s", id, name, email));
		    IUser userc = userDAO.findByFacebookId(id);
		    if (userc == null) {
		    	log.info("Creating a new user.");
		    	IUser created = userDAO.create();
		    	created.setEmail(email);
		    	created.setFacebookId(id);
		    	created.setName(name);
		    	userc = userDAO.save(created);
		    }
		    
		    return userc;
		    
		} catch (JsonProcessingException e) {
			log.log(Level.WARNING, "Exception while processing response from Facebook", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Exception while processing response from Facebook", e);
		}
		
		return null;
	}
}
