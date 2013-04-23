package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBFactory;
import database.Model;

public class TranscriptRecord extends Model
{

	private String comment;
	private float grade;
	private String id;
	private SchoolClass relatedClass;
	private Teacher teacher;
	private Transcript transcript;

	public TranscriptRecord( String id, String comment, float grade,
			String relatedClass_id, String teacher_id, String transcript_id,
			Boolean dirty, Boolean fresh )
	{
		super( "transcript_records", new ArrayList< String >()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 6165318387983757596L;

			{
				this.add( "id" );
				this.add( "comment" );
				this.add( "grade" );
				this.add( "class_id" );
				this.add( "teacher_id" );
				this.add( "transcript_id" );
			}
		}, new ArrayList< String >()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5135859880364567556L;

			{
			}
		}, dirty, fresh );
		// TODO
		throw new UnsupportedOperationException();
	}

	public TranscriptRecord( ResultSet resultSet ) throws SQLException
	{
		this( resultSet.getString( "id" ),
				resultSet.getString( "comment" ),
				resultSet.getFloat( "grade" ),
				resultSet.getString( "class_id" ),
				resultSet.getString( "teacher_id" ),
				resultSet.getString( "transcript_id" ) );
	}

	public TranscriptRecord( String id, String comment, float grade,
			String relatedClass_id, String teacher_id, String transcript_id )
	{
		this( id, comment, grade, relatedClass_id, teacher_id, transcript_id, true, true );
	}

	public static TranscriptRecord findById( String id )
	{
		TranscriptRecord transcriptRecord = null;

		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT transcripts.id, transcripts.is_predicted, transcripts.`date`, transcript_records.id, transcript_records.grade, transcript_records.teacher_id, transcript_records.class_id, transcript_records.comment FROM potato_peeler.transcript_records transcript_records RIGHT OUTER JOIN potato_peeler.transcripts transcripts ON (transcript_records.transcript_id = transcripts.id) LIMIT 1";
			statement = connection.prepareStatement( query );
			statement.setString( 1, id );
			resultSet = statement.executeQuery();

			while ( resultSet.next() )
			{
				transcriptRecord = new TranscriptRecord( resultSet );
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
			System.exit( 1 );
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
				DBFactory.closeConnection( connection );
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
			}
		}
		return transcriptRecord;
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

	public void setComment( String comment )
	{
		this.comment = comment;
	}

	public void setGrade( float grade )
	{
		this.grade = grade;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public void setRelatedClass( SchoolClass relatedClass )
	{
		this.relatedClass = relatedClass;
	}

	public void setTeacher( Teacher teacher )
	{
		this.teacher = teacher;
	}

	public void setTranscript( Transcript transcript )
	{
		this.transcript = transcript;
	}

}
