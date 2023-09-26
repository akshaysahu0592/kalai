package com.tcit.vms.vms.config;

import com.tcit.vms.vms.filter.AuthTokenFilter;
import com.tcit.vms.vms.jwt.AuthEntryPointJwt;
import com.tcit.vms.vms.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    private AuthEntryPointJwt unauthorizedHandler;

    private Boolean enableSwagger = true;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
   @Bean
    public PasswordEncoder passwordEncoder() {
      // return NoOpPasswordEncoder.getInstance();
         return new BCryptPasswordEncoder(12);
    }
    private static final String[] AUTH_WHITELIST = {
            "/api/Auth/Authentication",
            "/api/GetAllCampusList",
            "/api/GetAllVisitorTypeList",
            "/api/GetAllDepartmentList",
            "/api/GetStaffsList",
            "/api/GetStaffsByDepartmentIdList/**",
            "/api/GetStaffByStaffIdList/**",
            "/api/createVisitorDetails",
            "/api/forgot-password",
            "/api/reset-password",
            "/api/GetVisitByVisitId",
            "/api/image/**",
            "/api/image",
            "api/approveOrRejectVisitorByHost",
            "api/approveOrRejectVisitorBySecurity",
            "/api/Auth/test",
            "/swagger-ui",
            "/api/enroll",
            "api/GetAllVisitsForTab",
            "api/GetAllReasonList",
            "/api/AddReason",
            "/api/DeleteReason",
            "/swagger-ui/**"

    };
    @Bean
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }
    @Bean
    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @Override
    public void configure (HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .deny()
                .and()

                .authorizeRequests((authorize) -> {
                    try {
                        authorize
                                .antMatchers(AUTH_WHITELIST).permitAll()
                                //.and()
                                .anyRequest()
                                .permitAll();
                                //.and()
                                //.oauth2Login();


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    /*public void addInterceptors(InterceptorRegistry interceptorRegistry)
    {
        interceptorRegistry.addInterceptor(jwtInterceptor);
    }*/
}
