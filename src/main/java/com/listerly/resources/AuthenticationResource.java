package com.listerly.resources;

import java.net.URI;
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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.listerly.entities.IUser;
import com.listerly.services.authentication.AuthenticationServiceProvider;
import com.listerly.session.SessionStore;

@Path("/authenticate/")
public class AuthenticationResource {
	private static Logger log = Logger.getLogger(AuthenticationResource.class.getName());	

	@Inject AuthenticationServiceProvider authServiceProvider;
	@Inject SessionStore session;
	
	@GET
	@Path("facebook") 
	public Response startFacebookAuthentication(@HeaderParam("referer") String referer, @Context HttpServletRequest req) {
		return startOAuthAuthentication(referer, req, "Facebook");
	}
	
	@GET
	@Path("facebook/callback/")
	public Response facebookAuthenticationCallback(@QueryParam("code") String code, 
			@Context HttpServletRequest req) {
		return OAuthenticationCallback(code, req, "Facebook");
	}
	
	@GET
	@Path("twitter")
	public Response startTwitterAuthentication(@HeaderParam("referer") String referer, @Context HttpServletRequest req) {
		return startOAuthAuthentication(referer, req, "Twitter");
	}
	
	@GET
	@Path("twitter/callback/")
	public Response twitterAuthenticationCallback(@QueryParam("oauth_verifier") String code, 
			@Context HttpServletRequest req) {
		return OAuthenticationCallback(code, req, "Twitter");
	}
	
	@GET
	@Path("google")
	public Response startGoogleAuthentication(@HeaderParam("referer") String referer, @Context HttpServletRequest req) {
		return startOAuthAuthentication(referer, req, "Google");
	}

	@GET
	@Path("google/callback/")
	public Response googleAuthenticationCallback(@Context HttpServletRequest req) {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			log.info("Login did not succeed.");
			
		} 
		return OAuthenticationCallback(user.getEmail(), req, "Google");
	}
	
	private Response startOAuthAuthentication(String referer, HttpServletRequest req, String serviceName) {
		log.fine("Starting "+ serviceName +" authentication");
		//req.getSession(true).setAttribute("referer", referer);
		session.put("referer", referer);
		URI uri = authServiceProvider.getAuthenticationUrl(serviceName);
		Response response = Response.seeOther(uri).build();
		return response;
	}
	
	private Response OAuthenticationCallback(String code, HttpServletRequest req, String serviceName) {
		IUser user = authServiceProvider.getAuthenticatedUser(serviceName, code);
		if (user == null) {
			log.info("Did not succeed with " + serviceName + " authentication.");
		} else {
			log.info("Succeeded with " + serviceName + " authentication.");
			log.info("Putting data into session: " + session);
//			session.setUser(user);
//			session.setMessage("Yo what's up dawg: " + serviceName);
			session.put("message", "Log in successful");
			session.put("user", user.getEmail());
			log.info("Put user: " + user.getId());
		}
		Object attribute = session.get("referer");//req.getSession().getAttribute("referer");
		log.info("Finished " + serviceName + " authentication. Sending back to " + attribute.toString());
		URI uri = UriBuilder.fromUri(attribute.toString()).build();
		Response resp = Response.seeOther(uri).build();
		return resp;
	}

}
