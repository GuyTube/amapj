/*
 *  Copyright 2013-2050 Emmanuel BRUN (contact@amapj.fr)
 * 
 *  This file is part of AmapJ.
 *  
 *  AmapJ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  AmapJ is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with AmapJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
 package fr.amapj.service.services.authentification;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Origine du code : http://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html
 * 
 * 
 *  
 *
 */
public class PasswordEncryptionService
{

	public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
	{

		// Encrypt the clear-text password using the same salt that was used to
		// encrypt the original password
		byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

		// Authentication succeeds if encrypted password that the user entered
		// is equal to the stored hash
		return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);

	}

	public byte[] getEncryptedPassword(String password, byte[] salt)
	{
		try
		{
			// PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
			// specifically names SHA-1 as an acceptable hashing algorithm for
			// PBKDF2
			String algorithm = "PBKDF2WithHmacSHA1";
			// SHA-1 generates 160 bit hashes, so that's what makes sense here
			int derivedKeyLength = 160;
			// Pick an iteration count that works for you. The NIST recommends
			// at
			// least 1,000 iterations:
			// http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
			// iOS 4.x reportedly uses 10,000:
			// http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
			int iterations = 20000;

			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

			SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

			return f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			throw new RuntimeException("Erreur inattendue", e);
		}
	}

	public byte[] generateSalt()
	{
		try
		{
			// VERY important to use SecureRandom instead of just Random
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

			// Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
			byte[] salt = new byte[8];
			random.nextBytes(salt);

			return salt;
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("Erreur inattendue", e);
		}
	}
}