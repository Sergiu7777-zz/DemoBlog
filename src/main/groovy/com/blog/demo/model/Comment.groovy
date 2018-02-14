package com.blog.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

class Comment {

    String author

    String body

    String email

    Comment() {}

    Comment(String author, String body, String email) {
        this.author = author
        this.email = email
        this.body = body
    }
}
