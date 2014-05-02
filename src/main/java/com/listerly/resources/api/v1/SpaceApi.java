package com.listerly.resources.api.v1;

import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.listerly.apiobj.user.ASpace;
import com.listerly.apiobj.user.ApiResponse;
import com.listerly.dao.SpaceDAO;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.filter.UserRequiredFilter.UserRequired;

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

		// TODO: Debugging code to be removed
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, space);
			log.info("Sending back space: " + sw.toString());
		} catch (IOException e) {
			log.log(Level.WARNING, "Unable to serialize space:", e);
		}
		
		ApiResponse response = new ApiResponse(new ASpace(space, cardsInSpace));
		return response;
//		return space;
	}

}
