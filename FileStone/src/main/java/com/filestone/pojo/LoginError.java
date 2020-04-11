package com.filestone.pojo;

/*
 *A helper-pojo for sending error message to the front-end user 
 * @author Hoffman
 *
 */
public class LoginError {

	private String notEmpty;
	private String sizeUsername;
	private String duplicateUsername;
	private String sizePassword;
	private String dontMatchPasswordConfirm;
	private String emailNotValid;
	private String globalMessage;

	public LoginError() {
		// TODO Auto-generated constructor stub
	}

	public LoginError(String notEmpty, String sizeUsername, String duplicateUsername, String sizePassword,
			String dontMatchPasswordConfirm, String emailNotValid, String globalMessage) {
		super();
		this.notEmpty = notEmpty;
		this.sizeUsername = sizeUsername;
		this.duplicateUsername = duplicateUsername;
		this.sizePassword = sizePassword;
		this.dontMatchPasswordConfirm = dontMatchPasswordConfirm;
		this.emailNotValid = emailNotValid;
		this.globalMessage = globalMessage;
	}

	public String getNotEmpty() {
		return notEmpty;
	}

	public void setNotEmpty(String notEmpty) {
		this.notEmpty = notEmpty;
	}

	public String getSizeUsername() {
		return sizeUsername;
	}

	public void setSizeUsername(String sizeUsername) {
		this.sizeUsername = sizeUsername;
	}

	public String getDuplicateUsername() {
		return duplicateUsername;
	}

	public void setDuplicateUsername(String duplicateUsername) {
		this.duplicateUsername = duplicateUsername;
	}

	public String getSizePassword() {
		return sizePassword;
	}

	public void setSizePassword(String sizePassword) {
		this.sizePassword = sizePassword;
	}

	public String getDontMatchPasswordConfirm() {
		return dontMatchPasswordConfirm;
	}

	public void setDontMatchPasswordConfirm(String dontMatchPasswordConfirm) {
		this.dontMatchPasswordConfirm = dontMatchPasswordConfirm;
	}

	public String getEmailNotValid() {
		return emailNotValid;
	}

	public void setEmailNotValid(String emailNotValid) {
		this.emailNotValid = emailNotValid;
	}

	public String getGlobalMessage() {
		return globalMessage;
	}

	public void setGlobalMessage(String globalMessage) {
		this.globalMessage = globalMessage;
	}




}
