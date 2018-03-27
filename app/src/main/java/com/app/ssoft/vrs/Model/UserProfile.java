package com.app.ssoft.vrs.Model;

/**
 * Created by Shekahar.Shrivastava on 27-Mar-18.
 */

public class UserProfile {
    public String userId;
    public String emailId;
    public String password;
    public String profilePicture;
    public String firstName;
    public String lastName;
    public String gender;
    public String dob;
    public String country;
    public String state;
    public String city;

    public UserProfile (){

    }

    public UserProfile(String userId, String emailId,String password, String profilePicture, String firstName, String lastName, String gender, String dob, String country, String state, String city) {
        this.userId = userId;
        this.emailId = emailId;
        this.password = password;
        this.profilePicture = profilePicture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
