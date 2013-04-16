package resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.School;
import models.Teacher;

import com.google.gson.Gson;

@Path("schools")
public class SchoolsJSONResource
{

	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getSchoolById(@PathParam("id") String id)
	{
		School school = School.findById(id);
		return school.toJson();
	}

	@GET
	@Path("{id}/teachers")
	@Produces("application/json")
	public String getTeachers(@PathParam("id") String id)
	{
		// TODO
		ArrayList<Teacher> teachers = Teacher.findBySchool(id);
		return new Gson().toJson(teachers);
	}
}
