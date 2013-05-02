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
	private String relatedClassId;
	private String schoolId;
	private String stateId;
	private Teacher teacher;
	private String teacherId;
	private Transcript transcript;
	private String transcriptId;

	public TranscriptRecord(ResultSet resultSet) throws SQLException
	{
		this(resultSet.getString("id"), resultSet.getString("comment"),
				resultSet.getFloat("grade"), resultSet.getString("class_id"),
				resultSet.getString("teacher_id"), resultSet
						.getString("transcript_id"));
	}

	public TranscriptRecord(String id, String comment, float grade,
			String relatedClass_id, String teacher_id, String transcript_id)
	{
		this(id, comment, grade, relatedClass_id, teacher_id, transcript_id,
				null, null, true, true);
	}

	public TranscriptRecord(String id, String comment, float grade,
			String relatedClass_id, String teacher_id, String transcript_id,
			String state_id, String school_id, Boolean dirty, Boolean fresh)
	{
		super("transcript_records", new ArrayList<String>()
		{

			{
				this.add("id");
				this.add("comment");
				this.add("grade");
				this.add("class_id");
				this.add("teacher_id");
				this.add("transcript_id");
				this.add("school_id");
				this.add("state_id");
			}
		}, new ArrayList<String>()
		{

			{
			}
		}, dirty, fresh);

		System.out.println("--- t_id: " + transcript_id);
		this.setId(id);
		this.setComment(comment);
		this.setGrade(grade);
		this.setRelatedClassId(relatedClass_id);
		this.setTeacherId(teacher_id);
		this.setTranscriptId(transcript_id);
		this.setStateId(state_id);
		this.setSchoolId(school_id);
	}

	public TranscriptRecord(String transcript_id, String state_id,
			String school_id, String class_id)
	{
		this(null, null, 0, class_id, null, transcript_id, state_id, school_id,
				true, true);
	}

	public static TranscriptRecord findById(String id)
	{
		TranscriptRecord transcriptRecord = null;

		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
			String query = "SELECT transcripts.id, transcripts.is_predicted, "
					+ "transcripts.`date`, transcript_records.id, "
					+ "transcript_records.grade, transcript_records.teacher_id, "
					+ "transcript_records.class_id, transcript_records.comment "
					+ "FROM potato_peeler.transcript_records transcript_records "
					+ "RIGHT OUTER JOIN potato_peeler.transcripts transcripts ON "
					+ "(transcript_records.transcript_id = transcripts.id) LIMIT 1";
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				transcriptRecord = new TranscriptRecord(resultSet);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
				DBFactory.closeConnection(connection);
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
		return transcriptRecord;
	}

	public static TranscriptRecord findOrCreate(String transcript_id,
			String state_id, String school_id, String class_id)
	{
		String findQuery = "SELECT id, comment, grade, teacher_id, class_id, transcript_id, school_id, state_id FROM transcript_records WHERE "
				+ "transcript_id=? AND state_id=? AND school_id=? AND class_id=? LIMIT 1";
		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{

			statement = connection.prepareStatement(findQuery);
			statement.setString(1, transcript_id);
			statement.setString(2, state_id);
			statement.setString(3, school_id);
			statement.setString(4, class_id);
			resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				// System.out.println("School already exists");
				// return new School(resultSet);
			}
			else
			{
				TranscriptRecord record = new TranscriptRecord(transcript_id,
						state_id, school_id, class_id);
				record.save();
				return record;
			}
		}
		catch (SQLException | InvalidModelException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		finally
		{
			try
			{
				statement.close();
				resultSet.close();
				DBFactory.closeConnection(connection);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		return null;
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

	public String getRelatedClassId()
	{
		return this.relatedClassId;
	}

	public String getSchoolId()
	{
		return this.schoolId;
	}

	public String getStateId()
	{
		return this.stateId;
	}

	public Teacher getTeacher()
	{
		return this.teacher;
	}

	public String getTeacherId()
	{
		return this.teacherId;
	}

	public Transcript getTranscript()
	{
		return this.transcript;
	}

	public String getTranscriptId()
	{
		return this.transcriptId;
	}

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing transcript record needs an ID";
		}
		return valid;
	}

	@Override
	public boolean save() throws InvalidModelException
	{
		int result = 0;

		String error = this.isValid();
		if (error == null)
		{
			Connection connection = DBFactory.getConnection();
			PreparedStatement insertNewTranscriptRecord = null;
			ResultSet resultSet = null;
			PreparedStatement selectId = null;
			if (this.isFresh())
			{
				String insertQuery = "INSERT INTO `potato_peeler`.`transcript_records` "
						+ "(`transcript_id`,`grade`,`teacher_id`,`class_id`,`comment`, school_id, state_id) VALUES (?,?,?,?,?,?,?);";
				try
				{
					insertNewTranscriptRecord = connection
							.prepareStatement(insertQuery);
					insertNewTranscriptRecord.setString(1,
							this.getTranscriptId());
					insertNewTranscriptRecord.setFloat(2, this.getGrade());
					insertNewTranscriptRecord.setString(3, this.getTeacherId());
					insertNewTranscriptRecord.setString(4,
							this.getRelatedClassId());
					insertNewTranscriptRecord.setString(5, this.getComment());
					insertNewTranscriptRecord.setString(6, this.getSchoolId());
					insertNewTranscriptRecord.setString(7, this.getStateId());
					System.out.println("--- school id: " + this.getSchoolId());
					result = insertNewTranscriptRecord.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT `transcript_records`.`id` FROM "
								+ "`potato_peeler`.`transcript_records` WHERE "
								+ "`transcript_records`.`transcript_id` LIKE ? "
								+ "AND `transcript_records`.`class_id` LIKE ? "
								+ "LIMIT 1;";
						selectId = connection.prepareStatement(idQuery);
						selectId.setString(1, this.getId());
						selectId.setString(2, this.getTranscriptId());
						selectId.setString(3, this.getRelatedClassId());
						resultSet = selectId.executeQuery();

						if (resultSet.next())
						{
							this.id = resultSet.getString(1);
							this.transcript.setId(this.id);
							this.dirty = false;
							return true;
						}
						else
						{
							System.err
									.println("Error getting transcipt record ID");
							return false;
						}
					}
				}
				catch (SQLException e)
				{
					System.err.println(e.getSQLState() + ", "
							+ e.getErrorCode());
					e.printStackTrace();
					System.exit(1);
				}
				finally
				{
					try
					{
						insertNewTranscriptRecord.close();
						if (selectId != null)
						{
							selectId.close();
						}
						if (resultSet != null)
						{
							resultSet.close();
						}
						DBFactory.closeConnection(connection);
					}
					catch (SQLException e)
					{
						e.printStackTrace();
						System.exit(1);
					}
				}

			}
			else
			{
				String updateQuery = "UPDATE `potato_peeler`.`transcript_records` SET "
						+ "`id` = ?, "
						+ "`transcript_id` = ?, "
						+ "`grade` = ?, "
						+ "`teacher_id` = ?, "
						+ "`class_id` = ?, "
						+ "`comment` = ?"
						+ " WHERE `id`=?;";
				PreparedStatement updateTranscriptRecord = null;
				try
				{
					updateTranscriptRecord = connection
							.prepareStatement(updateQuery);
					updateTranscriptRecord.setString(1, this.id);
					updateTranscriptRecord
							.setString(2, this.transcript.getId());
					updateTranscriptRecord.setFloat(3, this.grade);
					updateTranscriptRecord.setString(4, this.teacher.getId());
					updateTranscriptRecord.setString(5,
							this.relatedClass.getId());
					updateTranscriptRecord.setString(5, this.comment);
					result = updateTranscriptRecord.executeUpdate();
					if (result > 0)
					{
						this.dirty = false;
						return true;
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					System.exit(1);
				}
				finally
				{
					try
					{
						updateTranscriptRecord.close();
						DBFactory.closeConnection(connection);
					}
					catch (SQLException e)
					{
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		}
		else
		{
			throw new InvalidModelException(error);
		}
		return false;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setGrade(float grade)
	{
		this.grade = grade;
	}

	public void setGrade(Float grade)
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

	public void setRelatedClassId(String relatedClassId)
	{
		this.relatedClassId = relatedClassId;
	}

	public void setSchoolId(String schoolId)
	{
		this.schoolId = schoolId;
	}

	public void setStateId(String stateId)
	{
		this.stateId = stateId;
	}

	public void setTeacher(Teacher teacher)
	{
		this.teacher = teacher;
	}

	public void setTeacherId(String teacherId)
	{
		this.teacherId = teacherId;
	}

	public void setTranscript(Transcript transcript)
	{
		this.transcript = transcript;
	}

	public void setTranscriptId(String transcriptId)
	{
		this.transcriptId = transcriptId;
	}

}
