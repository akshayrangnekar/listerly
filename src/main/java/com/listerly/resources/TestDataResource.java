package com.listerly.resources;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.Random;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.googlecode.objectify.VoidWork;
import com.listerly.dao.SpaceDAO;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IField;
import com.listerly.entities.IFieldOption;
import com.listerly.entities.IFieldOptions;
import com.listerly.entities.ISpace;
import com.listerly.entities.IUser;

@Path("/testdata")
public class TestDataResource {
	private static Logger log = Logger.getLogger(HeyResource.class.getName());	
	
	@Inject SpaceDAO spaceDAO;
	@Inject UserDAO userDAO;
	
	@GET
	@Path("/generate")
	@Produces(MediaType.TEXT_PLAIN)
	public String generate() {
		StringBuilder builder = new StringBuilder();
		log.fine("Generating test data.");
		
		log.fine("Creating space.");
		ISpace space = spaceDAO.create();
		space.setName("Akshay's Task List");

		log.fine("Creating Status field.");
		IField setting = space.createFieldSetting();
		setting.setName("Status");
		setting.setType("booleanDate");
		setting.setListable(false);
		
		log.fine("Creating Description field.");
		setting = space.createFieldSetting();
		setting.setName("Description");
		setting.setType("text");
		setting.setListable(false);
		
		log.fine("Creating Importance field.");
		setting = space.createFieldSetting();
		setting.setName("Importance");
		setting.setType("select-fixed");
		setting.setSetting("fixed", true);
		setting.setListable(true);
		IFieldOptions fieldOptions = setting.getFieldOptions();
			IFieldOption option = fieldOptions.createAtEnd();
				option.setColorCode("red2");
				option.setDisplay("Urgent and Important");
			
			option = fieldOptions.createAtEnd();
				option.setColorCode("orange");
				option.setDisplay("Important");
			
			option = fieldOptions.createAtEnd();
				option.setColorCode("orange");
				option.setDisplay("Urgent");
			
			option = fieldOptions.createAtEnd();
				option.setColorCode("green");
				option.setDisplay("Not Urgent or Important");
		
		
		log.fine("Creating Category field.");
		setting = space.createFieldSetting();
		setting.setName("Category");
		setting.setType("select");
		setting.setSetting("fixed", false);
		setting.setListable(true);
		fieldOptions = setting.getFieldOptions();
			option = fieldOptions.createAtEnd();
				option.setColorCode("blue2");
				option.setDisplay("Personal");
			
			option = fieldOptions.createAtEnd();
				option.setColorCode("purple");
				option.setDisplay("Source");
		
			option = fieldOptions.createAtEnd();
				option.setColorCode("green");
				option.setDisplay("Listerly");
		
		log.fine("Saving space.");
		spaceDAO.save(space);
		log.fine("Saved space.");
		builder.append("Created board: ").append(space.getId()).append("\n");
		return builder.toString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/space/{spaceId}") public Object retrieveSpace(@PathParam("spaceId") Long spaceId) {
		ISpace space = spaceDAO.findById(spaceId);
		return space;
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

}
