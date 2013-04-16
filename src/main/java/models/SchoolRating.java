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

public class SchoolRating extends Model
{

	private float campusGrounds;
	private float campusLocation;
	private float careerOpportunities;
	private float clubsAndEvents;
	private String comment;
	private float conditionOfLibrary;
	private Date date;
	private String id;
	private float internetSpeed;
	private float qualityOfFood;
	private String rmpId;
	private School school;
	private float schoolHappiness;
	private String schoolId;
	private float schoolReputation;
	private float socialActivities;

	public SchoolRating(ResultSet resultSet) throws SQLException
	{
		this(resultSet.getString("id"), resultSet.getString("rmp_id"),
				resultSet.getString("school_id"), resultSet.getDate("date"),
				resultSet.getFloat("school_reputation"), resultSet
						.getFloat("career_opportunities"), resultSet
						.getFloat("campus_grounds"), resultSet
						.getFloat("quality_of_food"), resultSet
						.getFloat("social_activities"), resultSet
						.getFloat("campus_location"), resultSet
						.getFloat("condition_of_library"), resultSet
						.getFloat("internet_speed"), resultSet
						.getFloat("clubs_and_events"), resultSet
						.getString("comment"), resultSet
						.getFloat("school_happiness"), false, false);
	}

	public SchoolRating(String rmp_id, String school_id, Date date,
			float school_reputation, float career_opportunities,
			float campus_grounds, float quality_of_food,
			float social_activities, float campus_location,
			float condition_of_library, float internet_speed,
			float clubs_and_events, String comment, float school_happiness)
	{
		this(null, rmp_id, school_id, date, school_reputation,
				career_opportunities, campus_grounds, quality_of_food,
				social_activities, campus_location, condition_of_library,
				internet_speed, clubs_and_events, comment, school_happiness,
				true, true);
	}

	public SchoolRating(String id, String rmp_id, String school_id, Date date,
			float school_reputation, float career_opportunities,
			float campus_grounds, float quality_of_food,
			float social_activities, float campus_location,
			float condition_of_library, float internet_speed,
			float clubs_and_events, String comment, float school_happiness,
			Boolean dirty, Boolean fresh)
	{
		super("school_ratings", new ArrayList<String>()
		{

			{
				this.add("id");
				this.add("campus_grounds");
				this.add("campus_location");
				this.add("career_opportunities");
				this.add("clubs_and_events");
				this.add("comment");
				this.add("condition_of_library");
				this.add("date");
				this.add("internet_speed");
				this.add("quality_of_food");
				this.add("rmp_id");
				this.add("school_id");
				this.add("school_reputation");
				this.add("social_activities");
				this.add("school_happiness");
			}
		}, new ArrayList<String>()
		{

			{
			}
		}, dirty, fresh);
		this.setRmpId(rmp_id);
		this.setSchoolId(school_id);
		this.setDate(date);
		this.setSchoolReputation(school_reputation);
		this.setCareerOpportunities(career_opportunities);
		this.setCampusGrounds(campus_grounds);
		this.setQualityOfFood(quality_of_food);
		this.setSocialActivities(social_activities);
		this.setCampusLocation(campus_location);
		this.setConditionOfLibrary(condition_of_library);
		this.setInternetSpeed(internet_speed);
		this.setClubsAndEvents(clubs_and_events);
		this.setComment(comment);
		this.setSchoolHappiness(school_happiness);
	}

	public static SchoolRating findOrCreate(String rmp_id, School school,
			String dateString, String school_reputationString,
			String career_opportunitiesString, String campus_groundsString,
			String quality_of_foodString, String social_activitiesString,
			String campus_locationString, String condition_of_libraryString,
			String internet_speedString, String clubs_and_eventsString,
			String comment, String school_happinessString)
			throws InvalidModelException
	{
		String findQuery = "SELECT id, rmp_id, school_id, date, school_reputation, "
				+ "career_opportunities, campus_grounds, quality_of_food, social_activities, "
				+ "campus_location, condition_of_library, internet_speed, clubs_and_events, "
				+ "comment, school_happiness "
				+ "FROM school_ratings WHERE "
				+ "rmp_id = ? LIMIT 1";
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
				return new SchoolRating(resultSet);
			}
			else
			{
				DateFormat df = new SimpleDateFormat("MM/DD/YYYY");
				Date date = df.parse(dateString);
				float school_reputation = Float
						.parseFloat(school_reputationString);
				float career_opportunities = Float
						.parseFloat(career_opportunitiesString);
				float campus_grounds = Float.parseFloat(campus_groundsString);
				float quality_of_food = Float.parseFloat(quality_of_foodString);
				float social_activities = Float
						.parseFloat(social_activitiesString);
				float campus_location = Float.parseFloat(campus_locationString);
				float condition_of_library = Float
						.parseFloat(condition_of_libraryString);
				float internet_speed = Float.parseFloat(internet_speedString);
				float clubs_and_events = Float
						.parseFloat(clubs_and_eventsString);
				float school_happiness = Float
						.parseFloat(school_happinessString);

				SchoolRating schoolRating = new SchoolRating(rmp_id,
						school.getId(), date, school_reputation,
						career_opportunities, campus_grounds, quality_of_food,
						social_activities, campus_location,
						condition_of_library, internet_speed, clubs_and_events,
						comment, school_happiness);
				schoolRating.save();
				return schoolRating;
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

	public float getCampusGrounds()
	{
		return this.campusGrounds;
	}

	public float getCampusLocation()
	{
		return this.campusLocation;
	}

	public float getCareerOpportunities()
	{
		return this.careerOpportunities;
	}

	public float getClubsAndEvents()
	{
		return this.clubsAndEvents;
	}

	public String getComment()
	{
		return this.comment;
	}

	public float getConditionOfLibrary()
	{
		return this.conditionOfLibrary;
	}

	public Date getDate()
	{
		return this.date;
	}

	public String getId()
	{
		return this.id;
	}

	public float getInternetSpeed()
	{
		return this.internetSpeed;
	}

	public float getQualityOfFood()
	{
		return this.qualityOfFood;
	}

	public String getRmpId()
	{
		return this.rmpId;
	}

	public School getSchool()
	{
		return this.school;
	}

	public float getSchoolHappiness()
	{
		return this.schoolHappiness;
	}

	public String getSchoolId()
	{
		return this.schoolId;
	}

	public float getSchoolReputation()
	{
		return this.schoolReputation;
	}

	public float getSocialActivities()
	{
		return this.socialActivities;
	}

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing school rating requires an ID";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getCampusGrounds())) ? null
					: "A school rating requires a campus grounds rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getCampusLocation())) ? null
					: "A school rating requires a campus location rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getCareerOpportunities())) ? null
					: "A school rating requires a career opportunities rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getClubsAndEvents())) ? null
					: "A school rating requires a clubs and events rating";
		}
		if (valid == null)
		{
			valid = (this.getComment() != null) ? null
					: "A school rating requires a comment";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getConditionOfLibrary())) ? null
					: "A school rating requires a condition of library rating";
		}
		if (valid == null)
		{
			valid = (this.getDate() != null) ? null
					: "A school rating requires a date";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getInternetSpeed())) ? null
					: "A school rating requires an internet speed rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getQualityOfFood())) ? null
					: "A school rating requires a quality of food rating";
		}
		if (valid == null)
		{
			valid = (this.getRmpId() != null) ? null
					: "A school rating requires an rmp id";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getSchoolHappiness())) ? null
					: "A school rating requires a school happiness rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getSchoolReputation())) ? null
					: "A school rating requires a school reputation rating";
		}
		if (valid == null)
		{
			valid = (!Float.isNaN(this.getSocialActivities())) ? null
					: "A school rating requires a social activities rating";
		}

		if (valid == null)
		{
			valid = (this.getSchoolId() != null) ? null
					: "A school rating rating requires a school_id";
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
			PreparedStatement insertNewSchool = null;
			PreparedStatement selectId = null;
			ResultSet resultSet = null;
			if (this.isFresh())
			{
				String insertQuery = "INSERT INTO school_ratings (campus_grounds, campus_location, career_opportunities, "
						+ "clubs_and_events, comment, condition_of_library, "
						+ "date, internet_speed, quality_of_food, "
						+ "rmp_id, school_id, school_happiness, "
						+ "school_reputation, social_activities) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				try
				{
					insertNewSchool = connection.prepareStatement(insertQuery);
					insertNewSchool.setFloat(1, this.getCampusGrounds());
					insertNewSchool.setFloat(2, this.getCampusLocation());
					insertNewSchool.setFloat(3, this.getCareerOpportunities());
					insertNewSchool.setFloat(4, this.getClubsAndEvents());
					insertNewSchool.setString(5, this.getComment());
					insertNewSchool.setFloat(6, this.getConditionOfLibrary());
					insertNewSchool.setDate(7, new java.sql.Date(this.getDate()
							.getTime()));
					insertNewSchool.setFloat(8, this.getInternetSpeed());
					insertNewSchool.setFloat(9, this.getQualityOfFood());
					insertNewSchool.setString(10, this.getRmpId());
					insertNewSchool.setString(11, this.getSchoolId());
					insertNewSchool.setFloat(12, this.getSchoolHappiness());
					insertNewSchool.setFloat(13, this.getSchoolReputation());
					insertNewSchool.setFloat(14, this.getSocialActivities());

					result = insertNewSchool.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT id FROM school_ratings WHERE "
								+ "campus_grounds = ? AND campus_location = ? AND career_opportunities = ? AND "
								+ "clubs_and_events = ? AND comment like ? AND condition_of_library = ? AND "
								+ "date = ? AND internet_speed = ? AND quality_of_food = ? AND "
								+ "rmp_id = ? AND school_id = ? AND school_happiness = ? AND "
								+ "school_reputation = ? AND social_activities = ? LIMIT 1";
						selectId = connection.prepareStatement(idQuery);
						selectId.setFloat(1, this.getCampusGrounds());
						selectId.setFloat(2, this.getCampusLocation());
						selectId.setFloat(3, this.getCareerOpportunities());
						selectId.setFloat(4, this.getClubsAndEvents());
						selectId.setString(5, this.getComment());
						selectId.setFloat(6, this.getConditionOfLibrary());
						selectId.setDate(7, new java.sql.Date(this.getDate()
								.getTime()));
						selectId.setFloat(8, this.getInternetSpeed());
						selectId.setFloat(9, this.getQualityOfFood());
						selectId.setString(10, this.getRmpId());
						selectId.setString(11, this.getSchoolId());
						selectId.setFloat(12, this.getSchoolHappiness());
						selectId.setFloat(13, this.getSchoolReputation());
						selectId.setFloat(14, this.getSocialActivities());

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
						insertNewSchool.close();
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
				String updateQuery = "UPDATE school_ratings SET "
						+ "campus_grounds=?, campus_locations=?, career_opportunities=?, "
						+ "clubs_and_events=?, comment=?, condition_of_library=?, "
						+ "date=?, internet_speed=?, quality_of_food=?, "
						+ "rmp_id=?, school_id=?, school_happiness=?, "
						+ "school_reputation=?, social_activities=? WHERE id=?";
				PreparedStatement updateSchool = null;
				try
				{
					updateSchool = connection.prepareStatement(updateQuery);
					updateSchool.setFloat(1, this.getCampusGrounds());
					updateSchool.setFloat(2, this.getCampusLocation());
					updateSchool.setFloat(3, this.getCareerOpportunities());
					updateSchool.setFloat(4, this.getClubsAndEvents());
					updateSchool.setString(5, this.getComment());
					updateSchool.setFloat(6, this.getConditionOfLibrary());
					updateSchool.setDate(7, new java.sql.Date(this.getDate()
							.getTime()));
					updateSchool.setFloat(8, this.getInternetSpeed());
					updateSchool.setFloat(9, this.getQualityOfFood());
					updateSchool.setString(10, this.getRmpId());
					updateSchool.setString(11, this.getSchoolId());
					updateSchool.setFloat(12, this.getSchoolHappiness());
					updateSchool.setFloat(13, this.getSchoolReputation());
					updateSchool.setFloat(14, this.getSocialActivities());
					updateSchool.setString(15, this.getId());

					result = updateSchool.executeUpdate();
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
						updateSchool.close();
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

	public void setCampusGrounds(float campusGrounds)
	{
		this.campusGrounds = campusGrounds;
	}

	public void setCampusLocation(float campusLocation)
	{
		this.campusLocation = campusLocation;
	}

	public void setCareerOpportunities(float careerOpportunities)
	{
		this.careerOpportunities = careerOpportunities;
	}

	public void setClubsAndEvents(float clubsAndEvents)
	{
		this.clubsAndEvents = clubsAndEvents;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setConditionOfLibrary(float conditionOfLibrary)
	{
		this.conditionOfLibrary = conditionOfLibrary;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setInternetSpeed(float internetSpeed)
	{
		this.internetSpeed = internetSpeed;
	}

	public void setQualityOfFood(float qualityOfFood)
	{
		this.qualityOfFood = qualityOfFood;
	}

	public void setRmpId(String rmpId)
	{
		this.rmpId = rmpId;
	}

	public void setSchool(School school)
	{
		this.setSchoolId(school.getId());
		this.school = school;
	}

	public void setSchoolHappiness(float schoolHappiness)
	{
		this.schoolHappiness = schoolHappiness;
	}

	public void setSchoolId(String schoolId)
	{
		this.schoolId = schoolId;
	}

	public void setSchoolReputation(float schoolReputation)
	{
		this.schoolReputation = schoolReputation;
	}

	public void setSocialActivities(float socialActivities)
	{
		this.socialActivities = socialActivities;
	}

}
