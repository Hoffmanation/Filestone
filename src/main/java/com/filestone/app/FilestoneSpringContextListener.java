package com.filestone.app;




import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.filestone.util.FileUploadUtil;

/**
 * A Spring Context listener class that will initialized right after the embedded tomcat
 * container is fully running and ready to serve HTTP requests and the spring application context was fully
 * initialized and ready to serve all Spring-Beans.
 * 
 * @author Hoffman
 *
 */
@Component
public class FilestoneSpringContextListener implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = LogManager.getLogger(FilestoneSpringContextListener.class);


	/**
	 * Method will be called right after application startup and after spring {@link ApplicationContext} is ready
	 * to serve any {@link Bean} requested
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Attempting to create the app resource  directory");
		//Creates The Application 'resources folder' to be use when a user upload/delete Files.
		FileUploadUtil.createAppResourcesFolder() ;
		
		logger.info("Attempting to create app file recovery resource directory");
		//Creates The Application 'file recovery folder' which can be use when a user upload a file to he's 
		//repository, delete it and request to recover it.
		//Additionally - the  system will store the file and will delete it when it passed the time limit configured (in application.properties) .
		FileUploadUtil.createAppRecoveryResourcesFolder();
	}


}
