package com.filestone.service.impl;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.filestone.service.UserService;
import com.filestone.util.AppUtil;
import com.filestone.util.Constants;
import com.filestone.entity.Users;
import com.filestone.pojo.MailMessageRequest;

/**
 * This Service class implements Email Messaging services to be use by the 'Forgot my password flow'.
 * 
 * @author Oren Hoffman
 * @date 28/6/2017
 *
 */

@Component
//Please create your own 'email.properties' file with your email credentials as mine with 'git ignore'.
@PropertySource("classpath:email.properties")
public class EmailServiceImpl {
	private static final Logger log = Logger.getLogger(EmailServiceImpl.class.getSimpleName());

	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private UserService userStub;
	@Autowired
	private AppUtil filestoneUtil ;
	@Autowired
	private AccessTokenServiceImpl  accessTokenService ;
	
	
	/*
	 * Retrieving Environment Variables for email credentials
	 */
	@Value("${username}")
	private String username;
	@Value("${password}")
	private String password;


/**
 * Method will send mail by a given {@link MailMessageRequest}  - (Configured for smtp.gmail.com)
 * @param messageRequest
 * @return {@link Boolean}
 */
	public boolean sendMail(MailMessageRequest messageRequest) {

		//Validate the user's email
		if (!filestoneUtil.emailValidator(messageRequest.getSentTo())) {
			return false;
		}

		//Please notice that email @java.util.Properties configured to be used with Gmail account, Configured as you wish.
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.setProperty("mail.transport.protocol", "smtp");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username,password);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "Filestone Application"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(messageRequest.getSentTo()));
			message.setSubject(messageRequest.getSubject());
			message.setText("swswsw");
			message.setContent(messageRequest.getBody(), "text/html");
			message.setReplyTo(messageRequest.getReplayTo());
			Transport.send(message);
			log.info("An e-mail has been sent to " + messageRequest.getSentTo() + " at --> " + new Date());
		} catch (Exception e) {
			log.info("Reset password e-mail to ------> " + messageRequest.getSentTo() + " has faild.", e);
			return false;
		}
		return true;
	}




/**
 * Method will prepare the 'Reset Password email' template to be send
 * @param sendConformationMailTo
 * @return {@link MailMessageRequest}
 */
	public MailMessageRequest prepareResetPasswordMail(String sendConformationMailTo) {
		Users temp = userStub.findByUsername(sendConformationMailTo);
		if (temp == null) {
			return null;
		}
		//Generate Access-Token
		String token = AppUtil.generateSaltString();
		//Inject the 'reset.html' end-point and the Access-Token in the URL and Create the template to be sent
		MailMessageRequest message = new MailMessageRequest(sendConformationMailTo,
				"Reset password for your Filestone account",
				"<h1>To reset your password please click <a href=\"" + Constants.BASE_URL + "reset.html?" + token + "?"
						+ sendConformationMailTo + "\">here</a></h1>",
				"donotrelplay@filestone.com");
		//Register the Access-Token
		accessTokenService.addAccessToken(sendConformationMailTo, token);
		//Send Mail to user
		return message ;

	}
	
	/**
	 * Method will send a 'Reset Password Mail' email by a given {@link MailMessageRequest} 
	 * @param messageRequest
	 * @return {@link Boolean}
	 */
	public boolean sendResetPasswordMail(String sendConformationMailTo) {
		MailMessageRequest message = prepareResetPasswordMail(sendConformationMailTo) ;
		if (message == null) {
			return false ;
		}
		return sendMail(message);
	}




}
