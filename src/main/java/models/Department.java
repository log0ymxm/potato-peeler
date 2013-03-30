package models;

import database.Model;

public class Department extends Model
{

	private String id;
	private String name;

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
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

	public void setName(String name)
	{
		this.name = name;
	}

}
