package models;

import java.util.Date;

import database.Model;

public class TeacherRating extends Model
{

	private float clarity;
	private String comment;
	private Date date;
	private float easiness;
	private float helpfulness;
	private String id;
	private float raterInterest;
	private Class relatedClass;
	private String rmpId;
	private Teacher teacher;

	public float getClarity()
	{
		return this.clarity;
	}

	public String getComment()
	{
		return this.comment;
	}

	public Date getDate()
	{
		return this.date;
	}

	public float getEasiness()
	{
		return this.easiness;
	}

	public float getHelpfulness()
	{
		return this.helpfulness;
	}

	public String getId()
	{
		return this.id;
	}

	public float getRaterInterest()
	{
		return this.raterInterest;
	}

	public Class getRelatedClass()
	{
		return this.relatedClass;
	}

	public String getRmpId()
	{
		return this.rmpId;
	}

	public Teacher getTeacher()
	{
		return this.teacher;
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

	public void setClarity(float clarity)
	{
		this.clarity = clarity;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setEasiness(float easiness)
	{
		this.easiness = easiness;
	}

	public void setHelpfulness(float helpfulness)
	{
		this.helpfulness = helpfulness;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setRaterInterest(float raterInterest)
	{
		this.raterInterest = raterInterest;
	}

	public void setRelatedClass(Class relatedClass)
	{
		this.relatedClass = relatedClass;
	}

	public void setRmpId(String rmpId)
	{
		this.rmpId = rmpId;
	}

	public void setTeacher(Teacher teacher)
	{
		this.teacher = teacher;
	}

}
