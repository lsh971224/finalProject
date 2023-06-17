package com.blue.bluearchive.config;

import com.blue.bluearchive.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(memberService);
        authProvider.setPasswordEncoder(passwordEncoder());

        http.authenticationProvider(authProvider);
        http.csrf().disable();
        http.formLogin()
                .loginPage("/member/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/member/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
        ;
        http.authorizeRequests()
//                .mvcMatchers("/", "/member/**","/health/**","/item/**","/shop/**","/healthContent/**","/user/**","/comm/**").permitAll()
//                .mvcMatchers("/", "/member/**","/health/**","/healthContent/**","/user/**","/board/**").permitAll()
//                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
//                .mvcMatchers("/admin/**").hasRole("ADMIN")
//                .mvcMatchers("/member/**").hasRole("ADMIN")
                .anyRequest().permitAll()
        ;
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}

