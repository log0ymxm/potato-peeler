package auth;

import java.security.SecureRandom;

public class PasswordSalts
{

	public static final int SALT_LENGTH = 16;

	public static byte[] nextSalt()
	{
		byte[] salt = new byte[PasswordSalts.SALT_LENGTH];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(salt);
		return salt;
	}
}
