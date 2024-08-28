package com.example.loadkaro;

public class my_request_user_model_class {
    String request_no,address_from,address_to,date_of_loading,date_of_unloading,distance,material_name,material_quantity,price;

    public my_request_user_model_class() {
    }

    public my_request_user_model_class(String request_no,String address_from, String address_to, String date_of_loading, String date_of_unloading, String distance, String material_name, String material_quantity, String price) {
        this.request_no = request_no;
        this.address_from = address_from;
        this.address_to = address_to;
        this.date_of_loading = date_of_loading;
        this.date_of_unloading = date_of_unloading;
        this.distance = distance;
        this.material_name = material_name;
        this.material_quantity = material_quantity;
        this.price = price;
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
}
