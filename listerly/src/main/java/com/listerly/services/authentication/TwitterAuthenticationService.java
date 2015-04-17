package com.listerly.services.authentication;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.session.SessionStore;

public class TwitterAuthenticationService implements AuthenticationService, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(TwitterAuthenticationService.class.getName());	
	private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
	
	OAuthService service;
	
	@Inject
	SessionStore store;

	@Inject UserDAO userDAO;

	public TwitterAuthenticationService() {
		service = new ServiceBuilder()
	        .provider(SecureTwitterApi.class)
	        .apiKey("pMXGYd4y3Cqg6mYuxiun7Q")
	        .apiSecret("BmbU4iRJC74HP7EM8lKzTfQlVu2eK6yePX1XHqPWrE")
	        .callback("http://listerly-dev.appspot.com/authenticate/twitter/callback/")
	        .build();
	}
	
	@Override
	public URI getAuthenticationRedirectUrl() {
		Token requestToken = service.getRequestToken();
		log.info("Putting in request token into session: " + requestToken.getToken() + " " + requestToken.getSecret());
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		URI uri = UriBuilder.fromUri(authorizationUrl).build();
		store.put("twitterToken", requestToken);
		return uri;
	}

	@Override
	public IUser getAuthenticatedUser(String validationCode) {
		Token requestToken = (Token) store.get("twitterToken");
		log.finest("Getting request token from session: " + requestToken.getToken() + " " + requestToken.getSecret());
		log.finest("Authorization code: " + validationCode);
		Verifier verifier = new Verifier(validationCode);
		Token accessToken = service.getAccessToken(requestToken, verifier);
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		log.finest("Got it! Lets see what we found...");
		log.fine("Code: " + response.getCode());
		log.finest(response.getBody());
		
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode node;
		try {
			node = mapper.readTree(response.getBody());
		    String id = node.get("id_str").asText();
		    String name = node.get("name").asText();
		    String profileImageUrl = node.get("profile_image_url_https").asText();
		    String email = "@" + node.get("screen_name").asText();
		    log.fine(String.format("Got back a response with id %s, email: %s, name: %s", id, name, email));
		    IUser userc = userDAO.findByTwitterId(id);
		    if (userc == null) {
		    	log.fine("Creating a new user (twitter).");
		    	IUser created = userDAO.create();
		    	String[] splitName = name.split(" ");
		    	if (splitName.length > 0) {
		    		created.setFirstName(splitName[0]);
		    		if (splitName.length > 1) created.setLastName(splitName[splitName.length - 1]);
		    	}
		    	created.setTwitterId(id);
		    	created.setProfileImageUrl(profileImageUrl);
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
