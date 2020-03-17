package com.sohaibaijaz.sawaari.model;

import java.util.List;

public class RidesModel {
    private Integer status;
    private List<Ride> rides = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
