package com.listerly.resources.api.v1;

import static java.util.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.listerly.apiobj.user.ASpaceList;
import com.listerly.apiobj.user.ASpaceMetadata;
import com.listerly.apiobj.user.AUser;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IAccessRule;
import com.listerly.entities.ISpace;
import com.listerly.entities.IUser;
import com.listerly.filter.UserRequiredFilter.UserOptional;
import com.listerly.filter.UserRequiredFilter.UserRequired;
import com.listerly.services.UserService;

@Path("/api/v1/user/")
public class UserApi {
    private final Logger log = getLogger(getClass().getName());
	@Inject UserService userSvc;
	@Inject UserDAO userDAO;
	
	@GET
	@Path("current")
	@Produces(MediaType.APPLICATION_JSON)
	@UserOptional
	public AUser getCurrent() {
		IUser user = userSvc.getRequestUser();
		if (user != null) return new AUser(user);
		return null;
	}
	
	@POST
	@UserRequired
	@Path("current")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AUser setCurrent(AUser in) {
		log.fine("Setting current user profile.");

		if (in != null) {
			log.fine("Got a user.");
			AUser user = new AUser(userSvc.getRequestUser());
			if (user != null && user.isLoggedIn()) {
				long currUserId = user.getId();
				long changedUserId = in.getId();
				log.finer("Got a logged in user: " + user.getId());
				log.finer("Changing user: " + in.getId());
				if (currUserId == changedUserId) {
					log.fine("Trying to set profile information on current user.");
					IUser pUser = userDAO.findById(user.getId());
					pUser.setEmail(in.getEmail());
					pUser.setFirstName(in.getFirstName());
					pUser.setLastName(in.getLastName());
					userDAO.save(pUser);
					log.fine("Saved updated user.");
					userSvc.setLoggedInUser(pUser);
					AUser loggedInUser = new AUser(pUser);
					log.fine("Returning updated user: " + loggedInUser.getEmail() + " " + loggedInUser.getFirstName() + " " + loggedInUser.isLoggedIn());
					return loggedInUser;
				} else {
					log.info("Not allowing change for user " + user.getId() + " to user " + in.getId());
				}
			} else {
				log.info("Not able to find a logged in user.");
			}
		} else {
			log.info("Didn't get a user");
		}
		log.info("Unable to set user profile. Returning null");
		return null;
	}
	
	@GET
	@UserRequired
	@Path("spaces")
	@Produces(MediaType.APPLICATION_JSON)
	public ASpaceList getSpaces() {
		log.fine("Retrieving spaces");
		List<ASpaceMetadata> spaceList = new ArrayList<>();
		List<? extends IAccessRule> rulesForUser = userDAO.findAllRulesForUser(userSvc.getRequestUser());
		for (IAccessRule rule : rulesForUser) {
			ISpace space = rule.getSpace();
			ASpaceMetadata asm = new ASpaceMetadata(space);
			spaceList.add(asm);
		}
		return new ASpaceList(spaceList);
	}
}
