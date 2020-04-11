package com.filestone.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * A web Configuration class for determining static resources (HTML,JS,CSS and such) location
 * @author Hoffman
 *
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	//Array of possible static location inside the application classpath
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    /**
     * Main method to configure possible locations.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**") .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
    
    
}