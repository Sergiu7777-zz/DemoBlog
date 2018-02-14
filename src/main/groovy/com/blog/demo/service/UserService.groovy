package com.blog.demo.service

import com.blog.demo.model.User
import com.blog.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    UserRepository userRepository

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder

    void save(User user) {
        user.password = bCryptPasswordEncoder.encode(user.password)

        userRepository.save(user)
    }

    User findByUsername(String username) {
        return userRepository.findOne(username)
    }
}
