package com.filestone.util;

/**
 * Filestone Application Constants
 * @author Hoffman
 *
 */
public interface Constants {


	final String FILESTONE_APP_RESOURCE_FOLDER = "filestoneData";
	
	final String BASE_FILES_FOLDER = System.getProperty("user.home") +"/"+FILESTONE_APP_RESOURCE_FOLDER+"/";
	final String RECOVERY_FILES_FOLDER =System.getProperty("user.home") +"/"+FILESTONE_APP_RESOURCE_FOLDER+"/recoveryFiles";
	final String LOGOUT_PAGE = "/login.html";
	final String USER_LOGIN = "user";
	final String RESET_PASS_REPLACE_URL = "inject-reset-password-url";

	//Media Type Arrays
	String[] imageFileExtention = { "svg", "png", "bpg", "bmp", "jpeg", "imp", "jbg", "jpe", "jpg", "mac","GIF", };

	String[] textFileExtention = { "doc", "docx", "rtf", "text", "txt" };

	String[] videoFileExtention = { "webm ", "mkv ", "flv ", "vob ", "ogv ", "ogg ", "drc ", "gif ", "gifv ", "mng ",
			"mov", "yuv", "rmvb", "amv", "mp4", "m4p", "m4v", "mpg", "mp2", "mpeg", "mpe", "mpv", "avi ", };

	String[] audioFileExtention = { "m4a", "m4b", "mp3", "wav", "wma",};
	
	String[] pdfFileExtention = { "pdf"};

}
