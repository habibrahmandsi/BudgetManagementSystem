package com.icontrolesi.envity.config;

import com.icontrolesi.envity.data.dao.UserDAO;
import com.icontrolesi.envity.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("*************** Load User By Username ***************");
        List<String> roles = new ArrayList<String>();

        if(StringUtils.isEmpty(userName))
            throw new UsernameNotFoundException("User name is empty");

        User user = userDAO.getUserByUserName(userName);
        if(user != null && user.isEnabled()){
            roles.add(user.getRole());
            System.out.println("roles:"+roles.toString());
            CustomUserDetails userDetails = new CustomUserDetails(userName, user.getPassword(), getAuthorities(roles));
            userDetails.setFullName(user.getFirstName()+" "+user.getLastName());
            return userDetails;
        }else {
            System.out.println("User not valid!!");
            throw new UsernameNotFoundException("could not find the user '" + userName + "'");
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

}