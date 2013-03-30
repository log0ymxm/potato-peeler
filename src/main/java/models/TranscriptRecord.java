package models;

import database.Model;

public class TranscriptRecord extends Model
{

	private String comment;
	private float grade;
	private String id;
	private Class relatedClass;
	private Teacher teacher;
	private Transcript transcript;

	public String getComment()
	{
		return this.comment;
	}

	public float getGrade()
	{
		return this.grade;
	}

	public String getId()
	{
		return this.id;
	}

	public Class getRelatedClass()
	{
		return this.relatedClass;
	}

	public Teacher getTeacher()
	{
		return this.teacher;
	}

	public Transcript getTranscript()
	{
		return this.transcript;
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

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setGrade(float grade)
	{
		this.grade = grade;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setRelatedClass(Class relatedClass)
	{
		this.relatedClass = relatedClass;
	}

	public void setTeacher(Teacher teacher)
	{
		this.teacher = teacher;
	}

	public void setTranscript(Transcript transcript)
	{
		this.transcript = transcript;
	}

}
