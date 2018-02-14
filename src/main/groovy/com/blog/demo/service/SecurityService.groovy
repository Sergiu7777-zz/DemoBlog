package com.blog.demo.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SecurityService {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserDetailsService userDetailsService


    String findLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName()
            return currentUserName
        }
    }


    void autoLogin(String username, String password) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities())

        authenticationManager.authenticate(usernamePasswordAuthenticationToken)

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken)
        }
    }
}
