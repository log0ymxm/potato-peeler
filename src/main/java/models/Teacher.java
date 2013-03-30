package models;

import java.util.ArrayList;

import database.Model;

public class Teacher extends Model
{

	private Department department;
	private String first_name;
	private String id;
	private String last_name;
	private String rmpId;
	private ArrayList<School> school;

	public Department getDepartment()
	{
		return this.department;
	}

	public String getFirst_name()
	{
		return this.first_name;
	}

	public String getId()
	{
		return this.id;
	}

	public String getLast_name()
	{
		return this.last_name;
	}

	public String getRmpId()
	{
		return this.rmpId;
	}

	public ArrayList<School> getSchools()
	{
		return this.school;
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

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public void setRmpId(String rmpId)
	{
		this.rmpId = rmpId;
	}

	public void setSchools(ArrayList<School> school)
	{
		this.school = school;
	}

}
