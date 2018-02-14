package com.blog.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant

@Document(collection = "posts")
class BlogPost {

    @Id
    String id

    String title

    Instant date

    String body

    String permalink

    String author

    List comments

    List tags

    BlogPost() {}
}
