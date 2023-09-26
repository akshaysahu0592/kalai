package com.tcit.vms.vms.payload.response;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Date;
import java.util.List;
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int staffId;
    private String staffName;

    private String email;
    private Integer roleId;

    private Date createddate;
    private List<String> department;
    private List<String> roles;

    public JwtResponse(String type) {
        this.type = type;
    }

    public JwtResponse(String token, String type, Integer staffId, String staffName,String email, Integer roleId, List<String> roles) {
        this.token = token;
        this.type=type;
        this.staffId = staffId;
        this.staffName=staffName;
        this.email = email;
        this.roleId = roleId;
        this.roles=roles;

    }



    public JwtResponse(String password, String username, Collection<? extends GrantedAuthority> authorities, List<String> roles) {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public int getUserId() {
        return staffId;
    }

    public void setUserIdId(int userId) {
        this.staffId = userId;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}

