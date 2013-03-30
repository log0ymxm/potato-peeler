package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DBFactory;
import database.Model;

public class State extends Model
{

	private String abbreviation;
	private String id;
	private String name;

	@SuppressWarnings("unchecked")
	public static ArrayList<State> findAll()
	{
		ArrayList<State> states = new ArrayList<>();

		Connection connection = DBFactory.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT id, abbreviation, name FROM states";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				State state = new State();
				state.setId(resultSet.getString(1));
				state.setAbbreviation(resultSet.getString(2));
				state.setName(resultSet.getString(3));
				states.add(state);
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
		return states;
	}

	public static State findById(String id)
	{
		State state = null;

		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT id, abbreviation, name FROM states WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				state = new State();
				state.setId(resultSet.getString(1));
				state.setAbbreviation(resultSet.getString(2));
				state.setName(resultSet.getString(3));
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
		return state;
	}

	public String getAbbreviation()
	{
		return this.abbreviation;
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing state needs an ID";
		}
		if (valid == null)
		{
			valid = (this.getName() != null) ? null : "A state needs a name";
		}
		if (valid == null)
		{
			valid = (this.getAbbreviation() != null) ? null
					: "A state requires an abbreviation";
		}
		return valid;
	}

	@Override
	public boolean save()
	{
		// TODO implement
		return false;
	}

	public void setAbbreviation(String abbreviation)
	{
		this.abbreviation = abbreviation;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
