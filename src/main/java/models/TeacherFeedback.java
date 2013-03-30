package models;

import database.Model;

public class TeacherFeedback extends Model
{

	private String id;

	public String getId()
	{
		return this.id;
	}

	@Override
	public String isValid()
	{
		// TODO implement
		return "Not Implemented";
	}

	@Override
	public boolean save()
	{
		// TODO implement
		return false;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
