

package com.benchmark.core.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.testng.Assert;

import com.benchmark.core.constants.FilterTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * Class to handle all file related operations
 */
public class FileOperations {
	

	
	
	/**
	 * Copies file from one location to other.
	 * 
	 * @param sourceFile
	 *            , destinationFile
	 */
	public static void copyFile(File sourceFile, File destinationFile) {
		try {
			if (!destinationFile.exists()) {
				destinationFile.createNewFile();
			}

			FileChannel sourceChannel = null;
			FileChannel destinationChannel = null;
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
			try {
				sourceChannel = fileInputStream.getChannel();
				destinationChannel = fileOutputStream.getChannel();
				destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			} finally {
				if (sourceChannel != null) {
					sourceChannel.close();
				}
				if (destinationChannel != null) {
					destinationChannel.close();
				}
				fileInputStream.close();
				fileOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to copy a file from source to destination folder
	 * 
	 * @param sourceFileName
	 *            -> Ex: c:\workspace\hilton\sample.xls
	 * @param destinationDir
	 *            -> Ex: c:\workspace\hilton
	 * @param destinationFileName
	 *            -> FileName. Ex: sample.xls
	 * @return -> Destination File Name with full path
	 */
	public static String copyFile(String sourceFileName, String destinationDir, String destinationFileName) {
		try {
			File fSourceFileName = new File(sourceFileName);
			if (!fSourceFileName.exists()) {
				Log.writeMessage(LogLevel.WARN, "Specified file is not available");
				return null;
			}

			File fDestinationDir = new File(destinationDir);
			if (!fDestinationDir.exists()) {
				fDestinationDir.mkdirs();
			}

			String finalDestinationFilePath = fDestinationDir.getPath() + "\\" + destinationFileName;
			if (copyImageOrFile(sourceFileName, finalDestinationFilePath)) {
				return finalDestinationFilePath;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		return null;
	}

	/**
	 * This method will copy the any Image/File
	 * 
	 * @param source
	 * @param destination
	 */
	public static boolean copyImageOrFile(String source, String destination) {

		try (InputStream inStream = new FileInputStream(new File(source))) {
			try (OutputStream outStream = new FileOutputStream(new File(destination))) {
				byte[] buffer = new byte[1024];

				int length;
				// copy the file content in bytes
				while ((length = inStream.read(buffer)) > 0) {
					outStream.write(buffer, 0, length);
				}
			}
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		return false;
	}

	/**
	 * Creates the directory named by this abstract pathname, including any
	 * necessary but nonexistent parent directories. Note that if this operation
	 * fails it may have succeeded in creating some of the necessary parent
	 * directories.
	 * 
	 * @param filePath
	 * @return boolean
	 */
	public static boolean createDirectory(String filePath) {
		return new File(filePath).mkdirs();
	}

	/**
	 * This method will delete the file based on the file name.
	 * 
	 * @param fileName
	 * @return true/false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		try {
			if (file.exists()) {
				file.delete();
				return true;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME,
					String.format("Error while deleting file. %s ", e.getMessage()));
		}
		return false;
	}

	/**
	 * This method will delete the all files/sub-folders and sub-folder files.
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
				Log.writeMessage(FileOperations.class.getName(), "Directory is deleted : " + file.getAbsolutePath());
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					deleteFile(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					Log.writeMessage(FileOperations.class.getName(),
							"Directory is deleted : " + file.getAbsolutePath());
				}
			}
		} else {
			// if file, then delete it
			file.delete();
			Log.writeMessage(FileOperations.class.getName(), "File is deleted : " + file.getAbsolutePath());
		}
	}

	/**
	 * This method will extract the ZIP file
	 * 
	 * @param zipFilePath
	 */
	public static void extractZipFile(String zipFilePath) {
		unZipFile(zipFilePath);
	}

	/**
	 * This method will extract the ZIP files to the destination folder
	 * 
	 * @param zipFilePath
	 * @param unZipFilePath
	 */
	public static void extractZipFile(String zipFilePath, String unZipFilePath) {
		File destinationDirectory = new File(unZipFilePath);
		deleteFile(zipFilePath);
		createDirectory(unZipFilePath);

		try (ZipFile zipFile = new ZipFile(zipFilePath);) {

			Enumeration<? extends ZipEntry> enumZipEntries = zipFile.entries();
			List<String> listFiles = new ArrayList<String>();
			while (enumZipEntries.hasMoreElements()) {
				ZipEntry zipEntryFiles = (ZipEntry) enumZipEntries.nextElement();
				String zipFileName = zipEntryFiles.getName();
				if (zipEntryFiles.isDirectory() || zipFileName.contains("/")) {
					String directoryName = zipEntryFiles.getName().split("/")[0];
					if (!listFiles.contains(directoryName)) {
						listFiles.add(directoryName);
						zipFileName = zipEntryFiles.getName().split("/")[1];
						System.out.println("Extracting directory: " + directoryName);
						(new File(destinationDirectory + "/" + directoryName)).mkdir();
						System.out.println("Extracting file: " + zipFileName);
						copyContentIPStream(zipFile.getInputStream(zipEntryFiles), new BufferedOutputStream(
								new FileOutputStream(destinationDirectory + "/" + directoryName + "/" + zipFileName)));
						continue;
					}
				}
				System.out.println("Extracting file: " + zipFileName);
				copyContentIPStream(zipFile.getInputStream(zipEntryFiles),
						new BufferedOutputStream(new FileOutputStream(destinationDirectory + "/" + zipFileName)));
			}
			zipFile.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
		}
	}

	/**
	 * This method will get the File based on file name.
	 * 
	 * @param fileName
	 * @return File
	 */
	public static File getFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	/**
	 * Verifies filename in the export directory and gets the file path
	 * 
	 * @param exportDir
	 * @param fileName
	 * @return
	 */
	public static String getFileFromExportedFileDir(String exportDir, String fileName) {
		String finalFileName = "";
		File directory = new File(exportDir);

		if (!directory.exists()) {
			directory.mkdir();
		}

		File[] listOfiles = directory.listFiles();
		for (File file : listOfiles) {
			if (file.isFile()) {
				String fName = file.getName();
				if (fName.indexOf(fileName) >= 0) {
					finalFileName = fName;
				}
			}
		}
		if (finalFileName.equals("")) {
			String errorMessage = String.format(
					"No file found with the file name containing the string %s in the directory %s. List of files found - %s",
					fileName, exportDir, Arrays.toString(listOfiles));
			Assert.assertTrue(false,
					CommonUtil.appendErrorCodeToMessage(errorMessage));
		}
		return String.format("%s/%s", exportDir, finalFileName);
	}

	/**
	 * This method will get the number of lines available in the file.
	 * 
	 * @param expectedFile
	 * @return lines count
	 */
	public static String getLineCount(String expectedFile) {
		return String.valueOf(readFileAndGetData(expectedFile).size());
	}

	/**
	 * Gets property value from a properties file.
	 * 
	 * @param fileName
	 *            : File Name EX: App.properties
	 * @param property
	 *            : Property EX: db.database
	 */
	public static String getPropertyValue(String fileName, String property) throws IOException {
		return getProperties(fileName).getProperty(property);
	}

	/**
	 * This method will return complete property file details.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Properties getProperties(String fileName) throws IOException {
		return getPropertyFileDetails(fileName);
	}

	/**
	 * Checks whether file exists or not.
	 * 
	 * @param fileName
	 *            : File Name
	 */
	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return (file.exists());
	}

	/**
	 * This method will verify the text is available in the given file content.
	 * 
	 * @param fileName
	 * @param textToCompare
	 * @return true/false
	 */
	public static boolean isTextPresentInFile(String fileName, String textToCompare) {
		String currentLine = null;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
			while ((currentLine = bufferedReader.readLine()) != null) {
				if (currentLine.indexOf(textToCompare) != -1) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
		}
		return false;
	}

	/**
	 * This method will merge two files. This will merge the second file
	 * contents into first file.
	 * 
	 * @param fileName1
	 * @param fileName2
	 */
	public static void mergeTwoFiles(String fileName1, String fileName2) {
		File file1 = new File(fileName1);
		File file2 = new File(fileName2);
		try (InputStream inputStream = new FileInputStream(file2)) {
			try (OutputStream outputStream = new FileOutputStream(file1, true)) {
				byte[] buffer = new byte[8192];
				int len;
				while ((len = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, len);
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
		}
	}

	/**
	 * This method will remove the files which are older than N days
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static void removeFilesOlderThanNDays(String fileName, int daysBack) {
		File file = new File(fileName);
		if (file.exists()) {
			final File[] listFiles = file.listFiles();
			final long purgeTime = System.currentTimeMillis() - (daysBack * 24L * 60L * 60L * 1000L);
			for (File listFile : listFiles) {
				if (listFile.lastModified() < purgeTime) {
					deleteFile(listFile);
				}
			}
		}
	}

	/**
	 * This method will write data to the file.
	 * 
	 * @param inputStream
	 * @param fileName
	 */
	public static void writeFile(InputStream inputStream, String fileName) {
		try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
			try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
				byte[] buffer = new byte[BUFFER_SIZE];
				int i = 0;
				while ((i = dataInputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, i);
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
		}
	}

	/**
	 * Method to find file from directory by file search
	 */
	public static String getFileFromDirectory(String directoryName, String searchFileName) {
		File directory = new File(directoryName);
		if (directory.exists()) {
			File[] listOfiles = directory.listFiles();
			for (File file : listOfiles) {
				if (file.isFile()) {
					String fileName = file.getName();
					if (fileName.equals(searchFileName) || (fileName.indexOf(searchFileName) >= 0)) {
						return String.format("%s\\%s", directoryName, fileName);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Method to get Last modified file from the directory
	 * 
	 * @param dir
	 *            -> Directory
	 * @return -> FilePath
	 */
	public static String lastFileModified(String dir) {
		File fl = new File(dir);
		String lastModifiedFile = null;
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choice = file;
				lastMod = file.lastModified();
			}
		}
		lastModifiedFile = choice.getAbsolutePath();
		return lastModifiedFile;
	}

	/**
	 * Method to get Last modified file with format from the directory
	 * 
	 * @param dir
	 *            -> Directory
	 * @return -> FilePath
	 */
	public static String lastFileModified(String dir, String format) {
		String lastModifiedFile = null;
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});

		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.getName().startsWith(format)) {
				if (file.lastModified() > lastMod) {
					choice = file;
					lastMod = file.lastModified();
				}
			}
		}
		lastModifiedFile = choice.getAbsolutePath();
		return lastModifiedFile;
	}

	/**
	 * Method to delete the file with specified format and specific folder
	 * 
	 * @param path
	 *            -> Path
	 * @param format
	 *            -> Format
	 * @return -> True/False
	 */
	public static boolean deleteFile(String path, String format) {
		return deleteFile(path, format, FilterTypeConstants.StartWith);
	}

	/**
	 * Method to delete the file with specified format and specific folder
	 * 
	 * @param path
	 * @param format
	 * @param filterType
	 * @return -> True/False
	 */
	public static boolean deleteFile(String path, String format, FilterTypeConstants filterType) {
		List<Boolean> results = new ArrayList<Boolean>();
		try {
			File mainFolder = new File(path);
			if (mainFolder.exists()) {
				File[] innerFiles = mainFolder.listFiles();
				for (File innerFile : innerFiles) {
					String fileName = innerFile.getName();
					boolean isDelete = false;
					switch (filterType) {
					case Contains:
						isDelete = fileName.contains(format);
						break;
					case EndWith:
						isDelete = fileName.endsWith(format);
						break;
					case StartWith:
						isDelete = fileName.startsWith(format);
						break;
					case Equals:
						isDelete = fileName.equals(format);
						break;
					}
					if (isDelete) {
						if (innerFile.isDirectory()) {
							deleteFile(innerFile);
							results.add(true);
						} else {
							results.add(innerFile.delete());
						}
					}
				}
			} else {
				Log.writeMessage(LogLevel.INFO, CLASS_NAME, String.format("%s, folder does not exists.", path));
			}
			return results.contains(false) ? false : true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	// Private Methods

	/**
	 * Method to copy content IP stream
	 */
	private static final void copyContentIPStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	/*
	 * Method will return property file details.
	 */
	private static Properties getPropertyFileDetails(String fileName) throws IOException {
		InputStream inputStream = null;
		final Properties properties = new Properties();
		try {

			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

			if (inputStream != null) {

				properties.load(inputStream);
				return properties;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return properties;
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
		return properties;
	}

	/*
	 * This method will read file and gets data.
	 * 
	 * @param fileName
	 * 
	 * @return
	 */
	public static List<String> readFileAndGetData(String fileName) {
		List<String> lines = new ArrayList<String>();
		try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
			try (DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {
				try (BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(dataInputStream, "UTF8"))) {
					String currentLine;
					while ((currentLine = bufferedReader.readLine()) != null) {
						lines.add(currentLine);
					}
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME,
					"Error while reading file to check no of lines in a file. " + e.getMessage());
		}
		return lines;
	}

	public static void readFileDataAndReplaceText(String fileName, String replaceText, String replaceWith)
			throws IOException {
		Path path = Paths.get(fileName);
		Charset charset = StandardCharsets.UTF_8;
		String content = new String(Files.readAllBytes(path), charset);
		content = content.replaceAll(replaceText, replaceWith);
		Files.write(path, content.getBytes(charset));
	}

	private static void unZipFile(String zipFilePath) {
		try (ZipFile zipFile = new ZipFile(zipFilePath)) {
			Enumeration<? extends ZipEntry> enumZipEntries = zipFile.entries();
			while (enumZipEntries.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enumZipEntries.nextElement();
				if (zipEntry.isDirectory()) {
					Log.writeMessage(CLASS_NAME, "Extracting directory: " + zipEntry.getName());
					(new File(zipEntry.getName())).mkdir();
					continue;
				}
				Log.writeMessage(CLASS_NAME, "Extracting file: " + zipEntry.getName());

				copyContentIPStream(zipFile.getInputStream(zipEntry),
						new BufferedOutputStream(new FileOutputStream(zipEntry.getName())));
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
		}
	}

	public static void appendToFile(String fileName, String content) {
		try {
			FileWriter fstream = new FileWriter(fileName, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
		}

	}

	/*
	 * Constant variable
	 */
	private final static String CLASS_NAME = FileOperations.class.getName();
	private final static int BUFFER_SIZE = 1024;
}
