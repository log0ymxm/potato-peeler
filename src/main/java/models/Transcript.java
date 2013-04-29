package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import database.DBFactory;
import database.Model;

public class Transcript extends Model
{

	private Date date;

	private String id;
	private boolean isPredicted;

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
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean save()
	{
		// TODO
		throw new UnsupportedOperationException();
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
