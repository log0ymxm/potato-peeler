package models;

import java.util.ArrayList;

import database.Model;

public class TeacherFeedback extends Model
{

	private String id;

	protected TeacherFeedback(String id, Boolean dirty, Boolean fresh)
	{
		super("teacher_feedback", new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 754742641530750087L;

			{
			}
		}, new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -9196697640452689340L;

			{
			}
		}, dirty, fresh);
		this.setId(id);
		// TODO we aren't scraping or using this yet, there probably aren't that
		// many to scrape
		throw new UnsupportedOperationException();
	}

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
