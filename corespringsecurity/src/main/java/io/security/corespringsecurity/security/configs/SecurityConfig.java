package io.security.corespringsecurity.security.configs;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.security.corespringsecurity.factory.UrlResourceMapFactoryBean;
import io.security.corespringsecurity.metadatasource.UrlFilterInvocationMetadataSource;
import io.security.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import io.security.corespringsecurity.security.handler.CustomAcessDeniedHandler;
import io.security.corespringsecurity.security.handler.CustomAuthenticationFailureHandler;
import io.security.corespringsecurity.security.handler.CustomAuthenticationSuccessHandler;
import io.security.corespringsecurity.security.provider.CustomAuthenticationProvider;
import io.security.corespringsecurity.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


//	@Autowired
//	private UserDetailsService userDetailsService;

	@Autowired
	private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler authenticationFailureHandler;

	@SuppressWarnings("rawtypes")
	@Autowired
	private AuthenticationDetailsSource authenticationDetailsSource;

	@Autowired
	private SecurityResourceService securityResourceService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService);

		auth.authenticationProvider(authenticationProvider());
	}



	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/users","/user/login/**","/login").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated()

        .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                ;
        http
        		.exceptionHandling()
        		.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        		.accessDeniedPage("/denied")
        		.accessDeniedHandler(accessDeniedHandler())
        		;

        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http.csrf().disable();
    }

    @Bean
	public AccessDeniedHandler accessDeniedHandler() {
    	CustomAcessDeniedHandler accessDeniedHandler = new CustomAcessDeniedHandler();
    	accessDeniedHandler.setErrorPage("/denied");
		return accessDeniedHandler;
	}

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
    	FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();

    	filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadatasource());
    	filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
    	filterSecurityInterceptor.setAuthenticationManager(authenticationManager());

    	return filterSecurityInterceptor;
    }

	public AccessDecisionManager affirmativeBased() {
		AffirmativeBased affirmativeBased = new AffirmativeBased(getAccessDecisionVoter());
		return affirmativeBased;
	}

	private List<AccessDecisionVoter<?>> getAccessDecisionVoter() {
		return Arrays.asList(new RoleVoter());
	}

	public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadatasource() throws Exception {
		return new UrlFilterInvocationMetadataSource(urlResourceMapFactoryBean().getObject());
	}



	private UrlResourceMapFactoryBean urlResourceMapFactoryBean() {

		UrlResourceMapFactoryBean urlResourceMapFactoryBean = new UrlResourceMapFactoryBean();
		urlResourceMapFactoryBean.setSecurityResourceService(securityResourceService);

		return urlResourceMapFactoryBean;
	}
}
