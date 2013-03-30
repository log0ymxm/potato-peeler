package models;

import database.Model;

public class Class extends Model
{

	private Department department;
	private String id;
	private int level;

	public Department getDepartment()
	{
		return this.department;
	}

	public String getId()
	{
		return this.id;
	}

	public int getLevel()
	{
		return this.level;
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

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

}
