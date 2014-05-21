package com.listerly.resources.api.v1;

import static java.util.logging.Logger.getLogger;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.listerly.apiobj.ApiResponse;
import com.listerly.apiobj.space.ASpace;
import com.listerly.dao.SpaceDAO;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.filter.UserRequiredFilter.UserRequired;
import com.listerly.util.LogUtil;

@Path("/api/v1/space")
public class SpaceApi {
    private final Logger log = getLogger(getClass().getName());
    
    @Inject SpaceDAO spaceDAO;

	@GET
	@Path("/{spaceId}")
	@Produces(MediaType.APPLICATION_JSON)
	@UserRequired
	public ApiResponse getSpace(@PathParam("spaceId") Long spaceId) 
	{
		log.fine("Retrieving space: " + spaceId);
		ISpace space = spaceDAO.findById(spaceId);
		List<? extends IItem> cardsInSpace = spaceDAO.findAllCardsInSpace(space);
		log.fine("Number of cards in space: " + cardsInSpace.size());

		ApiResponse response = new ApiResponse(new ASpace(space, cardsInSpace));

		LogUtil.debugPrint("Space API response", response);
		return response;
	}

}
