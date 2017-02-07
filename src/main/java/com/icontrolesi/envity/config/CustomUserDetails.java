package com.icontrolesi.envity.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    public CustomUserDetails(String username, String password,
         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    //for example lets add some person data        
    private String fullName;

    //getters and setters


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}