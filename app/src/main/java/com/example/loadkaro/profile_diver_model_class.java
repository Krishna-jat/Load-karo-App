package com.example.loadkaro;

public class profile_diver_model_class {

String name,contact,experience,email,profile_dp_url;

    public profile_diver_model_class()
    {}

    public profile_diver_model_class(String name, String contact, String experience, String email, String profile_pic_url) {
        this.name = name;
        this.contact = contact;
        this.experience = experience;
        this.email = email;
        this.profile_dp_url = profile_pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_dp_url() {
        return profile_dp_url;
    }

    public void setProfile_dp_url(String profile_pic_url) {
        this.profile_dp_url = profile_pic_url;
    }
}
