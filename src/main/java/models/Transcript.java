package models;

import java.util.Date;

import database.Model;

public class Transcript extends Model
{

	private Date date;
	private String id;
	private boolean isPredicted;

	public Date getDate()
	{
		return this.date;
	}

	public String getId()
	{
		return this.id;
	}

	public boolean isPredicted()
	{
		return this.isPredicted;
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

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setPredicted(boolean isPredicted)
	{
		this.isPredicted = isPredicted;
	}

}
