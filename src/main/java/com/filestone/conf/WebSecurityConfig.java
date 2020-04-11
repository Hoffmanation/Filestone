package com.filestone.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;

/**
 * A Web security configuration class for determining web behaviour,Authorities and user roles
 * @author Hoffman
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	/*
	 *Spring Dependency Injection 
	 */
    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * Creates a {@link BCryptPasswordEncoder} Spring-Bean to be 
     * used when ever encryption  is need 
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     *Main Web security configuration method 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	  http
    	     .authorizeRequests()
    	     	 //Permit all HTTP requests that trying to get a static resource (besides index.html) 
    	         .antMatchers("/resources/**", "/registration.html" ,"/reset.html","/filestone/**" ,"/static/**").permitAll() 
    	         //Authenticate every HTTP request that trying to get to the main application page (index.html) by checking ROLE-USER
    	         .antMatchers("/index.html").hasRole("USER")
    	         .anyRequest().authenticated()
    	         //Register a login page and a Success URL to be redirected to (upon successful user login) 
    	         .and()
    	         .formLogin()
    	         .loginPage("/login.html").defaultSuccessUrl("/index.html")
    	         //Permit all HTTP requests for the .formLogin() section
    	         .permitAll()
    	         //Register a logout URL and a logout Success URL to be redirected to (upon successful user logout) 
    	         .and()
    	         .logout()
    	         .logoutUrl("/filestone/logout")
    	         //Register a custom logout filter listener (Uncomment the lines below if you want to use your 
    	         //own custom logout listener class instead of a Logout {@link @RestController})
    	         
				//.logoutRequestMatcher(new AntPathRequestMatcher("/filestone/logout")) 
				//.addLogoutHandler(new FilestoneLogoutHandler()) 
    	         .logoutSuccessUrl("/login.html")
    	         .permitAll()
    	         //Add custom filer {@link FileStoneFilter} for managing CSRF-TOKEN right after Spring's own -  {@link CsrfFilter}
    	         .and().addFilterAfter(new FileStoneFilter(),CsrfFilter.class)
    	         //Allow <iframe> to be open if the request  came from the same origin
    	         .headers().frameOptions().sameOrigin();
    	  

    }

    /*
     * Via injection we are configuring {@link AuthenticationManagerBuilder} 
     * to use {@link UserDetailsService} as the in-memoty authentication provider
     * and {@link BCryptPasswordEncoder} as the password encryptor. 
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    
    

    
}