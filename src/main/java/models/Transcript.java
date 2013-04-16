package models;

import java.util.ArrayList;
import java.util.Date;

import database.Model;

public class Transcript extends Model
{

	private Date date;

	private String id;
	private boolean isPredicted;

	protected Transcript(String id, Date date, Boolean isPredicted,
			Boolean dirty, Boolean fresh)
	{
		super("transcripts", new ArrayList<String>()
		{

			{
				this.add("id");
				this.add("date");
				this.add("is_predicted");
			}
		}, new ArrayList<String>()
		{

			{
			}
		}, dirty, fresh);
		// TODO Auto-generated constructor stub
		throw new UnsupportedOperationException();
	}

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
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean save()
	{
		// TODO
		throw new UnsupportedOperationException();
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
