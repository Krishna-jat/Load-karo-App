package com.example.loadkaro;

public class all_trip_request_model_class {
    String user_token_id,request_no,address_from,address_to,date_of_loading,date_of_unloading,distance,material_name,material_quantity,price,user_contact,user_name,user_url_pic,user_id;

    //status needed for showing status of request in driver accepted request activity
    String request_status;

    public all_trip_request_model_class() {
    }

    public all_trip_request_model_class(String user_token_id,String request_no,String address_from, String address_to, String date_of_loading, String date_of_unloading, String distance, String material_name, String material_quantity, String price, String user_contact, String user_name, String user_url_pic, String user_id) {

        this.user_token_id=user_token_id;
        this.request_no=request_no;
        this.address_from = address_from;
        this.address_to = address_to;
        this.date_of_loading = date_of_loading;
        this.date_of_unloading = date_of_unloading;
        this.distance = distance;
        this.material_name = material_name;
        this.material_quantity = material_quantity;
        this.price = price;
        this.user_contact = user_contact;
        this.user_name = user_name;
        this.user_url_pic = user_url_pic;
        this.user_id = user_id;
    }

    public all_trip_request_model_class(String user_token_id,String request_no, String address_from, String address_to, String date_of_loading, String date_of_unloading, String distance, String material_name, String material_quantity, String price, String user_contact, String user_name, String user_url_pic, String user_id, String request_status) {

        this.user_token_id=user_token_id;
        this.request_no = request_no;
        this.address_from = address_from;
        this.address_to = address_to;
        this.date_of_loading = date_of_loading;
        this.date_of_unloading = date_of_unloading;
        this.distance = distance;
        this.material_name = material_name;
        this.material_quantity = material_quantity;
        this.price = price;
        this.user_contact = user_contact;
        this.user_name = user_name;
        this.user_url_pic = user_url_pic;
        this.user_id = user_id;
        this.request_status = request_status;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public void setUser_token_id(String user_token_id) {
        this.user_token_id = user_token_id;
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

    public String getDate_of_loading() {
        return date_of_loading;
    }

    public void setDate_of_loading(String date_of_loading) {
        this.date_of_loading = date_of_loading;
    }

    public String getDate_of_unloading() {
        return date_of_unloading;
    }

    public void setDate_of_unloading(String date_of_unloading) {
        this.date_of_unloading = date_of_unloading;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMaterial_quantity() {
        return material_quantity;
    }

    public void setMaterial_quantity(String material_quantity) {
        this.material_quantity = material_quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_url_pic() {
        return user_url_pic;
    }

    public void setUser_url_pic(String user_url_pic) {
        this.user_url_pic = user_url_pic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }
}
