

package com.benchmark.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * Class to perform ZIP file related operations
 */
public class ZIPFileOperations {

	// Holds all files in the folder
	private static List<String> m_fileList = new ArrayList<>();
	// Source folder path
	private static String m_sourceFolder;
	// Final zip file folder path
	private static String m_zipFolder;

	/**
	 * Method to zip files in the particular folder
	 * 
	 * @param sourceFolder
	 */
	public static void zipFolder(String sourceFolder) {
		// Log.writeMessage(CLASS_NAME, "Souce folder: " + sourceFolder);
		m_sourceFolder = sourceFolder.replace("//", "/");
		m_zipFolder = String.format("%s.zip", m_sourceFolder);
		generateFileList(new File(sourceFolder));
		zipIt(m_zipFolder);
	}

	/**
	 * Method to zip files in the particular folder
	 * 
	 * @param sourceFolder
	 * @param targetFolderName
	 */
	public static void zipFolder(String sourceFolder, String targetFolderName) {
		// Log.writeMessage(CLASS_NAME, "Souce folder: " + sourceFolder);
		m_sourceFolder = sourceFolder.replace("//", "/");
		m_zipFolder = String.format("%s.zip", targetFolderName);
		generateFileList(new File(sourceFolder));
		zipIt(m_zipFolder);
	}

	/**
	 * Method to unzip all files and put in output folder
	 */
	public static void unZipFolder(String zipFolderPath, String outputFolder) {
		byte[] buffer = new byte[1024];
		try {
			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFolderPath));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				Log.writeMessage("ZIPFileOperations", "file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// Private Methods

	/**
	 * Recursive method to find all files in the target zip directory
	 * 
	 * @param node
	 *            the current {@link File} object under inspection
	 * @param root
	 *            the base directory to be zipped
	 */
	private static void generateFileList(File node, File root) {
		// add file only
		if (node.isFile()) {
			m_fileList.add(root.toPath().relativize(node.toPath()).toString());
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), root);
			}
		}
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList.
	 * If the parameter is a single file, only that file will be stored in the
	 * zip file.
	 *
	 * @param root
	 *            {@link File} containing the file or directory to be zipped
	 */
	private static void generateFileList(File root) {
		if (root.isFile()) {
			m_fileList.add(root.getPath());
		} else {
			generateFileList(root, root);
		}
	}

	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 */
	private static void zipIt(String zipFile) {
		byte[] buffer = new byte[1024];
		try (FileOutputStream fileOutputStream = new FileOutputStream(zipFile)) {
			try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
				for (String file : m_fileList) {
					// Log.writeMessage(CLASS_NAME, file);
					ZipEntry zipEntry = new ZipEntry(file);
					zipOutputStream.putNextEntry(zipEntry);
					try (FileInputStream fileInputStream = new FileInputStream(
							Paths.get(m_sourceFolder, file).toString())) {
						int fileLength;
						while ((fileLength = fileInputStream.read(buffer)) > 0) {
							zipOutputStream.write(buffer, 0, fileLength);
						}
					}
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
	}

	private final static String CLASS_NAME = ZIPFileOperations.class.getName();

}
