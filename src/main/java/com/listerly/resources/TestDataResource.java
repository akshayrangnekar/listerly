package com.listerly.resources;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.List;
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
import com.listerly.entities.IAccessRule.AccessLevel;
import com.listerly.entities.IField;
import com.listerly.entities.IFieldOption;
import com.listerly.entities.ISpace;
import com.listerly.entities.ISpaceView;
import com.listerly.entities.IUser;

@Path("/testdata")
public class TestDataResource {
	private static Logger log = Logger.getLogger(TestDataResource.class.getName());	
	
	@Inject SpaceDAO spaceDAO;
	@Inject UserDAO userDAO;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/generate") public String generate() {
		StringBuilder builder = new StringBuilder();
		log.fine("Generating test data.");
		ISpace space = createNewSpace("Akshay's Notes", "Importance", "Category");
		space = setupSpace(space);
		space = createNewSpace("Akshay's Work Tasks Notes", "Level", "Group");
		space = setupSpace(space);

		builder.append("Created board: ").append(space.getId()).append("\n");
		return builder.toString();
	}
	
	private ISpace setupSpace(ISpace space) {
		space = createSpaceViews(space);
		log.fine("Saving space.");
		spaceDAO.save(space);
		log.fine("Saved space.");

		IUser user = createOrGetUser();
		spaceDAO.createAccessRule(space, user, AccessLevel.READWRITE);
		return space;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/space/{spaceId}") public Object retrieveSpace(@PathParam("spaceId") Long spaceId) {
		ISpace space = spaceDAO.findById(spaceId);
		return space;
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/transactExample") public String test6() {
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

	private IUser createOrGetUser() {
		IUser user = userDAO.findByGoogleId("akshay@a13r.com");
		
		if (user == null) {
			user = userDAO.create();
			user.setGoogleId("akshay@a13r.com");
			user.setEmail("akshay@a13r.com");
			user.setFirstName("Akshay");
			user.setLastName("Rangnekar");
			user = userDAO.save(user);
		}
		
		return user;
	}
	private ISpace createNewSpace(String name, String importanceFieldName, String categoryFieldName) {
		log.fine("Creating space.");
		ISpace space = spaceDAO.create();
		space.setName(name);

		log.fine("Creating Status field.");
		IField fieldStatus = space.createField();
		fieldStatus.setName("Status");
		fieldStatus.setType("booleanDate");
		fieldStatus.setListable(false);
		
		log.fine("Creating Description field.");
		IField desc = space.createField();
		desc.setName("Description");
		desc.setType("text");
		desc.setListable(false);
		
		log.fine("Creating Importance field.");
		IField importance = space.createField();
		importance.setName(importanceFieldName);
		importance.setType("select-fixed");
		importance.setSetting("fixed", true);
		importance.setListable(true);
		IFieldOption option = importance.createOption();
			option.setColorCode("red2");
			option.setDisplay("Urgent and Important");
		
		option = importance.createOption();
			option.setColorCode("orange");
			option.setDisplay("Important");
		
		option = importance.createOption();
			option.setColorCode("orange");
			option.setDisplay("Urgent");
		
		option = importance.createOption();
			option.setColorCode("green");
			option.setDisplay("Not Urgent or Important");
		
		
		log.fine("Creating Category field.");
		IField category = space.createField();
		category.setName(categoryFieldName);
		category.setType("select");
		category.setSetting("fixed", false);
		category.setListable(true);
		option = category.createOption();
			option.setColorCode("blue2");
			option.setDisplay("Personal");
		
		option = category.createOption();
			option.setColorCode("purple");
			option.setDisplay("Source");
	
		option = category.createOption();
			option.setColorCode("green");
			option.setDisplay("Listerly");

		return space;
	}

	private ISpace createSpaceViews(ISpace space) {
		List<? extends IField> fields = space.getFields();
		IField checkboxField = null;
		
		for (IField field : fields) {
			if (checkboxField == null && (field.getType().equals("booleanDate") || field.getType().equals("boolean"))) {
				log.info("Found a checkbox field");
				checkboxField = field;
			}
		}
		
		for (IField field : fields) {
			if (field.getListable()) {
				log.info("Creating view for " + field.getName());
				ISpaceView view = space.createView();
				view.setName("By " + field.getName());
				if (field.getName().equals("Importance")) {
					view.setLayoutType("GRID");
				} else {
					view.setLayoutType("LIST");
				}
				if (checkboxField != null) view.setCheckboxFieldUuid(checkboxField.getUuid());
				view.setPrimaryFieldUuid(field.getUuid());
			}
		}
		
		return space;
	}
}
