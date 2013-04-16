package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DBFactory;
import database.Model;
import database.ModelList;

public class State extends Model
{

	private String abbreviation;
	private String id;
	private String name;
	private ModelList<School> schools;

	public State(ResultSet resultSet) throws SQLException
	{
		this(resultSet.getString("id"), resultSet.getString("abbreviation"),
				resultSet.getString("name"), false, false);
	}

	public State(String id, String abbreviation, String name, Boolean dirty,
			Boolean fresh)
	{
		super("states", new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 7041524624059868442L;

			{
				this.add("id");
				this.add("abbreviation");
				this.add("name");
			}
		}, new ArrayList<String>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -7169027537463382455L;

			{
			}
		}, dirty, fresh);
		this.setId(id);
		this.setAbbreviation(abbreviation);
		this.setName(name);
	}

	public static ModelList<State> findAll()
	{
		ModelList<State> states = new ModelList<>();

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
				State state = new State(resultSet);
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
				state = new State(resultSet);
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

	public ModelList<School> getSchools()
	{
		if (this.schools == null)
		{
			this.schools = School.findByState(this.getId());
		}
		return this.schools;
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
		// TODO
		throw new UnsupportedOperationException();
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
