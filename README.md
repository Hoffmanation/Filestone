# Filestone Application
This Java Spring-Boot application is a Media File 'Store And Display' Application.
The end user have he's own account in which he can upload,delete and display almost any file type.
Ther's also an option for retrieving a deleted file from the application recovery system 

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


#### .properties files explanation
- .application.properties
    
	#MultipartFile Configuration
	spring.http.multipart.max-file-size= what will be the maximum allowed file size to be upload by any user.
	spring.http.multipart.max-request-size= what will be the maximum allowed request size to be upload by any user.
	spring.http.multipart.enabled= can enable or disable the multipart file spring bean from the system.
	spring.http.multipart.location= what will be the location of the multipart file to be copy.
	
	#Support For file recovery system
	filestone.file.recovery= can enable or disable the application file recovery system
	filestone.file.recovery.timelimit = what will be the time limit which the recovery system files will be stored.	
	
	server.port= what will be the port of the application
	
	
- .email.properties
    
	#Email credentials
	username= the username for your email account
	password= the password  for your email account	
	
- .messages.properties
    
	#Messages for front end user
	not.empty=* All fields are required.
	size.username= *Username have to be between 6 and 32 characters.
	duplicate.username= *Someone already has that username.
	size.password= *Password should be at least 8 characters.
	dont.match.passwordConfirm= *These passwords don't match.
	email.not.valid= *This email is not valid
	
	#Can be changed as you wish	
	
	
#### .env file example
    #postgres db settings
    DATABASE_HOST=127.0.0.1
    DATABASE_USER=postgres
    DATABASE_PASSWORD=postgres
    DATABASE_NAME=postgres
    DATABASE_SEARCH_PATH=public
    #env development | production
    NODE_ENV=development
    #app port
    PORT=3000
    #used for generating jwt token
    SECRET=s0m3su3rs3cr3tp4ssw0rd
    #JWT token expiry, can be 7d, 60 or whatever in seconds
    TOKEN_EXPIRES_IN=1d
    #used for cors policy
    FRONT_APP_DOMAIN=http://localhost:8080
    #root of the server
    JSON_CONFIGURATION_FILE_PATH='../jsonFile.json'
    
    ####EMAIL CONFIGURATION
    EMAIL_FROM='someEmail@gmail.com'
    # Gmail SMTP server Configuration
    GMAIL_SERVICE_NAME=gmail
    GMAIL_SERVICE_HOST=smtp.google.com
    GMAIL_SERVICE_SECURE=false
    GMAIL_SERVICE_PORT=587
    GMAIL_USER_NAME=someEmail@gmail.com
    GMAIL_USER_PASSWORD=someEmailGmailPassword
    
    # SMTP service configuration
    SMTP_SERVICE_HOST=
    SMTP_SERVICE_SECURE=
    SMTP_SERVICE_PORT=
    SMTP_USER_NAME=
    SMTP_USER_PASSWORD=	
    


# Contact
- For any questions you can send a mail to orenhoffman1777@gmail.com