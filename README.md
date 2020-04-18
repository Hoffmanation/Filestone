# Filestone Application
This Java Spring-Boot application is a Media File 'Store And Display' Application.
The end user have he's own account in which he can upload,delete and display almost any file type.
Ther's also an option for retrieving a deleted file from the application recovery system 

## Git Branches
- master - For Deploying a Spring-Boot application as a Tomcat war/ Embedded Tomcat jar  files
- filstone-wildfly - For deploying a Spring-Boot application as a Wildfly/Jboss (or any JEE application server) war file

# Module Major Dependencies
- Spring-Boot V1.5.10.RELEASE
- Spring-Security V1.5.10.RELEASE
- Spring-Date V2.0.0.RELEASE
- Java-javax.mail V1.5.0-b01
- Apache-commons-lang3 V3.1
- Apache-commons-io V2.5

# Server Specifications
- Java Maven project
- Spring-Boot
- Persistence - Spring-JPA-repository
- Security - Spring-Security


# Client Specifications
- AngularJs + angular-cookies - V1.0.8
- jQuery,Bootstrap, Bootstrap-ui
- HTML,CSS


# Environment
 - Ubuntu/Windows
 
# Requirements
- JVM
- DB connection (Configured as development env to local PostgreSql)



## Running up Environment
- run maven install and retrieve the filestone.jar file
- java - jar filestone.jar

## Accessing Filestone application
-http://localhost:8090/

# .properties files explanation

#### .application.properties
    
	#MultipartFile Configuration
	spring.http.multipart.max-file-size= what will be the maximum allowed file size to be upload by any user.
	spring.http.multipart.max-request-size= what will be the maximum allowed request size to be upload by any user.
	spring.http.multipart.enabled= can enable or disable the multipart file spring bean from the system.
	spring.http.multipart.location= what will be the location of the multipart file to be copy.
	
	#Support For file recovery system
	filestone.file.recovery= can enable or disable the application file recovery system
	filestone.file.recovery.timelimit = what will be the time limit which the recovery system files will be stored.	
	
	server.port= what will be the port of the application
	
	
#### .email.properties
    
	#Email credentials
	username= the username for your email account
	password= the password  for your email account	
	
#### .messages.properties
    
	#Messages for front end user
	not.empty=* All fields are required.
	size.username= *Username have to be between 6 and 32 characters.
	duplicate.username= *Someone already has that username.
	size.password= *Password should be at least 8 characters.
	dont.match.passwordConfirm= *These passwords don't match.
	email.not.valid= *This email is not valid
	
	#Can be changed as you wish	
	

# Contact
- For any questions you can send a mail to orenhoffman1777@gmail.com