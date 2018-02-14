package com.blog.demo.service

import com.blog.demo.model.User
import com.blog.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository

    @Override
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(username)
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>()
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"))

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities)
    }
}
