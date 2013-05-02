package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import database.DBFactory;
import database.InvalidModelException;
import database.Model;

public class Transcript extends Model
{

	private Date date;

	private String id;
	private boolean isPredicted;

	public Transcript()
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
		}, true, true);
	}

	public Transcript(ResultSet resultSet) throws SQLException
	{
		this(resultSet.getString("id"), resultSet.getDate("date"), resultSet
				.getBoolean("isPredicted"), false, false);
	}

	protected Transcript(String id, Date date, Boolean isPredicted,
			Boolean dirty, Boolean fresh)
	{
		super("transcripts", new ArrayList<String>()
		{

			private static final long serialVersionUID = -7904643137101173617L;

			{
				this.add("id");
				this.add("date");
				this.add("is_predicted");
			}
		}, new ArrayList<String>()
		{

			private static final long serialVersionUID = 6517481081926596080L;

			{
			}
		}, dirty, fresh);
		// TODO Auto-generated constructor stub
		throw new UnsupportedOperationException();
	}

	public static Transcript findById(String id)
	{
		Transcript transcript = null;

		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT id, is_predicted, date FROM transcripts WHERE id = ? LIMIT 1";
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				transcript = new Transcript(resultSet);
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
		return transcript;
	}

	public static Transcript findOrCreate() throws InvalidModelException
	{
		Transcript transcript = new Transcript();
		transcript.save();
		return transcript;
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
		return null;
	}

	@Override
	public boolean save() throws InvalidModelException
	{
		int result = 0;

		String error = this.isValid();
		if (error == null)
		{
			Connection connection = DBFactory.getConnection();
			PreparedStatement insertNewTeacher = null;
			ResultSet resultSet = null;
			PreparedStatement selectId = null;
			if (this.isFresh())
			{
				String insertQuery = "INSERT INTO transcripts (date, is_predicted) VALUES (?, ?)";
				try
				{
					insertNewTeacher = connection.prepareStatement(insertQuery);
					Date date = new Date();
					Boolean predicted = true;
					insertNewTeacher.setDate(1,
							new java.sql.Date(date.getTime()));
					insertNewTeacher.setBoolean(2, predicted);
					result = insertNewTeacher.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT id FROM transcripts WHERE date = ? AND is_predicted=? LIMIT 1";
						selectId = connection.prepareStatement(idQuery);
						selectId.setDate(1, new java.sql.Date(date.getTime()));
						selectId.setBoolean(2, predicted);
						resultSet = selectId.executeQuery();

						if (resultSet.next())
						{
							this.id = resultSet.getString(1);
							this.setId(this.id);
							this.fresh = false;
							this.dirty = false;
							return true;
						}
						else
						{
							System.err.println("Error getting class ID");
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
						insertNewTeacher.close();
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
				return false;
			}
		}
		else
		{
			throw new InvalidModelException(error);
		}
		return false;
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
