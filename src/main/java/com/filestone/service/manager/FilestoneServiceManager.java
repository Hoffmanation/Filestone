package com.filestone.service.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.filestone.entity.FileInfo;
import com.filestone.entity.UserDetail;
import com.filestone.entity.Users;
import com.filestone.handler.FilestoneMediaFileException;
import com.filestone.pojo.Message;
import com.filestone.pojo.RepositoryInfo;
import com.filestone.service.FileinfoService;
import com.filestone.service.impl.EmailServiceImpl;
import com.filestone.util.AppUtil;
import com.filestone.util.Constants;
import com.filestone.util.FileUploadUtil;

/**
 * A service manager class that will handle the Business-Logic for
 * {@link FileInfo} , {@link RepositoryInfo} and the media files (returned as a
 * {@link ResponseEntity<UrlResource>}) Entities. This class serves us as the
 * layer that connect between the REST-API's Layer and the DAO layer for the
 * media content files/entities
 * 
 * @author Hoffman
 *
 */
@Service
public class FilestoneServiceManager {

	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private FileinfoService fileInfoService;
	@Autowired
	private EmailServiceImpl mailService;
	@Autowired
	private AppUtil appUtil;

	
	public Response upload(MultipartFile file, HttpServletRequest request, HttpSession session) {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		if (appUtil.upaload(file, userDetail.getUsername())) {
			return Response.status(200).entity(Status.OK.getReasonPhrase()).build();
		}
		return Response.status(210).entity(new Message("Faild to upload " + file.getOriginalFilename())).build();
	}

	public Response createFileInfo(FileInfo fileInfo, HttpSession session, HttpServletRequest request) {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		appUtil.prepareFileInfo(fileInfo, userDetail.getId());
		fileInfo = fileInfoService.createFileinfo(fileInfo);
		return Response.status(210).entity(new Message(fileInfo.toString())).build();
	}

	public ResponseEntity<byte[]> downloadFile(HttpServletResponse response, String num, HttpSession session)
			throws IOException {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		FileInfo info = fileInfoService.getFileInfoByFIleInfoId(UUID.fromString(num)).get(0);
		byte[] array = Files.readAllBytes(
				new java.io.File(Constants.BASE_FILES_FOLDER + userDetail.getUsername() + "//" + info.getFileName())
						.toPath());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "image/jpeg");
		headers.set("Content-Disposition", "attachment; filename=\"" + info.getFileName() + "");
		return new ResponseEntity<byte[]>(array, headers, HttpStatus.OK);
	}

	public Response getRepositoryInfo(HttpSession session) {
		Users user = (Users) session.getAttribute(Constants.USER_LOGIN);
		List<UserDetail> details = user.getDetails();
		String lastLogin = appUtil.getTheTime(new Date(System.currentTimeMillis()));
		if (details.size() > 1) {
			UserDetail detail = details.get(details.size() - 2);
			lastLogin = appUtil.getTheTime(detail.getLoginTime());
		}

		DecimalFormat df = new DecimalFormat("#.##");
		try {
			String totalSize = "";
			Double megabytes = 0.0;
			Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
			long fileQuantity = fileInfoService.getRowCountByUserId(userDetail.getId());
			List<Double> byteList = fileInfoService.getFilesSizeOnDisk(userDetail.getId());
			for (Double bs : byteList) {
				megabytes = megabytes + bs;
			}

			totalSize = megabytes > 1000 ? df.format(megabytes / 1024.0) + " GB" : df.format(megabytes) + " MB";
			RepositoryInfo repoInfo = new RepositoryInfo(totalSize, String.valueOf(fileQuantity), lastLogin,
					StringUtils.substringBefore(userDetail.getUsername(), "@"));
			return Response.status(200).entity(repoInfo).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public FileInfo[] getAllFiles(HttpSession session) {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		List<FileInfo> getAllFiles = fileInfoService.getAllFileInfoByUserId(userDetail.getId());
		return getAllFiles.toArray(new FileInfo[0]);
	}

	public Response deleteFile(HttpServletResponse response, String num, HttpSession session) throws IOException {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		String fileName = fileInfoService.getFileInfoByFIleInfoId(UUID.fromString(num)).get(0).getFileName();
		FileUploadUtil.delete(fileName, userDetail.getUsername());
		fileInfoService.deleteFileInfoById(UUID.fromString(num));
		return Response.status(200).entity(Status.OK.getReasonPhrase()).build();
	}

	public Response sendResetPasswordMail(String sendConformationMailTo, HttpServletResponse res, HttpServletRequest req,HttpSession session) {
		if (!appUtil.emailValidator(sendConformationMailTo)) {
			return Response.status(200).entity(new Message("*Plaese enter a valid email address")).build();
		}
		if (mailService.sendResetPasswordMail(sendConformationMailTo,req)) {
			return Response.status(200).entity(new Message("A confirmation link has been sent to your email address"))
					.build();
		}
		return Response.status(200).entity(new Message("*This email is not registered in our website")).build();
	}

	public Response getPreviewTag(String id, HttpServletRequest req, HttpSession session) {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		FileInfo info = fileInfoService.getFileInfoByFIleInfoId(UUID.fromString(id)).get(0);
		String preview = appUtil.getFIlePreview(info.getType(), userDetail.getId(), info.getFileName());
		return Response.status(200).entity(preview).build();
	}

	public ResponseEntity<UrlResource> getVideoContent(String name, HttpServletRequest req, HttpSession session) throws FilestoneMediaFileException {
		Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
		UrlResource video = null;
		String requestedMediaFile = Constants.BASE_FILES_FOLDER + userDetail.getUsername() + "/" + name ;
		if (!FileUploadUtil.isMediaResourceExist(requestedMediaFile)) {
			throw new FilestoneMediaFileException(Constants.CANNOT_FIND_MEDIA_RESOURCE +", Requested resource: " + requestedMediaFile);
		}
		try {
			video = new UrlResource("file:///" + requestedMediaFile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "video/mp4");
		return new ResponseEntity<UrlResource>(video, headers, HttpStatus.OK);
	}

}
