package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBFactory
{

	private static Connection connection = null;

	public static final String DATABASE_URL = "jdbc:mysql://localhost/potato_peeler";

	public static void closeConnection(Connection connection)
	{
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// I'm a slow method, FIXME
	public static Connection getConnection()
	{
		try
		{
			DBFactory.connection = DriverManager.getConnection(
					DBFactory.DATABASE_URL, "root", "");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return DBFactory.connection;
	}
}
