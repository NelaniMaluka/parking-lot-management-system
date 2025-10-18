package org.example.model;

public class ParkingSpot {
    private long id;
    private Vehicle vehicle;
    private PARKING_SPOT_TYPE parkingSpotType;
    private boolean isOccupied;

    public ParkingSpot() {
    }

    public ParkingSpot(long id, Vehicle vehicle, PARKING_SPOT_TYPE parkingSpotType, boolean isOccupied) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpotType = parkingSpotType;
        this.isOccupied = isOccupied;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public PARKING_SPOT_TYPE getParkingSpotType() {
        return parkingSpotType;
    }

    public void setParkingSpotType(PARKING_SPOT_TYPE parkingSpotType) {
        this.parkingSpotType = parkingSpotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
