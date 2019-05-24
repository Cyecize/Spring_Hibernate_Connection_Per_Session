package com.cyecize.multidb.areas.demo.bindingModels;

import com.cyecize.multidb.areas.demo.validators.UniqueUsername;
import com.cyecize.multidb.utils.FieldMatch;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@FieldMatch(first = "password", second = "passwordConfirm", message = "passwordsDoNotMatch")
public class CreateUserBindingModel {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[^\\s]{3,}$", message = "Invalid username format")
    @UniqueUsername(message = "Username Taken")
    private String username;

    @NotNull
    @NotEmpty
    @UniqueUsername(message = "Email Taken")
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = 6, max = 255)
    private String password;

    private String passwordConfirm;

    public CreateUserBindingModel() {

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return this.passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
