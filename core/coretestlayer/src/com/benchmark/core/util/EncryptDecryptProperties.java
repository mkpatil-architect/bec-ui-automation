package com.benchmark.core.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.benchmark.core.constants.CommonConstants;

public class EncryptDecryptProperties {

	// Public Methods
	/*
	 * Encrypt the property key
	 */
	public static void setEncryptValue(String propertyFileName,
			String propertyKey, String propertyKeyEncrypted)
			throws ConfigurationException {
		encryptPropertyValue(propertyFileName, propertyKey,
				propertyKeyEncrypted);
	}

	// Gets encryptValue.
	public static String getEncryptValue(String valueToEncrypt) {
		try {
			// Encrypt
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword(CommonConstants.ENCRYPTION_KEY);
			return encryptor.encrypt(valueToEncrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Decrypt the property key.
	 */
	public static String getDecryptValue(String propertyFileName,
			String propertyKey) throws ConfigurationException {
		return decryptPropertyValue(propertyFileName, propertyKey);
	}

	// Method to decrypt value
	public static String getDecryptValue(String valueToDecrypt) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(CommonConstants.ENCRYPTION_KEY);
		return encryptor.decrypt(valueToDecrypt);
	}

	// Private methods
	/*
	 * The method that encrypt property key in the properties file. This method
	 * will first check if the password is already encrypted or not. If not then
	 * only it will encrypt the password.
	 * 
	 * @throws ConfigurationException
	 */
	private static void encryptPropertyValue(String propertyFileName,
			String propertyKey, String isPropertyKeyEncrypted)
			throws ConfigurationException {

		PropertiesConfiguration config = new PropertiesConfiguration(
				propertyFileName);

		// Retrieve boolean properties value to see if password is already
		// encrypted or not
		// String isEncrypted = config.getString(isPropertyKeyEncrypted);

		// Check if password is encrypted?
		// if (isEncrypted.equals("false")) {
		String tmpPwd = config.getString(propertyKey);

		// Encrypt
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

		encryptor.setPassword(CommonConstants.ENCRYPTION_KEY);
		String encryptedPassword = encryptor.encrypt(tmpPwd);

		// Overwrite password with encrypted password in the properties file
		config.setProperty(propertyKey, encryptedPassword);

		// Set the boolean flag to true to indicate future encryption
		// operation that password is already encrypted
		config.setProperty(isPropertyKeyEncrypted, "true");
		// Save the properties file
		config.save();
		// } else {
		// System.out.println("User password is already encrypted.\n ");
		// }
	}

	/*
	 * The method that decrypt property key
	 */
	private static String decryptPropertyValue(String propertyFileName,
			String propertyKey) throws ConfigurationException {

		PropertiesConfiguration config = new PropertiesConfiguration(
				propertyFileName);
		String encryptedPropertyValue = config.getString(propertyKey);

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(CommonConstants.ENCRYPTION_KEY);
		String decryptedPropertyValue = encryptor
				.decrypt(encryptedPropertyValue);

		return decryptedPropertyValue;
	}

}
