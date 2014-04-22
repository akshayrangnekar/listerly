package com.listerly.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.listerly.apiobj.user.AUser;
import com.listerly.config.jersey.UserRequiredFilter.UserOptional;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.services.UserService;
import com.listerly.services.authentication.AuthenticationServiceProvider;
import com.listerly.session.SessionStore;

@Path("/authenticate/")
public class AuthenticationResource {
	private static Logger log = Logger.getLogger(AuthenticationResource.class.getName());	

	@Inject AuthenticationServiceProvider authServiceProvider;
	@Inject SessionStore session;
	@Inject UserDAO userDAO;
	@Inject UserService userSvc;
	
	@GET
	@Path("logout")
	public Response logout() {
		try {
			session.put("user", null); // TODO: Yeah, may want to invalidate token too.
			return Response.seeOther(new URI("/")).build();
		} catch (URISyntaxException e) {
			return Response.ok().build();
		}
	}
	
	@GET
	@Path("state")
	@Produces(MediaType.APPLICATION_JSON)
	@UserOptional
	public AUser getLoggedInUser() {
		AUser user = (AUser) userSvc.getRequestUser();
		if (user != null && user.isLoggedIn()) return user;
		return null;
		
	}
	
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
		IUser pUser = authServiceProvider.getAuthenticatedUser(serviceName, code);
		if (pUser == null) {
			log.fine("Did not succeed with " + serviceName + " authentication.");
		} else {
			log.fine("Succeeded with " + serviceName + " authentication.");
			log.fine("Putting data into session: " + session);
			userSvc.setLoggedInUser(pUser);
			//			session.setUser(user);
//			session.setMessage("Yo what's up dawg: " + serviceName);
			
//			String loginToken = null;
//			session.put("message", "Log in successful");
//			AUser aUser = new AUser(pUser);
//			session.put("user", aUser);
//			session.put("userid", pUser.getId());
//			log.fine("Put user: " + aUser.getId());
//			loginToken = userDAO.createLoginToken(pUser);
//			session.put("token", loginToken);
		}
		Object attribute = session.get("referer");//req.getSession().getAttribute("referer");
		log.info("Finished " + serviceName + " authentication. Sending back to " + attribute.toString());
		URI uri = UriBuilder.fromUri(attribute.toString()).build();
		Response resp = null;
		resp = Response.seeOther(uri).build();
		return resp;
	}

}
