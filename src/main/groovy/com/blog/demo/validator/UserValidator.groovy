package com.blog.demo.validator

import com.blog.demo.model.User
import com.blog.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator

@Component
class UserValidator implements Validator {

    @Autowired
    UserService userService

    @Override
    boolean supports(Class<?> aClass) {
        return User.class == aClass
    }

    @Override
    void validate(Object target, Errors errors) {
        User user = (User) target

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty")
        if (user.getUsername().length() < 3 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username")
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username")
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty")
        if (user.getPassword().length() < 3 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password")
        }

        if (user.getPasswordConfirm() != user.getPassword()) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm")
        }
    }
}
