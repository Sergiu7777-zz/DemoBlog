package com.blog.demo.repository

import com.blog.demo.model.BlogPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BlogPostRepository extends MongoRepository<BlogPost, String> {

    @Query("{},{date: -1}")
    Page<BlogPost> findByDateDescending(Pageable pageable)

    @Query("{'tags' : ?0 },{date: -1}")
    List<BlogPost> findByTagDateDescending(String tag)

    @Query("{ 'permalink' : ?0 }")
    BlogPost findByPermalink(String permalink)

}