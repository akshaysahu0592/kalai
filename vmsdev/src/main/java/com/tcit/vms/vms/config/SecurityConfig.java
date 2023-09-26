package com.tcit.vms.vms.config;
/*import com.tcit.vms.vms.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public UserDetailsService userDetailsService() {
            return new CustomUserDetailsService();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/public/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService());
        }*/

/*@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0})")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("member={0}")
                .contextSource()
                .url("ldap://your-ad-server-url:389/dc=example,dc=com")
                .managerDn("your-ldap-admin-username")
                .managerPassword("your-ldap-admin-password");
    }
}*/




