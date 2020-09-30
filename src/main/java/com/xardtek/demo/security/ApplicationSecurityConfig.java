package com.xardtek.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.xardtek.demo.security.ApplicationUserRole.ADMIN;
import static com.xardtek.demo.security.ApplicationUserRole.ADMINTRAINEE;
import static com.xardtek.demo.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Enable code level annotation with this line of code!
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  //Enable this line when usig with front-end framework(Angular,React etc) for form posting
                .csrf().disable() //Disable for Testing purposes
               // .and()
                .authorizeRequests() // Authorize Request
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll() // Permit all antMatchers above
                //Commented out to enable code level annotation with PreAuthorize
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission()) //COURSE_WRITE PERMISSIONS FOR USERS WITH AUTHORITY/PERMISSION
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())//COURSE_WRITE PERMISSIONS FOR USERS WITH AUTHORITY/PERMISSION
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())//COURSE_WRITE PERMISSIONS FOR USERS WITH AUTHORITY/PERMISSION
//                .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())//ADMINTRAINEE PERMISSIONS FOR USERS WITH ROLE
                .anyRequest()        //Any Request
                .authenticated()    //USER MUST BE Authenticated
                .and()
                .httpBasic(); // HTTP BASIC NB: Downside to Basic HTTP -> You can not log out!!!!
    }

    /* This part handles In-memory, JDBC, UserDetails Caching overriding httpBasic with
    autogenerated user =='user' and password =='auto generated encoded-password' !!!
    */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        //IMPLEMENTING IN-MEMORY AUTHENTICATION WITH AUTHORIZATION
        UserDetails xardUser1 = User.builder()   // INVOKE USER FROM THE USER DETAILS CLASS & BUILD A USER
                .username("xarduser1")
// .password("password")  // Passing plain password like this will be rejected! After Password Encode has been Enabled!
                .password(passwordEncoder.encode("123456")) //Invoke password Encoder method HERE!!!
                //  .roles(STUDENT.name())   // STATIC ROLE_APP-USER
                .authorities(STUDENT.getGrantedAuthorities()) // GRANTED ROLES & AUTHORITY, IMPL IN ApplicationUserRole
                .build();

        UserDetails adminUser = User.builder()   // INVOKE USER FROM THE USER DETAILS CLASS & BUILD A USER
                .username("adminuser1")
//                .password("password")  // Passing plain password like this will be rejected! After Password Encode has been Enabled!
                .password(passwordEncoder.encode("123456")) //Invoke password Encoder method HERE!!!
                // .roles(ADMIN.name())   // STATIC ROLE_APP-USER
                .authorities((ADMIN.getGrantedAuthorities()))
                .build();

        UserDetails xardUser2 = User.builder()   // INVOKE USER FROM THE USER DETAILS CLASS & BUILD A USER
                .username("xarduser2")
                //.password("password")  // Passing plain password like this will be rejected! After Password Encode has been Enabled!
                .password(passwordEncoder.encode("123456")) //Invoke password Encoder method HERE!!!
                // .roles(ADMINTRAINEE.name())   // STATIC ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                adminUser, xardUser1, xardUser2
        );
    }
}