package com.shravan.api.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserRequestModel {
    @NotNull(message = "firstName cannot be null")
    @Size(min = 2, message = "firstName cannot be less than 2 characters")
    private String firstName;
    @NotNull(message = "lastName cannot be null")
    @Size(min = 2, message = "lastName cannot be less than 2 characters")
    private String lastName;
    @NotNull(message = "email cannot be null")
    @Email
    private String email;
    @NotNull(message = "password cannot be null")
    @Size(min = 2, max = 24, message = "password cannot be less than 2 characters and greater than 16 characters")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequestModel userRequestModel = (UserRequestModel) o;
        return firstName.equals(userRequestModel.firstName) &&
                lastName.equals(userRequestModel.lastName) &&
                email.equals(userRequestModel.email) &&
                password.equals(userRequestModel.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }
}
