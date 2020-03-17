package com.sohaibaijaz.sawaari.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PickUpLocation implements Serializable {

    @SerializedName("stop_id")
    private Integer stopId;
    @SerializedName("stop_name")
    private String stopName;
    @SerializedName("duration")
    private String duration;
    @SerializedName("distance")
    private String distance;
    @SerializedName("arrival_time")
    private String arrivalTime;

    public Integer getStopId() {
        return stopId;
    }

    public void setStopId(Integer stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
