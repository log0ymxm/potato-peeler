package models;

import java.util.ArrayList;

import database.Model;

public class TranscriptRecord extends Model
{

	private String comment;
	private float grade;
	private String id;
	private SchoolClass relatedClass;
	private Teacher teacher;
	private Transcript transcript;

	public TranscriptRecord(String id, String comment, float grade,
			String relatedClass_id, String teacher_id, String transcript_id,
			Boolean dirty, Boolean fresh)
	{
		super("transcript_records", new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 6165318387983757596L;

			{
				this.add("id");
				this.add("comment");
				this.add("grade");
				this.add("class_id");
				this.add("teacher_id");
				this.add("transcript_id");
			}
		}, new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5135859880364567556L;

			{
			}
		}, dirty, fresh);
		// TODO
		throw new UnsupportedOperationException();
	}

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

	public SchoolClass getRelatedClass()
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

	public void setRelatedClass(SchoolClass relatedClass)
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
