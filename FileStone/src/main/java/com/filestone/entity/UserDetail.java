package com.filestone.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * This Entity class will be use to store the user detail information in the DB.
 * @author Hoffman
 *
 */
@Entity
@Table(name = "user_detail")
public class UserDetail {

	private UUID id;
	private String username;
	private String password;
	private String passwordConfirm;
	private java.util.Date loginTime;
	private java.util.Date logoutTime;
	private Users users;

	public UserDetail() {
		// TODO Auto-generated constructor stub
	}
	
	

	public UserDetail(Users users,String username, Date loginTime ,Date logoutTime) {
		super();
		this.users= users ;
		this.username = username;
		this.loginTime =loginTime ;
		this.logoutTime = logoutTime;
	}
	
	public UserDetail(String username, String password, String passwordConfirm) {
		super();
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Transient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Transient
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(java.util.Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}



	@Override
	public String toString() {
		return "UserDetail [loginTime=" + loginTime + ", logoutTime=" + logoutTime + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((loginTime == null) ? 0 : loginTime.hashCode());
		result = prime * result + ((logoutTime == null) ? 0 : logoutTime.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((passwordConfirm == null) ? 0 : passwordConfirm.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDetail other = (UserDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (loginTime == null) {
			if (other.loginTime != null)
				return false;
		} else if (!loginTime.equals(other.loginTime))
			return false;
		if (logoutTime == null) {
			if (other.logoutTime != null)
				return false;
		} else if (!logoutTime.equals(other.logoutTime))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (passwordConfirm == null) {
			if (other.passwordConfirm != null)
				return false;
		} else if (!passwordConfirm.equals(other.passwordConfirm))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
	
	
	
	
}