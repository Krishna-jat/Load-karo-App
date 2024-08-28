package com.example.loadkaro;

public class driver_vehicle_model_class {
    String vehicle_type,vehicle_number,vehicle_capacity;

    public driver_vehicle_model_class() {
    }

    public driver_vehicle_model_class(String vehicle_type, String vehicle_number, String vehicle_capacity) {
        this.vehicle_type = vehicle_type;
        this.vehicle_number = vehicle_number;
        this.vehicle_capacity = vehicle_capacity;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getVehicle_capacity() {
        return vehicle_capacity;
    }

    public void setVehicle_capacity(String vehicle_capacity) {
        this.vehicle_capacity = vehicle_capacity;
    }
}
