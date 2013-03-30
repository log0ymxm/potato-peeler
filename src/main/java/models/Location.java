package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBFactory;
import database.InvalidModelException;
import database.Model;

public class Location extends Model
{

	private String id;

	private String name;
	private State state;
	private String stateId;

	public Location(ResultSet resultSet)
	{
		System.out.println("construct location from resultset");
		try
		{
			this.setId(resultSet.getString("id"));
			this.setName(resultSet.getString("name"));
			this.setStateId(resultSet.getString("state_id"));
			this.dirty = false;
			this.fresh = false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Location(String name, State state)
	{
		System.out.println("location constructor");
		this.setName(name);
		this.setState(state);
		this.dirty = true;
		this.fresh = true;
	}

	public static Location findById(String id)
	{
		Location location = null;

		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT id, name, state_id FROM locations WHERE id = ? LIMIT 1";
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				location = new Location(resultSet);
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
		return location;
	}

	public static Location findOrCreate(State state, String name)
			throws InvalidModelException
	{
		String findQuery = "SELECT id, name, state_id FROM locations WHERE state_id = ? AND name like ? LIMIT 1";
		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
			statement = connection.prepareStatement(findQuery);
			statement.setString(1, state.getId());
			statement.setString(2, name);

			resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				return new Location(resultSet);
			}
			else
			{
				Location location = new Location(name, state);
				location.save();
				return location;
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

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public State getState()
	{
		return this.state;
	}

	public String getStateId()
	{
		return this.stateId;
	}

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing location needs an ID";
		}
		if (valid == null)
		{
			valid = (this.getName() != null) ? null : "A location needs a name";
		}
		if (valid == null)
		{
			valid = (this.getState() != null) ? null
					: "A location requires a state";
			System.out.println(this);
		}
		if (valid == null)
		{
			String stateValid = this.getState().isValid();
			valid = (stateValid == null) ? null
					: "A location requires a valid state (" + stateValid + ")";
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
			PreparedStatement insertNewLocation = null;
			ResultSet resultSet = null;
			PreparedStatement selectId = null;
			if (this.isFresh())
			{
				System.out.println("saving new location");
				String insertQuery = "INSERT INTO locations (name, state_id) VALUES (?, ?)";
				try
				{
					insertNewLocation = connection
							.prepareStatement(insertQuery);
					insertNewLocation.setString(1, this.getName());
					insertNewLocation.setString(2, this.getState().getId());
					result = insertNewLocation.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT id FROM locations WHERE name like ? AND state_id = ?";
						selectId = connection.prepareStatement(idQuery);
						selectId.setString(1, this.getName());
						selectId.setString(2, this.getState().getId());
						resultSet = selectId.executeQuery();

						if (resultSet.next())
						{
							this.id = resultSet.getString(1);
							System.out.println("save get id: " + this.id);
							this.setId(this.id);
							this.fresh = false;
							this.dirty = false;
							return true;
						}
						else
						{
							System.err.println("Error getting location ID");
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
						insertNewLocation.close();
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
				String updateQuery = "UPDATE locations SET name=?, state_id=? WHERE id=?";
				PreparedStatement updateLocation = null;
				try
				{
					updateLocation = connection.prepareStatement(updateQuery);
					updateLocation.setString(1, this.getName());
					updateLocation.setString(2, this.getState().getId());
					updateLocation.setString(3, this.getId());
					result = updateLocation.executeUpdate();
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
						updateLocation.close();
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

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public void setStateId(String stateId)
	{
		this.stateId = stateId;
	}

	@Override
	public String toString()
	{
		return "Location: " + this.getId() + ", " + this.getName() + ", "
				+ this.getState();
	}

}
