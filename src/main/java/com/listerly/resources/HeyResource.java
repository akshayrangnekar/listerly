package com.listerly.resources;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import com.googlecode.objectify.VoidWork;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.session.SessionStore;

@Path("/Hey")
public class HeyResource {
	private static Logger log = Logger.getLogger(HeyResource.class.getName());	
		
	@Inject UserDAO userDAO;
	@Inject SessionStore session;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Template(name="/page.ftl")
	public Map<String, Object> get() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) log.info("Session was null");
		else {
			log.fine("Your session: " + session);
			log.fine("Message in Map:" + session.get("message"));
			log.fine("Email in Map:" + session.get("user"));
		}
		return map;
	}
	
	@GET
	@Path("/test5")
	@Produces(MediaType.APPLICATION_JSON)
	public Object test5() {
		log.finest("Returning all Users as JSON");
		List<? extends IUser> findAll = userDAO.findAll();
		return findAll;
	}
	
	@GET
	@Path("/test6")
	@Produces(MediaType.TEXT_PLAIN)
	public String test6() {
		log.finest("Creating new user");
		final Random rnd = new Random();
		ofy().transact(new VoidWork() {
	        public void vrun() {
    			int nextInt = rnd.nextInt();
    			IUser usr = userDAO.create();
    			usr.setEmail("" + nextInt + "@google.com");
    			usr.setFacebookId("" + nextInt);
    			userDAO.save(usr);
	        }
	    });
		
		return "Created user. Use appstats to figure out timing. ";
	}

}
