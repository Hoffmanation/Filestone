package com.filestone.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.filestone.entity.FileInfo;
import com.filestone.handler.FilestoneMediaFileException;
import com.filestone.pojo.RepositoryInfo;
import com.filestone.service.manager.FilestoneServiceManager;

/**
 * A Collection of {@link RestController} class that will accept HTTP request to
 * interact with the application JS client, A Collection of REST-API's for {@link FileInfo} ,
 * {@link RepositoryInfo} and the files media content (as a
 * {@link ResponseEntity<UrlResource>}) CRUD operations
 * 
 * @author Hoffman
 *
 */
@RestController
public class FilestoneFilesOpController {

	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private FilestoneServiceManager filestoneServiceManager;

	
	@RequestMapping(path = "fs/uploadFile", method = RequestMethod.POST, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
		return filestoneServiceManager.upload(file, request, session);
	}

	@RequestMapping(path = "fs/createFileInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response regiesterFile(@RequestBody FileInfo fileInfoRes, HttpSession session, HttpServletRequest request) {
		return filestoneServiceManager.createFileInfo(fileInfoRes, session, request);

	}

	@RequestMapping(value = "fs/downloadFile/{num}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFile(HttpServletResponse response, @PathVariable("num") String num,
			HttpSession session) throws IOException {
		return filestoneServiceManager.downloadFile(response, num, session);

	}

	@RequestMapping(path = "fs/getRepositoryInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response getRepositoryInfo(HttpSession session) {
		return filestoneServiceManager.getRepositoryInfo(session);

	}

	@RequestMapping(path = "fs/getAllFiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public FileInfo[] getAllFiles(HttpSession session) {
		return filestoneServiceManager.getAllFiles(session);
	}

	@RequestMapping(value = "fs/deleteFile/{num}", method = RequestMethod.DELETE)
	public Response deleteFile(HttpServletResponse response, @PathVariable("num") String num, HttpSession session)
			throws IOException {
		return filestoneServiceManager.deleteFile(response, num, session);
	}

	@RequestMapping(value = "fs/forgotMyPassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Response sendConformationMailTo(@RequestBody String sendConformationMailTo, @Context HttpServletResponse res,
			HttpServletRequest req ,HttpSession session) {
		return filestoneServiceManager.sendResetPasswordMail(sendConformationMailTo, res,req, session);

	}

	@RequestMapping(path = "fs/getPreviewTag/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Response getPreviewTag(@PathVariable("id") String id, @Context HttpServletRequest req, HttpSession session) {
		return filestoneServiceManager.getPreviewTag(id, req, session);

	}

	@RequestMapping(path = "fs/getContent/{name}/videos", method = RequestMethod.GET)
	public ResponseEntity<UrlResource> getFullVideo(@PathVariable("name") String name, @Context HttpServletRequest req,HttpSession session) throws FilestoneMediaFileException {
		return filestoneServiceManager.getVideoContent(name, req, session);
	}
}
