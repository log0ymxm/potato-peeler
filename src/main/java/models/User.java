package models;

import java.security.Key;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import database.DBFactory;

// TODO Match to proper spec

public class User
{

	public static boolean login(String username, String password)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = DBFactory.getConnection();

			statement = connection
					.prepareStatement("SELECT user.id, user.username, user.salt, user.password FROM users WHERE user= ?;");

			// byte[] salt = PasswordSalts.nextSalt();
			// String saltStr = Arrays.toString(salt);

			statement.setString(1, username);

			ResultSet rs = statement.executeQuery();

			if (rs.next())
			{

				byte[] salt = rs.getString("salt").getBytes();
				SecretKeyFactory f = SecretKeyFactory
						.getInstance("PBKDF2WithHmacSHA1");
				KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 1024,
						128);
				SecretKey s = f.generateSecret(ks);
				Key k = new SecretKeySpec(s.getEncoded(), "AES");
				System.out.println(k.getEncoded());

				System.out.println(rs.getString("user"));
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception ex)
		{
			System.out.println("Error in login() -->" + ex.getMessage());
			return false;
		}
		finally
		{
			DBFactory.closeConnection(connection);
		}
	}
}
