package com.blog.demo.service

import com.blog.demo.model.BlogPost
import com.blog.demo.model.Comment
import com.blog.demo.repository.BlogPostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

import java.time.Instant

@Service
class BlogPostService {

    @Autowired
    BlogPostRepository blogPostRepository

    @Autowired
    SecurityService securityService

    @Autowired
    MongoTemplate template

    @Autowired
    BlogPostService blogPostService

//show blog posts by date descending
    List<BlogPost> findByDateDescending() {
        PageRequest request = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "date"))
        List<BlogPost> blogPostList = blogPostRepository.findByDateDescending(request).getContent()

        return blogPostList
    }

//save a new blog post
    BlogPost savePost(BlogPost post) {
        String title = post.getTitle()

        String permalink = title.replaceAll("\\s", "_")
        permalink = permalink.replaceAll("\\W", "")
        permalink = permalink.toLowerCase()

        String permalinkExtra = Instant.now().toEpochMilli().toString()
        permalink = permalink + permalinkExtra

        post.setPermalink(permalink)
        post.setDate(Instant.now())
        post.setAuthor(securityService.findLoggedInUsername())

        blogPostRepository.insert(post)

        return post
    }


    List updateComments(String author, String body, String email, String permalink) {
        BlogPost blogPost = blogPostRepository.findByPermalink(permalink)

        List commentsList = blogPost.getComments()
        Comment comment = new Comment(author, body, email)

        Update update = new Update()
        update.push("comments", comment)
        Criteria criteria = Criteria.where("permalink").is(permalink);
        template.updateFirst(Query.query(criteria), update, "posts")

        return commentsList
    }

}
