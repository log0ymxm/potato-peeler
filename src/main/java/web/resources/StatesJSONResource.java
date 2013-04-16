package web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.State;

@Path("states")
public class StatesJSONResource
{

	@GET
	@Path("{id}/schools")
	@Produces("application/json")
	public String getAllSchools(@PathParam("id") String id)
	{
		return State.findById(id).toJson();
	}

	@GET
	@Produces("application/json")
	public String getAllStates()
	{
		return State.findAll().toJson();
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getUserById(@PathParam("id") String id)
	{
		return State.findById(id).toJson();
	}

}
