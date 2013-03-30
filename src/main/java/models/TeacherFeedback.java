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
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean save()
	{
		// TODO
		throw new UnsupportedOperationException();
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
