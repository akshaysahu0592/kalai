package com.tcit.vms.vms.service;

import com.tcit.vms.vms.model.Staff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    public static final Long serialversionUID = 1L;
    private Integer id;
    private String name;
    private String mobileNo;
    private String email;
    private String password;
    private Integer departmentId;
    private Integer roleId;
    private String designation;
    private Date createdDate;
    private boolean isActive=true;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String name, String mobileNo,
                           String email, String password, Integer departmentId,
                           Integer roleId, String designation, Date createdDate,
                           boolean isActive, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.mobileNo = mobileNo;
        this.email = email;
        this.password = password;
        this.departmentId = departmentId;
        this.roleId = roleId;
        this.designation = designation;
        this.createdDate = createdDate;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    private UserDetailsImpl(Integer id,String name, String email, Integer roleId,String designation,String password,
                            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name=name;
        this.email = email;

        this.roleId=roleId;
        this.designation=designation;
        this.password=password;
        this.authorities = authorities;
    }
    public static UserDetailsImpl build(Staff staff) {
        //"USER,ADMIN,READ,WRITE,VIEW"

        //List<GrantedAuthority> authorities = Arrays.stream(staff.getRole(ro(new String{"1", "2"}))
        List<GrantedAuthority> authorities = Arrays.stream(staff.getDesignation().split(","))

                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                staff.getId(),
                staff.getStaffName(),
                staff.getEmail(),
                staff.getRole().getId(),
                staff.getDesignation(),
                staff.getPassword(),
                authorities
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    @Override
    public String getUsername() {
        return name;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }}

