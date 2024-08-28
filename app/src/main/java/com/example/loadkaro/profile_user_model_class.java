package com.example.loadkaro;

public class profile_user_model_class {
    String name,contact,contact2,email,address,profile_dp_url;

    public profile_user_model_class() {
    }

    public profile_user_model_class(String name, String contact,String contact2, String email, String address, String profile_dp_url) {
        this.name = name;
        this.contact = contact;
        this.contact2=contact2;
        this.email = email;
        this.address = address;
        this.profile_dp_url = profile_dp_url;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfile_dp_url() {
        return profile_dp_url;
    }

    public void setProfile_dp_url(String profile_dp_url) {
        this.profile_dp_url = profile_dp_url;
    }
}
