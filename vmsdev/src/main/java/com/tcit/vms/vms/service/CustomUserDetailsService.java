package com.tcit.vms.vms.service;
/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

    @Service
    public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private LdapTemplate ldapTemplate;
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            DirContextOperations userData;
            userData = ldapTemplate.searchForContext(
                    "ou=users", "(sAMAccountName=" + username + ")",
                    (context) -> context
            );

            if (userData == null) {
                throw new UsernameNotFoundException("User not found");
            }

            // Extract user details from the userData
            String userDn = userData.getStringAttribute("distinguishedName");
            String password = "someDummyPassword"; // Not needed for LDAP authentication

            return User.withUsername(username)
                    .password(password)
                    .roles("USER")
                    .authorities("ROLE_USER")
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        }
    }*/

/*import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class CustomUserDetailsService extends LdapUserDetailsMapper implements UserDetailsService {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        // Map LDAP attributes to UserDetails fields
        UserDetails userDetails = super.mapUserFromContext(ctx, username, authorities);

        // You can customize UserDetails further here

        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement this method if needed for custom user lookup
        throw new UsernameNotFoundException("Custom user lookup not implemented");
    }
}*/


