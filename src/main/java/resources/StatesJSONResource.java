package resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.State;

import com.google.gson.Gson;

@Path("states")
public class StatesJSONResource
{

	@GET
	@Path("{id}/schools")
	@Produces("application/json")
	public String getAllSchools(@PathParam("id") String id)
	{
		State state = State.findById(id);
		return new Gson().toJson(state.getSchools());
	}

	@GET
	@Produces("application/json")
	public String getAllStates()
	{
		ArrayList<State> states = State.findAll();
		return new Gson().toJson(states);
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getUserById(@PathParam("id") String id)
	{
		return State.findById(id).toJson();
	}
}
