package com.blog.demo.repository

import com.blog.demo.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentsRepository extends MongoRepository<Comment, String> {

}