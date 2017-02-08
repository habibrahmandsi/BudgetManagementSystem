package com.macrosoft.bms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

/*    @Bean
    @Primary
    public DataSource dataSource() {
        System.out.println(" *************** dataSource 22 *************** ");
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://103.245.204.114:5432/ediscovery")
                .driverClassName("org.postgresql.Driver")
                .username("postgres")
                .password("ninja")
                .build();
   }*/

   /* @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }*/

/*    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/scripts*").permitAll()
                .antMatchers("/styles*").permitAll()
                .antMatchers("/images*").permitAll()
                .antMatchers("/admin/**").access("hasAnyRole('ROLE_ADMIN')")
//              .anyRequest().authenticated()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .loginPage("/login").permitAll()
                .loginPage("/forgotPassword").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxAwareAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/403")
                .and()
                .csrf().disable();
    }

}
