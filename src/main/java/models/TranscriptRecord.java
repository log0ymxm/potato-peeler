package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBFactory;
import database.InvalidModelException;
import database.Model;

public class TranscriptRecord extends Model
{

	private String comment;
	private Float grade;
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
			String query = "SELECT transcripts.id, transcripts.is_predicted, " +
					"transcripts.`date`, transcript_records.id, " +
					"transcript_records.grade, transcript_records.teacher_id, " +
					"transcript_records.class_id, transcript_records.comment " +
					"FROM potato_peeler.transcript_records transcript_records " +
					"RIGHT OUTER JOIN potato_peeler.transcripts transcripts ON " +
					"(transcript_records.transcript_id = transcripts.id) LIMIT 1";
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

	public Float getGrade()
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
		String valid = null;
		if ( !this.isFresh() )
		{
			valid = ( this.getId() != null ) ? null
					: "An existing transcript record needs an ID";
		}
		// comment not required
		if ( valid == null )
		{
			valid = ( this.getGrade() != null ) ? null
					: "A transcript record needs a grade";
		}
		if ( valid == null )
		{
			valid = ( this.getRelatedClass().getId() != null ) ? null
					: "A transcript record's existing class needs needs an ID";
		}
		if ( valid == null )
		{
			valid = ( this.getTeacher().getId() != null ) ? null
					: "A transcript record's existing teacher needs an ID";
		}
		if ( valid == null )
		{
			valid = ( this.getTranscript().getId() != null ) ? null
					: "A transcript record's existing transcript needs an ID";
		}
		return valid;
	}

	@Override
	public boolean save() throws InvalidModelException
	{
		int result = 0;

		String error = this.isValid();
		if ( error == null )
		{
			Connection connection = DBFactory.getConnection();
			PreparedStatement insertNewTranscriptRecord = null;
			ResultSet resultSet = null;
			PreparedStatement selectId = null;
			if ( this.isFresh() )
			{
				String insertQuery = "INSERT INTO `potato_peeler`.`transcript_records` " +
						"(`id`,`transcript_id`,`grade`,`teacher_id`,`class_id`,`comment`) VALUES (?,?,?,?,?,?);";
				try
				{
					insertNewTranscriptRecord = connection.prepareStatement( insertQuery );
					insertNewTranscriptRecord.setString( 1, this.id );
					insertNewTranscriptRecord.setString( 2, this.transcript.getId() );
					insertNewTranscriptRecord.setFloat( 3, this.grade );
					insertNewTranscriptRecord.setString( 4, this.teacher.getId() );
					insertNewTranscriptRecord.setString( 5, this.relatedClass.getId() );
					insertNewTranscriptRecord.setString( 5, this.comment );
					result = insertNewTranscriptRecord.executeUpdate();
					if ( result > 0 )
					{
						String idQuery = "SELECT `transcript_records`.`id` FROM " +
								"`potato_peeler`.`transcript_records` WHERE " +
								"`transcript_records`.`transcript_id` LIKE ? " +
								"AND `transcript_records`.`grade` LIKE ? " +
								"AND `transcript_records`.`teacher_id` LIKE ? " +
								"AND `transcript_records`.`class_id` LIKE ? " +
								"AND `transcript_records`.`comment`LIKE ? LIMIT 1;";
						selectId = connection.prepareStatement( idQuery );
						selectId.setString( 1, this.id );
						selectId.setString( 2, this.transcript.getId() );
						selectId.setFloat( 3, this.grade );
						selectId.setString( 4, this.teacher.getId() );
						selectId.setString( 5, this.relatedClass.getId() );
						selectId.setString( 5, this.comment );
						resultSet = selectId.executeQuery();

						if ( resultSet.next() )
						{
							this.id = resultSet.getString( 1 );
							this.transcript.setId( this.id );
							this.dirty = false;
							return true;
						}
						else
						{
							System.err.println( "Error getting transcipt record ID" );
							return false;
						}
					}
				}
				catch ( SQLException e )
				{
					System.err.println( e.getSQLState() + ", "
							+ e.getErrorCode() );
					e.printStackTrace();
					System.exit( 1 );
				}
				finally
				{
					try
					{
						insertNewTranscriptRecord.close();
						selectId.close();
						resultSet.close();
						DBFactory.closeConnection( connection );
					}
					catch ( SQLException e )
					{
						e.printStackTrace();
						System.exit( 1 );
					}
				}

			}
			else
			{
				String updateQuery = "UPDATE `potato_peeler`.`transcript_records` SET " +
						"`id` = ?, " +
						"`transcript_id` = ?, " +
						"`grade` = ?, " +
						"`teacher_id` = ?, " +
						"`class_id` = ?, " +
						"`comment` = ?" +
						" WHERE `id`=?;";
				PreparedStatement updateTranscriptRecord = null;
				try
				{
					updateTranscriptRecord = connection.prepareStatement( updateQuery );
					updateTranscriptRecord.setString( 1, this.id );
					updateTranscriptRecord.setString( 2, this.transcript.getId() );
					updateTranscriptRecord.setFloat( 3, this.grade );
					updateTranscriptRecord.setString( 4, this.teacher.getId() );
					updateTranscriptRecord.setString( 5, this.relatedClass.getId() );
					updateTranscriptRecord.setString( 5, this.comment );
					result = updateTranscriptRecord.executeUpdate();
					if ( result > 0 )
					{
						this.dirty = false;
						return true;
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
						updateTranscriptRecord.close();
						DBFactory.closeConnection( connection );
					}
					catch ( SQLException e )
					{
						e.printStackTrace();
						System.exit( 1 );
					}
				}
			}
		}
		else
		{
			throw new InvalidModelException( error );
		}
		return false;
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
