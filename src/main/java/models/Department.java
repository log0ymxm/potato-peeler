package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBFactory;
import database.InvalidModelException;
import database.Model;

public class Department extends Model
{

	private String id;
	private String name;

	public Department(ResultSet resultSet)
	{
		try
		{
			this.setId(resultSet.getString("id"));
			this.setName(resultSet.getString("name"));
			this.dirty = false;
			this.fresh = false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Department(String name)
	{
		this.setName(name);
		this.dirty = true;
		this.fresh = true;
	}

	public static Department findById(String string)
	{
		// TODO
		throw new UnsupportedOperationException();
	}

	public static Department findOrCreate(String department_name)
			throws InvalidModelException
	{
		String findQuery = "SELECT id, name FROM departments WHERE name like ? LIMIT 1";
		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
			statement = connection.prepareStatement(findQuery);
			statement.setString(1, department_name);

			resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				return new Department(resultSet);
			}
			else
			{
				Department department = new Department(department_name);
				department.save();
				return department;
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

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing department needs an ID";
		}
		if (valid == null)
		{
			valid = (this.getName() != null) ? null
					: "A department needs a name";
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
			PreparedStatement insertNewDepartment = null;
			ResultSet resultSet = null;
			PreparedStatement selectId = null;
			if (this.isFresh())
			{
				System.out.println("saving new department");
				String insertQuery = "INSERT INTO departments (name) VALUES (?)";
				try
				{
					insertNewDepartment = connection
							.prepareStatement(insertQuery);
					insertNewDepartment.setString(1, this.getName());
					result = insertNewDepartment.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT id FROM departments WHERE name like ? LIMIT 1";
						selectId = connection.prepareStatement(idQuery);
						selectId.setString(1, this.getName());
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
							System.err.println("Error getting departments ID");
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
						insertNewDepartment.close();
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
				String updateQuery = "UPDATE departments SET name=? WHERE id=?";
				PreparedStatement updateDepartment = null;
				try
				{
					updateDepartment = connection.prepareStatement(updateQuery);
					updateDepartment.setString(1, this.getName());
					updateDepartment.setString(2, this.getId());
					result = updateDepartment.executeUpdate();
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
						updateDepartment.close();
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

	@Override
	public String toString()
	{
		return "Department: " + this.getId() + " " + this.getName();
	}

}
