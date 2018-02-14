package com.blog.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {

    @Id
    String username

    String password

    @Transient
    String passwordConfirm

    String email

    User() {}
}
