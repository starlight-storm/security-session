package com.example.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.handler.AppLogoutSuccessHandler;
import com.example.handler.DoubleLoginAuthenticationFailureHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	http
    		.authorizeRequests()
    			.antMatchers(GET, "/top.html").permitAll()
        		.antMatchers(GET, "/login.html").permitAll()
        		.antMatchers(POST, "/processLogin").permitAll()
        		.antMatchers(POST, "/logout").authenticated()
        		.antMatchers("/admin/**").hasRole("ADMIN")
        		.antMatchers("/user/**").authenticated()
        		.and()
			.formLogin()
				.loginPage("/login.html")
				.loginProcessingUrl("/processLogin")
				.defaultSuccessUrl("/top.html")
				//.failureUrl("/login.html")
				.failureHandler(new DoubleLoginAuthenticationFailureHandler())
				.usernameParameter("paramLoginId")
				.passwordParameter("paramPassword")
				.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(appLogoutSuccessHandler())
				.logoutSuccessUrl("/login.html")
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.and()
			.exceptionHandling()
			    .accessDeniedPage("/accessDenied.html")
			    .and()
			.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.disable() // disable()を外すと、今回のサンプルは大変なのでつけておく
			.sessionManagement()
		    	.maximumSessions(1)
		    	.maxSessionsPreventsLogin(true)
		    	.sessionRegistry(sessionRegistry());
	}

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    } 
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
    
    @Bean
    protected LogoutSuccessHandler appLogoutSuccessHandler() {
        return new AppLogoutSuccessHandler();
    }
    
    @Autowired
    private DataSource dataSource;

    private static final String USER_QUERY
        = "select LOGIN_ID, PASSWORD, true "
        + "from T_USER "
        + "where LOGIN_ID = ?";

    private static final String ROLES_QUERY
        = "select LOGIN_ID, ROLE_NAME "
        + "from T_ROLE "
        + "inner join T_USER_ROLE on T_ROLE.ID = T_USER_ROLE.ROLE_ID "
        + "inner join T_USER on T_USER_ROLE.USER_ID = T_USER.ID "
        + "where LOGIN_ID = ?";

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery(USER_QUERY)
                .authoritiesByUsernameQuery(ROLES_QUERY);
    }
}
