package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DBFactory;
import database.InvalidModelException;
import database.Model;

public class Teacher extends Model
{

	private Department department;
	private String department_id;
	private String first_name;
	private String id;
	private String last_name;
	private String rmpId;
	private ArrayList<School> schools;

	public Teacher(ResultSet resultSet)
	{
		try
		{
			this.setId(resultSet.getString("id"));
			this.setFirst_name(resultSet.getString("first_name"));
			this.setLast_name(resultSet.getString("last_name"));
			this.setRmpId(resultSet.getString("rmp_id"));
			this.setDepartment_id(resultSet.getString("department_id"));
			this.dirty = false;
			this.fresh = false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Teacher(String first_name, String last_name, String rmp_id,
			Department department)
	{
		this.setFirst_name(first_name);
		this.setLast_name(last_name);
		this.setRmpId(rmp_id);
		this.setDepartment(department);
		this.dirty = true;
		this.fresh = true;
	}

	public static ArrayList<Teacher> findAll()
	{
		System.out.println("find all teachers");
		ArrayList<Teacher> teachers = new ArrayList<>();

		Connection connection = DBFactory.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT id, department_id, first_name, last_name, rmp_id FROM teachers";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				Teacher teacher = new Teacher(resultSet);
				teachers.add(teacher);
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
		return teachers;
	}

	public static Teacher findById(String id)
	{
		Teacher teacher = null;

		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{

			String query = "SELECT id, department_id, first_name, last_name, rmp_id FROM teachers WHERE id = ? LIMIT 1";
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				teacher = new Teacher(resultSet);
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
		return teacher;
	}

	public static Teacher findOrCreate(String first_name, String last_name,
			String rmp_id, Department department) throws InvalidModelException
	{
		String findQuery = "SELECT id, first_name, last_name, rmp_id, department_id FROM teachers WHERE first_name like ? AND last_name like ? AND rmp_id like ? AND department_id = ? LIMIT 1";
		Connection connection = DBFactory.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
			statement = connection.prepareStatement(findQuery);
			statement.setString(1, first_name);
			statement.setString(2, last_name);
			statement.setString(3, rmp_id);
			statement.setString(4, department.getId());

			resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				return new Teacher(resultSet);
			}
			else
			{
				Teacher teacher = new Teacher(first_name, last_name, rmp_id,
						department);
				teacher.save();
				return teacher;
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

	public Department getDepartment()
	{
		// TODO perform a lazy load of department if department_id exists and
		// department is null
		return this.department;
	}

	public String getDepartment_id()
	{
		return this.department_id;
	}

	public String getFirst_name()
	{
		return this.first_name;
	}

	public String getId()
	{
		return this.id;
	}

	public String getLast_name()
	{
		return this.last_name;
	}

	public String getRmpId()
	{
		return this.rmpId;
	}

	public ArrayList<School> getSchools()
	{
		return this.schools;
	}

	@Override
	public String isValid()
	{
		String valid = null;
		if (!this.isFresh())
		{
			valid = (this.getId() != null) ? null
					: "An existing teacher needs an ID";
		}
		if (valid == null)
		{
			valid = (this.getDepartment_id() != null) ? null
					: "A teacher needs a department";
		}
		if (valid == null)
		{
			valid = (this.getFirst_name() != null) ? null
					: "A teacher needs a first name";
		}
		if (valid == null)
		{
			valid = (this.getLast_name() != null) ? null
					: "A teacher needs a last name";
		}
		if (valid == null)
		{
			valid = (this.getRmpId() != null) ? null
					: "A teacher needs a rmp id";
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
				String insertQuery = "INSERT INTO teachers (department_id, first_name, last_name, rmp_id) VALUES (?, ?, ?, ?)";
				try
				{
					insertNewDepartment = connection
							.prepareStatement(insertQuery);
					insertNewDepartment.setString(1, this.getDepartment_id());
					insertNewDepartment.setString(2, this.getFirst_name());
					insertNewDepartment.setString(3, this.getLast_name());
					insertNewDepartment.setString(4, this.getRmpId());
					result = insertNewDepartment.executeUpdate();
					if (result > 0)
					{
						String idQuery = "SELECT id FROM teachers WHERE department_id = ? AND first_name like ? AND last_name like ? AND rmp_id like ? LIMIT 1";
						selectId = connection.prepareStatement(idQuery);
						selectId.setString(1, this.getDepartment_id());
						selectId.setString(2, this.getFirst_name());
						selectId.setString(3, this.getLast_name());
						selectId.setString(4, this.getRmpId());
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
							System.err.println("Error getting teachers ID");
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
				String updateQuery = "UPDATE teachers SET department_id = ?, first_name=?, last_name=?, rmp_id=? WHERE id=?";
				PreparedStatement updateDepartment = null;
				try
				{
					updateDepartment = connection.prepareStatement(updateQuery);
					updateDepartment.setString(1, this.getDepartment_id());
					updateDepartment.setString(2, this.getFirst_name());
					updateDepartment.setString(3, this.getLast_name());
					updateDepartment.setString(4, this.getRmpId());
					updateDepartment.setString(5, this.getId());
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

	public void setDepartment(Department department)
	{
		this.department = department;
		this.setDepartment_id(department.getId());
	}

	public void setDepartment_id(String department_id)
	{
		this.department_id = department_id;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public void setRmpId(String rmpId)
	{
		this.rmpId = rmpId;
	}

	public void setSchools(ArrayList<School> schools)
	{
		this.schools = schools;
	}

}
