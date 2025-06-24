package org.swiftboot.demo.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Custom user details object for SpringSecurity.
 *
 * @since 3.0
 */
public class CustomUserDetails extends User {

    /**
     * User ID
     */
    private String id;

    /**
     * User Role
     */
    private String role;

    public CustomUserDetails(String id, String username, String password, String role, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.role = role;
    }

    public CustomUserDetails(String id, String username, String password, String role, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
