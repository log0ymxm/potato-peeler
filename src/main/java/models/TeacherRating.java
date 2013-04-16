package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import database.DBFactory;
import database.InvalidModelException;
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
	private SchoolClass relatedClass;
	private String relatedClassId;
	private String rmpId;
	private Teacher teacher;
	private String teacherId;

	public TeacherRating(float clarity, String comment, Date date,
			float easiness, float helpfulness, float rater_interest,
			String classRelation_id, String rmp_id, String teacher_id)
	{
		this(null, clarity, comment, date, easiness, helpfulness,
				rater_interest, classRelation_id, rmp_id, teacher_id, true,
				true);
	}

	public TeacherRating(ResultSet resultSet) throws SQLException
	{
		this(resultSet.getString("id"), resultSet.getFloat("clarity"),
				resultSet.getString("comment"), resultSet.getDate("date"),
				resultSet.getFloat("easiness"), resultSet
						.getFloat("helpfulness"), resultSet
						.getFloat("rater_interest"), resultSet
						.getString("class_id"), resultSet.getString("rmp_id"),
				resultSet.getString("teacher_id"), false, false);

	}

	public TeacherRating(String id, float clarity, String comment, Date date,
			float easiness, float helpfulness, float rater_interest,
			String classRelation_id, String rmp_id, String teacher_id,
			Boolean dirty, Boolean fresh)
	{
		super("teacher_ratings", new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 4189393430864405625L;

			{
				this.add("id");
				this.add("clarity");
				this.add("comment");
				this.add("date");
				this.add("easiness");
				this.add("helpfulness");
				this.add("rater_interest");
				this.add("class_id");
				this.add("rmp_id");
				this.add("teacher_id");
			}
		}, new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 5262359969608239074L;

			{
			}
		}, dirty, fresh);
		this.setClarity(clarity);
		this.setComment(comment);
		this.setDate(date);
		this.setEasiness(easiness);
		this.setHelpfulness(helpfulness);
		this.setRaterInterest(rater_interest);
		this.setRelatedClassId(classRelation_id);
		this.setRmpId(rmp_id);
		this.setTeacherId(teacher_id);
	}

	public static TeacherRating findOrCreate(Teacher teacher,
			SchoolClass classRelation, String rmp_id, String easinessString,
			String helpfulnessString, String clarityString,
			String rater_interestString, String comment, String dateString)
			throws InvalidModelException
	{
		String findQuery = "SELECT id, teacher_id, class_id, "
				+ "rmp_id, easiness, helpfulness, "
				+ "clarity, rater_interest, comment, date "
				+ "FROM teacher_ratings WHERE " + "rmp_id = ? LIMIT 1";
		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
			statement = connection.prepareStatement(findQuery);
			statement.setString(1, rmp_id);

			resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				return new TeacherRating(resultSet);
			}
			else
			{
				// inconsistent date format
				DateFormat df = new SimpleDateFormat("MM/DD/YY");
				Date date = df.parse(dateString);

				float clarity = TeacherRating.getFloat(clarityString);
				float easiness = TeacherRating.getFloat(easinessString);
				float helpfulness = TeacherRating.getFloat(helpfulnessString);
				float rater_interest = TeacherRating
						.getFloat(rater_interestString);

				TeacherRating teacherRating = new TeacherRating(clarity,
						comment, date, easiness, helpfulness, rater_interest,
						classRelation.getId(), rmp_id, teacher.getId());
				teacherRating.save();
				return teacherRating;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (ParseException e)
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

	private static float getFloat(String rating)
	{
		try
		{
			return Float.parseFloat(rating);
		}
		catch (Exception e)
		{
			return 0;
		}
	}

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

	public SchoolClass getRelatedClass()
	{
		return this.relatedClass;
	}

	public String getRelatedClassId()
	{
		return this.relatedClassId;
	}

	public String getRmpId()
	{
		return this.rmpId;
	}

	public Teacher getTeacher()
	{
		return this.teacher;
	}

	public String getTeacherId()
	{
		return this.teacherId;
	}

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing teacher rating requires an ID";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getClarity())) ? null
					: "A teacher rating requires a clarity rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getEasiness())) ? null
					: "A teacher rating requires a easiness rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getHelpfulness())) ? null
					: "A teacher rating requires a helpfulness rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getRaterInterest())) ? null
					: "A teacher rating requires a rater interest rating";
		}
		if (valid == null)
		{
			valid = (this.getComment() != null) ? null
					: "A teacher rating requires a comment";
		}
		if (valid == null)
		{
			valid = (this.getDate() != null) ? null
					: "A teacher rating requires a date";
		}
		if (valid == null)
		{
			valid = (this.getRmpId() != null) ? null
					: "A teacher rating requires an rmp id";
		}
		if (valid == null)
		{
			valid = (this.getRelatedClassId() != null) ? null
					: "A teacher rating rating requires a class_id";
		}
		if (valid == null)
		{
			valid = (this.getTeacherId() != null) ? null
					: "A teacher rating rating requires a teacher_id";
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
			PreparedStatement insertNewTeacherRating = null;
			PreparedStatement selectId = null;
			ResultSet resultSet = null;
			if (this.isFresh())
			{
				String insertQuery = "INSERT INTO teacher_ratings ("
						+ "clarity, comment, date, "
						+ "easiness, helpfulness, rater_interest, "
						+ "class_id, rmp_id, teacher_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				try
				{
					insertNewTeacherRating = connection
							.prepareStatement(insertQuery);
					insertNewTeacherRating.setFloat(1, this.getClarity());
					insertNewTeacherRating.setString(2, this.getComment());
					insertNewTeacherRating.setDate(3, new java.sql.Date(this
							.getDate().getTime()));
					insertNewTeacherRating.setFloat(4, this.getEasiness());
					insertNewTeacherRating.setFloat(5, this.getHelpfulness());
					insertNewTeacherRating.setFloat(6, this.getRaterInterest());
					insertNewTeacherRating.setString(7,
							this.getRelatedClassId());
					insertNewTeacherRating.setString(8, this.getRmpId());
					insertNewTeacherRating.setString(9, this.getTeacherId());

					result = insertNewTeacherRating.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT id FROM teacher_ratings WHERE "
								+ "clarity=? AND comment like ? AND date=? AND "
								+ "easiness=? AND helpfulness=? AND rater_interest=? AND "
								+ "class_id=? AND rmp_id=? AND teacher_id=? LIMIT 1";
						selectId = connection.prepareStatement(idQuery);
						selectId.setFloat(1, this.getClarity());
						selectId.setString(2, this.getComment());
						selectId.setDate(3, new java.sql.Date(this.getDate()
								.getTime()));
						selectId.setFloat(4, this.getEasiness());
						selectId.setFloat(5, this.getHelpfulness());
						selectId.setFloat(6, this.getRaterInterest());
						selectId.setString(7, this.getRelatedClassId());
						selectId.setString(8, this.getRmpId());
						selectId.setString(9, this.getTeacherId());

						resultSet = selectId.executeQuery();
						if (resultSet.next())
						{
							String id = resultSet.getString(1);
							System.out.println("save get id: " + id);
							this.setId(id);
							this.fresh = false;
							this.dirty = false;
							return true;
						}
						else
						{
							return false;
						}
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
						insertNewTeacherRating.close();
						selectId.close();
						resultSet.close();
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
				String updateQuery = "UPDATE teacher_ratings SET "
						+ "clarity=?, comment like ?, date=?, "
						+ "easiness=?, helpfulness=?, rater_interest=?, "
						+ "class_id=?, rmp_id=?, teacher_id=? WHERE id=?";
				PreparedStatement updateTeacherRating = null;
				try
				{
					updateTeacherRating = connection
							.prepareStatement(updateQuery);
					updateTeacherRating.setFloat(1, this.getClarity());
					updateTeacherRating.setString(2, this.getComment());
					updateTeacherRating.setDate(3, new java.sql.Date(this
							.getDate().getTime()));
					updateTeacherRating.setFloat(4, this.getEasiness());
					updateTeacherRating.setFloat(5, this.getHelpfulness());
					updateTeacherRating.setFloat(6, this.getRaterInterest());
					updateTeacherRating.setString(7, this.getRelatedClassId());
					updateTeacherRating.setString(8, this.getRmpId());
					updateTeacherRating.setString(9, this.getTeacherId());
					updateTeacherRating.setString(10, this.getId());

					result = updateTeacherRating.executeUpdate();
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
						updateTeacherRating.close();
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

	public void setRelatedClass(SchoolClass relatedClass)
	{
		this.setRelatedClassId(relatedClass.getId());
		this.relatedClass = relatedClass;
	}

	public void setRelatedClassId(String relatedClassId)
	{
		this.relatedClassId = relatedClassId;
	}

	public void setRmpId(String rmpId)
	{
		this.rmpId = rmpId;
	}

	public void setTeacher(Teacher teacher)
	{
		this.setTeacherId(teacher.getId());
		this.teacher = teacher;
	}

	public void setTeacherId(String teacherId)
	{
		this.teacherId = teacherId;
	}

}
