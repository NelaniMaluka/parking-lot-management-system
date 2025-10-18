package org.example.model;

import java.util.List;

public class ParkingState {

    private static ParkingFloor parkingFloor1;
    private static ParkingFloor parkingFloor2;
    private static ParkingFloor parkingFloor3;
    private static ParkingFloor parkingFloor4;
    private static ParkingFloor parkingFloor5;
    private static List<Vehicle> parkedVehicles;
    private static List<Vehicle> completedVehicles;
    private static double totalRevenue;


    // Prevent creating an instance
    private ParkingState() {}

    // Initialize all floors at once
    public static void initialize(
            ParkingFloor f1, ParkingFloor f2, ParkingFloor f3,
            ParkingFloor f4, ParkingFloor f5, List<Vehicle> pv,
            List<Vehicle> cv, double tr
    ) {
        parkingFloor1 = f1;
        parkingFloor2 = f2;
        parkingFloor3 = f3;
        parkingFloor4 = f4;
        parkingFloor5 = f5;
        parkedVehicles = pv;
        completedVehicles = cv;
        totalRevenue = tr;
    }

    // Static Getters
    public static ParkingFloor getParkingFloor1() { return parkingFloor1; }
    public static ParkingFloor getParkingFloor2() { return parkingFloor2; }
    public static ParkingFloor getParkingFloor3() { return parkingFloor3; }
    public static ParkingFloor getParkingFloor4() { return parkingFloor4; }
    public static ParkingFloor getParkingFloor5() { return parkingFloor5; }
    public static List<Vehicle> getParkedVehicles() { return parkedVehicles; }
    public static List<Vehicle> getCompletedVehicles() { return completedVehicles; }
    public static double getTotalRevenue() { return totalRevenue; }

    // Static Setters
    public static void setParkingFloor1(ParkingFloor floor) { parkingFloor1 = floor; }
    public static void setParkingFloor2(ParkingFloor floor) { parkingFloor2 = floor; }
    public static void setParkingFloor3(ParkingFloor floor) { parkingFloor3 = floor; }
    public static void setParkingFloor4(ParkingFloor floor) { parkingFloor4 = floor; }
    public static void setParkingFloor5(ParkingFloor floor) { parkingFloor5 = floor; }
    public static void setParkedVehicles(List<Vehicle> list) { parkedVehicles = list; }
    public static void setCompletedVehicles(List<Vehicle> list) { completedVehicles = list; }
    public static void setTotalRevenue(double revenue) { totalRevenue = revenue; }

    // Static helper to get a floor by number
    public static ParkingFloor getFloor(String fl) {
        return switch (fl) {
            case "1" -> parkingFloor1;
            case "2" -> parkingFloor2;
            case "3" -> parkingFloor3;
            case "4" -> parkingFloor4;
            case "5" -> parkingFloor5;
            default -> null;
        };
    }

    // Static helper to get a floor by number
    public static void setFloor(String fl, ParkingFloor floor) {
        switch (fl) {
            case "1" : parkingFloor1 = floor;
            case "2" : parkingFloor2 = floor;
            case "3" : parkingFloor3 = floor;
            case "4" : parkingFloor4 = floor;
            case "5" : parkingFloor5 = floor;
        };

        System.out.println("Saved data for this floor: " + fl);
    }
}
