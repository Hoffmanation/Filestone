package com.filestone.pojo;

/**
 * RepositoryInfo POJO will be used as a custom response for the /getRepositoryInfo REST API.
 * @author Hoffman
 */
public class RepositoryInfo {

	private String totalSize;
	private String fileQuantity;
	private String lastLogin;
	private String username;

	public RepositoryInfo() {
		// TODO Auto-generated constructor stub
	}

	public RepositoryInfo(String totalSize, String fileQuantity, String lastLogin, String username) {
		super();
		this.totalSize = totalSize;
		this.fileQuantity = fileQuantity;
		this.lastLogin = lastLogin;
		this.username = username;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	public String getFileQuantity() {
		return fileQuantity;
	}

	public void setFileQuantity(String fileQuantity) {
		this.fileQuantity = fileQuantity;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "RepositoryInfo [totalSize=" + totalSize + ", fileQuantity=" + fileQuantity + ", lastLogin=" + lastLogin
				+ ", username=" + username + "]";
	}



}
