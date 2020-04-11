package com.filestone.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This Entity class will be use to store upload file information in the DB
 * @author Hoffman
 *
 */
@Entity
@Table(name = "FILE_INFO")
public class FileInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id ;
	private String fileName;
	private String type;
	private Date uploadTime;
	private String description;
	private Double size;
	private UUID userId;

	public FileInfo() {
		// TODO Auto-generated constructor stub
	}

	public FileInfo(String fileName, String type, Date uploadTime, String description, Double size) {
		super();
		this.fileName = fileName;
		this.type = type;
		this.uploadTime = uploadTime;
		this.description = description;
		this.size = size ;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}


	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", fileName=" + fileName + ", type=" + type + ", uploadTime=" + uploadTime
				+ ", description=" + description + ", size=" + size +  ", userId=" + userId
				+ "]";
	}

	
}
