package com.struts.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDTO {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String dob;
    private String gender;

    public UserDTO() {
    }

    public UserDTO (User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.email = user.getEmail();
            setGender(user.getGender());
            setDob(user.getDob());
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.dob = dob.format(formatter);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender  = (gender == 'M') ? "Male" : (gender == 'F') ? "Female" : "Other";
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", dob=" + dob + ", gender=" + gender + "]";
    }
}
