package com.listerly.resources;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.mvc.Template;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listerly.SecondTest;
import com.listerly.TestClass;
import com.listerly.api.SimpleReturnObject;
import com.listerly.entities.SimpleTestEntity;

@Path("/Hey")
public class HeyResource {
	private static Logger log = Logger.getLogger(HeyResource.class.getName());	
	private static Token theToken;
	
	@Inject TestClass first;
	@Inject SecondTest second;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String get() {
		return ("Hey there");
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/foo")
	public String foo(@QueryParam("test") String test) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		StringBuilder builder = new StringBuilder();

		builder.append("Hi there. You entered ").append(test).append("\n");
		builder.append("The first date is:").append(sdf.format(first.getDate())).append("\n");
		builder.append("The second date is:").append(sdf.format(second.getDate())).append("\n");
		
		return builder.toString();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/json")
	public Object json() {
		SimpleReturnObject ret = new SimpleReturnObject();
		
		ret.setFruit("Apple");
		ret.setHome("Hong Kong");
		
		return ret;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/template")
	@Template(name="/foo.ftl")
	public Map<String, Object> template() {
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "Akshay");
		map.put("bar", "Yo Yo Whatsup?");
		return map;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/persist")
	@Template(name="/foo.ftl")
	public Map<String, Object> persist(@QueryParam("name") String name, @QueryParam("message") String message) {
		Map<String, Object> map = new HashMap<>();
		map.put("foo", name);
		map.put("bar", message);
		SimpleTestEntity ste = new SimpleTestEntity();
		ste.setName(name);
		
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("transactions-optional");
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(ste);
        entityManager.getTransaction().commit();

		return map;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/retrieve")
	@Template(name="/foo.ftl")
	public Map<String, Object> retrieve() {
		Map<String, Object> map = new HashMap<>();
		
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("transactions-optional");
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("select t from " + SimpleTestEntity.class.getSimpleName() + " t");
        List<?> list = q.getResultList();
        map.put("foo", "Number of Entries");
        map.put("bar", "The number of database entries is: " + list.size());
        
        entityManager.getTransaction().commit();

		return map;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/auth1")
	public String auth1() {
		final Token EMPTY_TOKEN = null;

		StringBuilder builder = new StringBuilder();
		OAuthService service = new ServiceBuilder()
			.provider(FacebookApi.class)
			.apiKey("1413462062246104")
			.apiSecret("f57327ea04814261007164d654125e2b")
			.scope("basic_info,email")
			.callback("http://annkh-source.appspot.com/")
			.build();
		
		String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

		URI uri = UriBuilder.fromUri(authorizationUrl).build();
		if (uri != null) {
			javax.ws.rs.core.Response response = javax.ws.rs.core.Response.seeOther(uri).build();		
			WebApplicationException wae = new WebApplicationException(response);
			throw wae;
		}
		builder.append("Got authorization URL").append("\n");		
		builder.append(authorizationUrl);
		return builder.toString();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/auth2/")
	public String auth2(@QueryParam("code") String code) throws JsonProcessingException, IOException {
		final Token EMPTY_TOKEN = null;
		final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
		StringBuilder builder = new StringBuilder();
		OAuthService service = new ServiceBuilder()
			.provider(FacebookApi.class)
			.apiKey("1413462062246104")
			.apiSecret("f57327ea04814261007164d654125e2b")
			.scope("basic_info,email,user_birthday")
			.callback("http://annkh-source.appspot.com/")
			.build();
		
		// Now let's go and ask for a protected resource!
	    Verifier verifier = new Verifier(code);
	    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
	    builder.append("Now we're going to access a protected resource...").append("\n");
	    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    builder.append("Got it! Lets see what we found...").append("\n").append("\n");
	    builder.append(response.getCode()).append("\n");
	    builder.append(response.getBody()).append("\n");

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode node = mapper.readTree(response.getBody());
	    String id = node.get("id").asText();
	    builder.append(id).append("\n");
		return builder.toString();
	}
	
	@GET
	@Path("/gauth") 
	public javax.ws.rs.core.Response gauth() {

		final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
		
		OAuthService service = new ServiceBuilder()
	        .provider(GoogleApi.class)
	        .apiKey("222299669952-3peqbcqrdbj3v4i2nfv6r7rmcid5cusa.apps.googleusercontent.com")
	        .apiSecret("naQUpPytONPe59X1kcTOR5os")
	        .scope("https://www.googleapis.com/auth/plus.me")
			.callback("http://localhost:8080/Hey/gauthcallback/")
	        .build();
		
		Token requestToken = service.getRequestToken();
		theToken = requestToken;
		log.info("Got the Request Token!");
		log.info("(if your curious it looks like this: " + requestToken + " )");
		log.info("Now go and authorize Scribe here:");
		log.info(AUTHORIZE_URL + requestToken.getToken());
	    
		URI uri = UriBuilder.fromUri(AUTHORIZE_URL + requestToken.getToken()).build();
		return javax.ws.rs.core.Response.seeOther(uri).build();
	}
	
	@GET
	@Path("/gauthcallback/") 
	public String gauthcallback(@QueryParam("oauth_token") String authToken, @QueryParam("oauth_verifier") String authVerifier) {
		//final String NETWORK_NAME = "Google";
		//final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
		final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me";

		StringBuilder builder = new StringBuilder();

		OAuthService service = new ServiceBuilder()
	        .provider(GoogleApi.class)
	        .apiKey("222299669952-3peqbcqrdbj3v4i2nfv6r7rmcid5cusa.apps.googleusercontent.com")
	        .apiSecret("naQUpPytONPe59X1kcTOR5os")
	        .scope("https://www.googleapis.com/auth/userinfo.profile")
			.callback("http://localhost:8080/Hey/gauthcallback/")
	        .build();

	    log.info("In the callback!");
	    Verifier verifier = new Verifier(authVerifier);
	    Token requestToken = theToken;
	    log.info("In the callback 2 with token: " + requestToken);
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    
	    log.info("Got the Access Token!");
	    log.info("(if your curious it looks like this: " + accessToken + " )");

	    // Now let's go and ask for a protected resource!
	    log.info("Now we're going to access a protected resource...");
	    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
	    service.signRequest(accessToken, request);
	    request.addHeader("GData-Version", "3.0");
	    Response response = request.send();
	    log.info("Got it! Lets see what we found...");
	    log.info("");
	    log.info("Response Code: " + response.getCode());
	    builder.append(response.getBody()).append("\n");
	    
	    request = new OAuthRequest(Verb.POST,"https://accounts.google.com/o/oauth2/token");
	    request.addBodyParameter("grant_type", "refresh_token");
	    request.addBodyParameter("refresh_token", accessToken.getToken()); // were accessToken is the Token object you want to refresh.
	    request.addBodyParameter("client_id", "222299669952-3peqbcqrdbj3v4i2nfv6r7rmcid5cusa.apps.googleusercontent.com");
	    request.addBodyParameter("client_secret", "naQUpPytONPe59X1kcTOR5os");
	    response = request.send();
	    log.info("Got it! Lets see what we found...");
	    log.info("");
	    log.info("Response Code: " + response.getCode());
	    builder.append("\n\n\n\n");
	    builder.append(response.getBody()).append("\n");

		return builder.toString();
	}
	
	@GET
	@Path("/page/")
	@Template(name="/page.ftl")
	public Map<String, Object> loadPage() {
		Map<String, Object> map = new HashMap<>();
		return map;
	}
}
