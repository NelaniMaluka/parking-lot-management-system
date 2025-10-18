package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    int id;
    List<ParkingSpot> parkingSpots = new ArrayList<>(80);
    private int availableSpots = 80;

    public ParkingFloor() {
    }

    public ParkingFloor(int id, List<ParkingSpot> parkingSpots, int availableSpots) {
        this.id = id;
        this.parkingSpots = parkingSpots;
        this.availableSpots = availableSpots;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }
}
