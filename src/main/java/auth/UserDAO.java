package auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//http://knowledgeshare.awardspace.info/?p=204

public class UserDAO {
	public static boolean login(String user, String password) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = Database.getConnection();

			statement = connection
					.prepareStatement("SELECT `user`.`id`, `user`.`username`, `user`.`password` FROM `application`.`user` WHERE `user`= ? AND `password` = ?;");

			// ps =
			// con.prepareStatement("select user, pass from userinfo where user= ? and pass= ? ");
			statement.setString(1, user);
			statement.setString(2, password);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) // found
			{
				System.out.println(rs.getString("user"));
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			System.out.println("Error in login() -->" + ex.getMessage());
			return false;
		} finally {
			Database.close(connection);
		}
	}
}