package com.filestone.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.filestone.entity.FileInfo;
import com.filestone.entity.Users;
import com.filestone.service.UserService;

/**
 * A Helper-Service class with global utility methods for the Filestone Application
 * @author Hoffman
 *
 */
@Component
public class AppUtil {
	public static final Logger logger = Logger.getLogger(AppUtil.class);
	
	@Value("#{new Boolean('${filestone.file.recovery}')}")
	private Boolean fileRecovery ;

	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private UserService userStub;
		
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	/**
	 * Method will return an HTML template already injected with the media content 
	 * requested by the user to be shown in the page
	 * @param fileExtention
	 * @param userId
	 * @param fileName
	 * @return {@link String}
	 */
	public String getFIlePreview(String fileExtention, UUID userId, String fileName) {
		try {
			Users userDetail = userStub.getUserById(userId);
			Path path = new File(Constants.BASE_FILES_FOLDER + userDetail.getUsername() + "//" + fileName).toPath();
			byte[] array = Files.readAllBytes(path);
			String base64DataString = Base64.getEncoder().encodeToString(array);

			if (Arrays.asList(Constants.pdfFileExtention).contains(fileExtention.toLowerCase())) {
				return "<img style=\"width: auto;\"  ng-src=\"data:image/JPEG;base64,"+ getPdfAsImg(userDetail, fileName) + "\">";
			}

			else if (Arrays.asList(Constants.imageFileExtention).contains(fileExtention.toLowerCase())) {
				return "<img class=\"img-content\" ng-src=\"data:image/JPEG;base64,"+ base64DataString + "\">";
			}

			else if (Arrays.asList(Constants.audioFileExtention).contains(fileExtention.toLowerCase())) {
				return "<video class=\"video-content\" controls><source src=\"data:video/mp4;base64,"+ base64DataString + "\" type=\"video/mp4\"></video>";
			}

			else if (Arrays.asList(Constants.textFileExtention).contains(fileExtention.toLowerCase())) {
				return "<div style=\"width: auto;\" id=\"list\"><p><iframe src=\"" + base64DataString+ "\" frameborder=\"0\" height=\"400\"width=\"95%\"></iframe></p></div>";
			}

			else if (Arrays.asList(Constants.videoFileExtention).contains(fileExtention.toLowerCase())) {
				return "<video class=\"video-content\" controls><source src=\"/filestone/fs/getContent/"+fileName+"/videos\" type=\"video/mp4\"></video>";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "<div style=\"color: red;\"><h2>Preview is not available for this file type.</h2></div>";
	}

	
	/**
	 * A Utility method that will convert a PDF file to a base64 Data String
	 * @return {@link String}
	 * @param userDetail
	 * @param fileName
	 * @return {@link String}
	 */
	public String getPdfAsImg(Users userDetail, String fileName) {
		String base64DataString = null;
		ByteArrayOutputStream baos = null;
		try {

			File sourceFile = new File(Constants.BASE_FILES_FOLDER + userDetail.getUsername() + "//" + fileName);
			PDDocument document = null;
			Set<byte[]> imagesByte = new HashSet<>();
			document = PDDocument.load(sourceFile);
			List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
			for (int i = 0; i < allPages.size(); i++) {
				PDPage page = allPages.get(i);
				page.convertToImage();
				BufferedImage image = page.convertToImage();
				baos = new ByteArrayOutputStream();
				ImageIO.write(image, "jpg", baos);
				break;
			}
			document.close();
			base64DataString = java.util.Base64.getMimeEncoder().encodeToString(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base64DataString;
	}
	
	/*
	 * Method will Generate a Salt-String (Can be use to generate Access-Token)
	 * @return {@link String}
	 */
	public static String generateSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.toLowerCase().charAt(index));
		}

		return salt.toString();

	}
	
	/**
	 * Method Will validate if a string is an email
	 * @param email
	 * @return {@link Boolean}
	 */
	public boolean emailValidator(String email) {
		if (email != "" && email != null) {
			final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = EMAIL_REGEX.matcher(email);
			return matcher.find();
		} else {
			logger.error("Field email have a null value in it.", new RuntimeException("At least one attributes returned a null value."));
			return false;
		}

	}
	
	/**
	 * Method will convert from {@link Date} to {@link String}
	 * @param date
	 * @return {@link String}
	 */
	public String getTheTime(Date date) {
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return sdf.format(date);
	}

	/**
	 * Method will calculate file size and convert it from bytes to megabytes  and return the modified  {@link FileInfo}
	 * @param fileInfo
	 * @param userId
	 * @return {@link FileInfo}
	 */
	public FileInfo prepareFileInfo(FileInfo fileInfo, UUID userId) {
		try {
			double bytes = Double.valueOf(fileInfo.getSize());
			double kilobytes = (bytes / 1024);
			double megabytes =  (kilobytes / 1024);
			
			fileInfo.setUserId(userId);
			fileInfo.setUploadTime(new Date());
			fileInfo.setSize(Math.floor(megabytes * 100) / 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileInfo;
	}
	
	/**
	 * Method will upload file by a user request to the user's folder.
	 * If global @param fileRecovery is set to be true (configured in application.properties file) 
	 * the Files recovery system will be activated 
	 * @param multipartFile
	 * @param user
	 * @return {@link Boolean}
	 */	
		public  boolean upaload(MultipartFile multipartFile, String user) {
			try {
				File originalDestination = new File(Constants.BASE_FILES_FOLDER + user + "/" + multipartFile.getOriginalFilename());
				multipartFile.transferTo(originalDestination);
				if(fileRecovery) {
					File recoveryDestination = new File(Constants.RECOVERY_FILES_FOLDER + "/" + multipartFile.getOriginalFilename());
					FileUtils.copyFile(originalDestination, recoveryDestination);
				}
			} catch (IOException e) {
				logger.error("Faild to create new file, Already exist in repository: " + Constants.BASE_FILES_FOLDER + user + "\\" + multipartFile.getOriginalFilename());
				return false ;
			}
			return true;
		}

	/**
	 * Method will encode a string with {@link BCrypt} encoding method (Can be used for password hashing)
	 * @param password
	 * @return {@link String}
	 */
	public  String encryptPassword(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

}
