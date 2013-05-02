package web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.School;
import models.SchoolClass;
import models.Teacher;

@Path("schools")
public class SchoolsJSONResource
{

	@GET
	@Path("{id}/classes")
	@Produces("application/json")
	public String getClasses(@PathParam("id") String id)
	{
		return SchoolClass.findBySchool(id).toJson();
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getSchoolById(@PathParam("id") String id)
	{
		return School.findById(id).toJson();
	}

	@GET
	@Path("{id}/teachers")
	@Produces("application/json")
	public String getTeachers(@PathParam("id") String id)
	{
		// PE NOTE no longer in use
		return Teacher.findBySchool(id).toJson();
	}
}
