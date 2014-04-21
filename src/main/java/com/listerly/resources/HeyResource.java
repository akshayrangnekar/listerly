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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import com.googlecode.objectify.VoidWork;
import com.listerly.apiobj.user.AUser;
import com.listerly.config.jersey.UserRequiredFilter.UserRequired;
import com.listerly.dao.SpaceDAO;
import com.listerly.dao.UserDAO;
import com.listerly.entities.ISpace;
import com.listerly.entities.IUser;
import com.listerly.session.SessionStore;

@Path("/Hey")
public class HeyResource {
	private static Logger log = Logger.getLogger(HeyResource.class.getName());	
		
	@Inject AUser fsuser;
	@Inject UserDAO userDAO;
	@Inject SpaceDAO spaceDAO;
	@Inject SessionStore session;
	
	@UserRequired
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
			log.fine("Your user: " + fsuser);
			log.fine("Is logged in: " + fsuser.isLoggedIn());
		}
		return map;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test5") public Object test5() {
		log.finest("Returning all Users as JSON");
		List<? extends IUser> findAll = userDAO.findAll();
		return findAll;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/test6") public String test6() {
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
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/testCreateSpace") public String testCreateSpace() {
		ISpace space = spaceDAO.create();
		
//		space.setName("Hello");
//		IField field = spaceDAO.createField();
//		field.setListable(true);
//		field.setName("Field1");
//		field.setType("String");
//		space.addFieldSetting(field);
		
//		IField field2 = spaceDAO.createField();
//		field2.setListable(false);
//		field2.setName("Field2");
//		field2.setType("BooleanDate");
//		field2.setSetting("Foobar", "McGoober");
//		space.addFieldSetting(field2);
		
//		List<String> fieldValues = new ArrayList<String>();
//		fieldValues.add("URGENT AND IMPORTANT");
//		fieldValues.add("URGENT");
//		fieldValues.add("URGENT");
//		fieldValues.add("URGENT");
		
//		log.fine("Saving a space with: " + space.getFields().size());
		spaceDAO.save(space);
		
		return "Created new space: " + space.getId();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/space/{spaceId}/") public Object retrieveSpace(@PathParam("spaceId") Long spaceId) {
		ISpace space = spaceDAO.findById(spaceId);
		return space;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/spaceT/{spaceId}/") public String retrieveSpaceText(@PathParam("spaceId") Long spaceId) {
		StringBuilder builder = new StringBuilder();
		ISpace space = spaceDAO.findById(spaceId);
		builder.append("Space ID: " + space.getId()).append("\n");
		builder.append("Space name: ").append(space.getName()).append("\n");
//		builder.append("Space fields: ").append(space.getFields().size()).append("\n");
		return builder.toString();
	}

}
