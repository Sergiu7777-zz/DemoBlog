package com.blog.demo.controller

import com.blog.demo.model.BlogPost
import com.blog.demo.model.Comment
import com.blog.demo.model.User
import com.blog.demo.repository.BlogPostRepository
import com.blog.demo.repository.CommentsRepository
import com.blog.demo.service.BlogPostService
import com.blog.demo.service.SecurityService
import com.blog.demo.service.UserService
import com.blog.demo.validator.UserValidator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class BlogController {

    @Autowired
    BlogPostRepository blogPostRepository

    @Autowired
    UserService userService

    @Autowired
    BlogPostService blogPostService

    @Autowired
    SecurityService securityService

    @Autowired
    UserValidator userValidator

    @Autowired
    CommentsRepository commentsRepository


    @ModelAttribute("currentUser")
    String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails == null) ? null : userDetails.getUsername()
    }


    @GetMapping("/login")
    String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.")

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.")

        return "login"
    }


    //add new user
    @GetMapping("/registration")
    String addUser(Model model) {
        model.addAttribute("userForm", new User())
        return "registration"
    }


    @PostMapping("/registration")
    RedirectView addUser(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult)

        if (bindingResult.hasErrors()) {
            return new RedirectView("/registration")
        }

        userService.save(userForm)
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm())
        return new RedirectView("/welcome")
    }


    //welcome page
    @GetMapping("/welcome")
    String welcome() {
        return "welcome"
    }


    //blog posts sorted by date descending
    @GetMapping("/posts")
    String postsByDateDescending(Model model) {
        model.addAttribute("posts", blogPostService.findByDateDescending())

        return "posts"
    }


    //find a post by permalink
    @GetMapping("/posts/{permalink}")
    String findByPermalink(Model model, Comment commentForm, @PathVariable("permalink") String permalink) {
        model.addAttribute("postPermalink", blogPostRepository.findByPermalink(permalink))
        model.addAttribute("commentForm", new Comment("", "", ""))

        return "postByPermalink"
    }


    @PostMapping("/posts/{permalink}")
    String addNewComment (Model model, Comment commentForm, @PathVariable("permalink") String permalink) {
        model.addAttribute("postPermalink", blogPostRepository.findByPermalink(permalink))

        BlogPost blogPost = blogPostRepository.findByPermalink(permalink)
        blogPost.setComments(blogPostService.updateComments(commentForm.author, commentForm.body, commentForm.email, permalink))

        return "redirect:/posts/{permalink}"
    }


    //add a new post
    @GetMapping("/newPost")
    String newPost(Model model) {
        model.addAttribute("postForm", new BlogPost())
        return "/newPost"
    }


    @PostMapping("/newPost")
    RedirectView newPost(@ModelAttribute("postForm") BlogPost postForm) {
        String permalink = ""
        try {
            blogPostService.savePost(postForm)
            permalink = postForm.getPermalink()
            println("Blog post successfully saved")
        } catch (Exception e) {
            println("Error saving new post")
            e.printStackTrace()
        }

        return new RedirectView("/posts/" + permalink)
    }


    @GetMapping("/logout")
    String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/login?logout"
    }


    //find posts by a specific tag
    @GetMapping("/tag/{tag}")
    String findByTagDateDescending(Model model, @PathVariable("tag") String tags) {
        model.addAttribute("postsByTag", blogPostRepository.findByTagDateDescending(tags))

        return "postsByTag"
    }

}
