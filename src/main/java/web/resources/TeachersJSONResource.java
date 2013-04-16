package web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.SchoolClass;
import models.Teacher;

@Path("teachers")
public class TeachersJSONResource
{

	@GET
	@Path("{id}/classes")
	@Produces("application/json")
	public String getAllClasses(@PathParam("id") String id)
	{
		return SchoolClass.findByTeacher(id).toJson();
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getTeacher(@PathParam("id") String id)
	{
		return Teacher.findById(id).toJson();
	}

}
