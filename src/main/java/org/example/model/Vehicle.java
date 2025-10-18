package org.example.model;

public class Vehicle {
    private long id;
    private VEHICLE_TYPE vehicleType;
    private Ticket ticket;
    private String numberPlate;

    public Vehicle() {
    }

    public Vehicle(long id, VEHICLE_TYPE vehicleType, Ticket ticket, String numberPlate) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.ticket = ticket;
        this.numberPlate = numberPlate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VEHICLE_TYPE getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VEHICLE_TYPE vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
}
