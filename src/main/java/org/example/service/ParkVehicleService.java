package org.example.service;

import org.example.model.*;

import java.util.List;

public class ParkVehicleService {

    public void parkVehicle (String fl, VEHICLE_TYPE vehicleType, String numberPlate) {
        ParkingFloor floor = ParkingState.getFloor(fl);

        // fill parking spots with parkingSpot objects
        assert floor != null;
        List<ParkingSpot> parkingSpots = (!floor.getParkingSpots().isEmpty())
                ? floor.getParkingSpots()
                : generateSpots(fl, floor);

        // Check if the vehicles isn't already parked with the same number plate
        List<Vehicle> parkedVehicles = ParkingState.getParkedVehicles();
        boolean alreadyParked = parkedVehicles.stream()
                .anyMatch(v -> v.getNumberPlate().equals(numberPlate));
        if (alreadyParked) {
            System.out.println("A vehicle is already parked with that number plate: " + numberPlate);
            return;
        }

        // Create a new ticket for the vehicle
        Ticket ticket = new Ticket();

        // Create a new vehicle object
        Vehicle vehicle = new Vehicle();
        vehicle.setNumberPlate(numberPlate);
        vehicle.setVehicleType(vehicleType);
        vehicle.setTicket(ticket);

        // Find the first available parking spot for the vehicle type
        ParkingSpot spot = findAvailableSpot(parkingSpots, vehicleType);
        if (spot == null) {
            System.out.println("No available parking pots found. try looking at a different floor.");
            return;
        }

        spot.setOccupied(true);
        spot.setVehicle(vehicle);
        parkedVehicles.add(vehicle);

        // Save the updates
        floor.setParkingSpots(parkingSpots);
        ParkingState.setFloor(fl, floor);
        ParkingState.setParkedVehicles(parkedVehicles);

        System.out.println("Succesfully saved data for parked " + vehicleType);
    }

    /**
     * Generates parking spots for a given floor based on predefined configuration.
     * <p>
     * Floor 1 includes 10 reserved TRUCK_SPOTs for deliveries.
     * Other floors exclude truck spots.
     *
     * @param fl    Floor identifier (e.g., "1", "2", "3")
     * @param floor The ParkingFloor object to populate with spots
     * @return List of generated ParkingSpot objects
     */
    private List<ParkingSpot> generateSpots(String fl, ParkingFloor floor) {
        List<ParkingSpot> parkingSpots = floor.getParkingSpots();
        boolean isFirstFloor = "1".equals(fl);

        int totalSpots = floor.getAvailableSpots();

        for (int i = 0; i < totalSpots; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.setId(i + 1);

            if (i <= 5) {
                spot.setParkingSpotType(PARKING_SPOT_TYPE.CAR_SPOT);
            } else if (i <= 15) {
                spot.setParkingSpotType(PARKING_SPOT_TYPE.BIKE_SPOT);
            } else if (i <= 45) {
                spot.setParkingSpotType(PARKING_SPOT_TYPE.CAR_SPOT);
            } else if (i <= 50) {
                spot.setParkingSpotType(PARKING_SPOT_TYPE.HANDICAP_SPOT);
            } else if (i < 70) {
                spot.setParkingSpotType(PARKING_SPOT_TYPE.CAR_SPOT);
            } else if (isFirstFloor && i < 80) {  // only first floor
                spot.setParkingSpotType(PARKING_SPOT_TYPE.TRUCK_SPOT);
            } else {
                spot.setParkingSpotType(PARKING_SPOT_TYPE.CAR_SPOT);
            }

            parkingSpots.add(spot);
        }

        return parkingSpots;
    }

    /**
     * ===================== PARKING RULES =====================
     * 1️⃣  Bikes → Prefer BIKE_SPOT → then CAR_SPOT → lastly HANDICAP_SPOT (if allowed)
     * 2️⃣  Cars → Prefer CAR_SPOT → then HANDICAP_SPOT (if allowed)
     * 3️⃣  Handicap Cars → Only HANDICAP_SPOT
     * 4️⃣  Trucks → 🚫 STRICT RULE: Only TRUCK_SPOT (reserved for deliveries)
     * =========================================================
     *
     * Finds the first available parking spot for the given vehicle type,
     * following the priority and restrictions defined above.
     */
    private ParkingSpot findAvailableSpot(List<ParkingSpot> parkingSpots, VEHICLE_TYPE vehicleType) {
        for (ParkingSpot parkingSpot : parkingSpots) {

            /* First round of checks if there are available spots for the specific vehicle type */
            if (!parkingSpot.isOccupied() && vehicleType.equals(VEHICLE_TYPE.BIKE)
                    && parkingSpot.getParkingSpotType().equals(PARKING_SPOT_TYPE.BIKE_SPOT)) {
                // Bike can park on bike spot
                return parkingSpot;
            } else if (!parkingSpot.isOccupied() && vehicleType.equals(VEHICLE_TYPE.CAR)
                    && parkingSpot.getParkingSpotType().equals(PARKING_SPOT_TYPE.CAR_SPOT)) {
                // Car can park on car spot
                return parkingSpot;
            } else if (!parkingSpot.isOccupied() && vehicleType.equals(VEHICLE_TYPE.HANDICAP_CAR)
                    && parkingSpot.getParkingSpotType().equals(PARKING_SPOT_TYPE.HANDICAP_SPOT)) {
                // Handicap car can park on handicap spot
                return parkingSpot;
            } else if (!parkingSpot.isOccupied() && vehicleType.equals(VEHICLE_TYPE.TRUCK)
                    && parkingSpot.getParkingSpotType().equals(PARKING_SPOT_TYPE.TRUCK_SPOT)) {
                // Truck can ONLY park on a truck spot (reserved for deliveries)
                return parkingSpot;
            }

            /* Second round of checks if there are available corresponding spots */
            if (!parkingSpot.isOccupied() && vehicleType.equals(VEHICLE_TYPE.BIKE)
                    && parkingSpot.getParkingSpotType().equals(PARKING_SPOT_TYPE.CAR_SPOT)) {
                // Bike can park on car spot
                return parkingSpot;
            }

            /* Third round of checks if there are no available spots, allow handicap spots */
            if (!parkingSpot.isOccupied() &&
                    (vehicleType.equals(VEHICLE_TYPE.BIKE) || vehicleType.equals(VEHICLE_TYPE.CAR)) &&
                    parkingSpot.getParkingSpotType().equals(PARKING_SPOT_TYPE.HANDICAP_SPOT)) {
                // Bikes and cars can park on handicap spot (last resort)
                return parkingSpot;
            }
        }
        return null;
    }

}
