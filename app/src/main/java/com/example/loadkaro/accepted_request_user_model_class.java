package com.example.loadkaro;

public class accepted_request_user_model_class {
    String driver_name,driver_pic_url,driver_id,request_no,address_from,address_to,driver_contact,material_name;

    public accepted_request_user_model_class() {
    }

    public accepted_request_user_model_class(String driver_name, String driver_pic_url, String driver_id, String request_no, String address_from, String address_to, String driver_contact, String material_name) {
        this.driver_name = driver_name;
        this.driver_pic_url = driver_pic_url;
        this.driver_id = driver_id;
        this.request_no = request_no;
        this.address_from = address_from;
        this.address_to = address_to;
        this.driver_contact = driver_contact;
        this.material_name = material_name;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_pic_url() {
        return driver_pic_url;
    }

    public void setDriver_pic_url(String driver_pic_url) {
        this.driver_pic_url = driver_pic_url;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getRequest_no() {
        return request_no;
    }

    public void setRequest_no(String request_no) {
        this.request_no = request_no;
    }

    public String getAddress_from() {
        return address_from;
    }

    public void setAddress_from(String address_from) {
        this.address_from = address_from;
    }

    public String getAddress_to() {
        return address_to;
    }

    public void setAddress_to(String address_to) {
        this.address_to = address_to;
    }

    public String getDriver_contact() {
        return driver_contact;
    }

    public void setDriver_contact(String driver_contact) {
        this.driver_contact = driver_contact;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }
}
