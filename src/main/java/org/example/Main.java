package org.example;

import org.example.model.ParkingFloor;
import org.example.state.ParkingState;
import org.example.model.VEHICLE_TYPE;
import org.example.model.Vehicle;
import org.example.service.ParkVehicleService;
import org.example.service.UnparkVehicleService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Create your floor objects
        ParkingFloor f1 = new ParkingFloor();
        ParkingFloor f2 = new ParkingFloor();
        ParkingFloor f3 = new ParkingFloor();
        ParkingFloor f4 = new ParkingFloor();
        ParkingFloor f5 = new ParkingFloor();
        List<Vehicle> pv = new ArrayList<>();
        List<Vehicle> cv = new ArrayList<>();
        double r = 0;

        // Initialize the global state
        ParkingState.initialize(f1, f2, f3, f4, f5, pv, cv, r);

        ParkVehicleService parkVehicleService = new ParkVehicleService();
        parkVehicleService.parkVehicle("1", VEHICLE_TYPE.CAR, "NBR 123 GP");
        parkVehicleService.parkVehicle("2", VEHICLE_TYPE.CAR, "NBR 123 GP");

        UnparkVehicleService unparkVehicleService = new UnparkVehicleService();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule task to run 5 minutes later
        scheduler.schedule(() -> {
            System.out.println(" ");
            unparkVehicleService.unparkVehicle("NBR 123 GP");
            System.out.println(ParkingState.getTotalRevenue());
        }, 5, TimeUnit.MINUTES);
    }
}