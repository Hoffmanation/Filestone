package com.filestone.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.filestone.entity.UserDetail;

/**
 * A Helper-Service class with global utility methods for the Filestone Application
 * @author Hoffman
 *
 */
public abstract class FileUploadUtil {
	public static final Logger logger = Logger.getLogger(FileUploadUtil.class);
	
	
	/**
	 * Method will validate if the media file requested to displayed by the user is exist or not
	 * @param path
	 * @return {@link String}
	 */
	public static boolean isMediaResourceExist(String path) {
		File tmpDir = new File(path);
		return tmpDir.exists();
	}

	/**
	 * Method will resolve the filestone application Machine Host Name
	 * @param request
	 * @return {@link String}
	 */
	public static String getMachineHostName(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		int idx = (((uri != null) && (uri.length() > 0)) ? url.indexOf(uri) : url.length());
		String host = url.substring(0, idx); //base url
		idx = host.indexOf("://");
		if(idx > 0) {
		  host = host.substring(idx); //remove scheme if present
		}
		
		return host ;
	}
	/**
	 * Method will retrieve a string content from a given {@link InputStream}
	 * @param is
	 * @return {@link String}
	 */
	public static String getEmailTemplateFromClasspath(InputStream is) {
		String emailTemplate = null;
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				sb.append(line).append("\n");
				line = buf.readLine();
			}
			emailTemplate = sb.toString();
		} catch (Exception e) {
			logger.error("Failed to get Email-Template resource from classpath." ,e);
		}
		return emailTemplate ;
	}

	/**
	 * Method will Create the main application folder which will handle all read/write files operations
	 * @return {@link Boolean}
	 */
	public static boolean createAppResourcesFolder() {
		File file = new File(Constants.BASE_FILES_FOLDER);
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("FileStone app resource directory was created successfully, Path:  " + file.getAbsolutePath());
				return true;
			}
		}
		logger.info("FileStone app resource directory already exist, Path: " + file.getAbsolutePath());
		return false;
	}
	
	/**
	 * Method will Create the Recovery application folder which will handle all files of which the user can Recover from
	 * (Since we using {@link MultipartFile} we need to specify a unique place to save the files)
	 * @return {@link Boolean}
	 */
	public static boolean createAppRecoveryResourcesFolder() {
		File file = new File(Constants.RECOVERY_FILES_FOLDER);
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("FileStone Recovery app resource directory was created successfully, Path:  " + file.getAbsolutePath());
				return true;
			}
		}
		logger.info("FileStone Recovery app resource directory already exist, Path: " + file.getAbsolutePath());
		return false;
	}
	
	/**
	 * Method will Create the users folder which will handle all read/write files operations for specific user
	 * @return {@link Boolean}
	 */
	public static void  createUserResourcesFolder(UserDetail userDetail) {
		 File file = new File(Constants.BASE_FILES_FOLDER+userDetail.getUsername());
	        if (!file.exists()) {
	            if (file.mkdir()) {
					logger.info("FileStone user resource directory was created successfully, User: "+ userDetail.getUsername() +", Path:  " + file.getAbsolutePath());
	            } else {
					logger.info("FileStone user resource directory already exist for User: "+ userDetail.getUsername() +", Path:  " + file.getAbsolutePath());
	            }
	        }
	}


/**
 * Method will get a list of all files name for a specific user
 * @param userName
 * @return {@link List<String>}
 */
	public List<String> getAllFiles(String userName) {
		List<String> userFiles = new ArrayList<>();
		File folder = new File(Constants.BASE_FILES_FOLDER + userName);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				userFiles.add(listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				logger.info("Directory " + listOfFiles[i].getName());
			}
		}
		return userFiles;

	}

	/**
	 * Method will delete file by a user request from the user's folder
	 * @param multipartFile
	 * @param user
	 * @return {@link Boolean}
	 */
	public static boolean delete(String fileName, String user) {
		boolean isDeleted = false ;
		try {
			File fileToDelete = new File(Constants.BASE_FILES_FOLDER + user + "//" + fileName);
			if (fileToDelete.delete()) {
				isDeleted = true ;
				logger.info("Successfully deleted file: "+fileName + " For user :" +user);
			} else {
				logger.info("Failed to deleted file: "+fileName + " For user :" +user);
			}
		} catch (Exception e) {
			logger.error("Failed to deleted file: "+fileName+ " For user :" +user, e);
		}
		return isDeleted;
	}

}
