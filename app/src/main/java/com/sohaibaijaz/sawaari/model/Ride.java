package com.sohaibaijaz.sawaari.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ride implements Serializable {

    @SerializedName("vehicle_no_plate")
    private String vehicleNoPlate;
    @SerializedName("seats_left")
    private Integer seatsLeft;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("ride_date")
    private String rideDate;
    @SerializedName("pick-up-location")
    private PickUpLocation pickUpLocation;
    @SerializedName("drop-off-location")
    private DropOffLocation dropOffLocation;

    public String getVehicleNoPlate() {
        return vehicleNoPlate;
    }

    public void setVehicleNoPlate(String vehicleNoPlate) {
        this.vehicleNoPlate = vehicleNoPlate;
    }

    public Integer getSeatsLeft() {
        return seatsLeft;
    }

    public void setSeatsLeft(Integer seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public PickUpLocation getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(PickUpLocation pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public DropOffLocation getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(DropOffLocation dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }
}
