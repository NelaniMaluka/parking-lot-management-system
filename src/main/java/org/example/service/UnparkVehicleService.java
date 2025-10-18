package org.example.service;

import org.example.model.*;
import org.example.state.ParkingState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnparkVehicleService {

    public void unparkVehicle(String numberPlate) {
        List<Vehicle> parkedVehicles = ParkingState.getParkedVehicles();

        // Find the vehicle
        Vehicle vehicle = parkedVehicles.stream()
                .filter(v -> v.getNumberPlate().equals(numberPlate))
                .findFirst()
                .orElse(null);

        if (vehicle == null) {
            System.out.println("No parked vehicle found with that number plate: " + numberPlate);
            return;
        }

        // Calculate ticket fees
        vehicle.setTicket(calculateTicketFees(vehicle.getTicket()));

        // Remove from parked list
        parkedVehicles.remove(vehicle);

        // Add to completed vehicles
        List<Vehicle> completedVehicles = ParkingState.getCompletedVehicles();
        completedVehicles.add(vehicle);

        // Update state
        ParkingState.setParkedVehicles(parkedVehicles);
        ParkingState.setCompletedVehicles(completedVehicles);

        // Remove from the floor
        removeVehicleFromFloor(vehicle);
    }

    private void removeVehicleFromFloor(Vehicle vehicle) {
        boolean removed = false;

        for (int i = 1; i <= 5; i++) {
            ParkingFloor floor = ParkingState.getFloor(String.valueOf(i));
            if (floor == null) continue;

            for (ParkingSpot spot : floor.getParkingSpots()) {
                Vehicle parked = spot.getVehicle();
                if (parked != null && parked.getNumberPlate().equals(vehicle.getNumberPlate())) {
                    spot.setVehicle(null);
                    spot.setOccupied(false);
                    removed = true;
                    System.out.println("Successfully un-parked the vehicle from floor " + i);
                    break; // vehicle found, no need to check other spots on this floor
                }
            }

            if (removed) break; // vehicle found, stop iterating floors
        }

        if (!removed) {
            System.out.println("Failed to remove vehicle from the floors.");
        }
    }

    /**
     * Calculates the total parking fee for a given ticket based on the time spent
     * in different periods (peak, off-peak, and weekend).
     *
     * <p><strong>Fee Calculation Rules:</strong></p>
     * <ul>
     *   <li><b>Peak Hours:</b> Weekdays from 06:00–09:00 and 16:00–19:00, billed at R17.50/hour.</li>
     *   <li><b>Off-Peak Hours:</b> All other weekday hours, billed at R15.00/hour.</li>
     *   <li><b>Weekend Hours:</b> All hours on Saturday and Sunday, billed at R20.00/hour.</li>
     * </ul>
     *
     * <p>This method determines the total parked time since entry, divides it into
     * the three categories using {@link #calculateTimeCategories(LocalDateTime, LocalDateTime)},
     * and computes the total cost accordingly.</p>
     *
     * @param ticket the {@link Ticket} containing at least the vehicle's entry time
     * @return the updated {@link Ticket} instance with the calculated price set
     */
    private Ticket calculateTicketFees(Ticket ticket) {
        // Entry and exit timestamps
        LocalDateTime parkTime = ticket.getTimeIn();
        LocalDateTime exitTime = LocalDateTime.now();

        // Total time parked
        Duration totalDuration = Duration.between(parkTime, exitTime);

        // Retrieve categorized time spent (peak & weekend)
        Map<String, Long> results = calculateTimeCategories(parkTime, exitTime);
        Duration peakDuration = Duration.ofMillis(results.get("peak"));
        Duration weekendDuration = Duration.ofMillis(results.get("weekend"));
        Duration offPeakDuration = totalDuration.minus(peakDuration).minus(weekendDuration);

        // Hourly fee rates
        final double WEEKEND_FEE = 20.00;
        final double PEAK_FEE = 17.50;
        final double OFF_PEAK_FEE = 15.00;

        // Fees per category
        double peakCost = (peakDuration.toMillis() / 3_600_000.0) * PEAK_FEE;
        double offPeakCost = (offPeakDuration.toMillis() / 3_600_000.0) * OFF_PEAK_FEE;
        double weekendCost = (weekendDuration.toMillis() / 3_600_000.0) * WEEKEND_FEE;

        // Set durations and costs on the ticket
        ticket.setPeakDuration(peakDuration);
        ticket.setOffPeakDuration(offPeakDuration);
        ticket.setWeekendDuration(weekendDuration);

        ticket.setPeakCost(peakCost);
        ticket.setOffPeakCost(offPeakCost);
        ticket.setWeekendCost(weekendCost);


        // Round to 2 decimal places
         double total = BigDecimal.valueOf(peakCost + offPeakCost + weekendCost)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        // Set total price
        ticket.setPrice(total);
        ParkingState.setTotalRevenue(total);

        return ticket;
    }

    /**
     * Calculates how much time between two timestamps falls into "peak" hours
     * and "weekend" hours.
     *
     * <p>Peak hours are defined as 06:00–09:00 and 16:00–19:00 on weekdays.
     * All time on Saturday and Sunday counts as weekend time. Remaining time
     * can be considered off-peak.</p>
     *
     * <p>Returned map keys:</p>
     * <ul>
     *   <li><b>"peak"</b> – total milliseconds spent during weekday peak hours</li>
     *   <li><b>"weekend"</b> – total milliseconds spent during weekends</li>
     * </ul>
     *
     * @param start the start time (inclusive)
     * @param end   the end time (exclusive)
     * @return a map containing "peak" and "weekend" time durations in milliseconds
     */
    public Map<String, Long> calculateTimeCategories(LocalDateTime start, LocalDateTime end) {
        // Define weekday peak periods (morning & evening)
        List<LocalTime[]> peakPeriods = List.of(
                new LocalTime[]{LocalTime.of(6, 0), LocalTime.of(9, 0)},
                new LocalTime[]{LocalTime.of(16, 0), LocalTime.of(19, 0)}
        );

        long peakMillis = 0;
        long weekendMillis = 0;
        LocalDate currentDate = start.toLocalDate();

        // Iterate day by day between start and end
        while (!currentDate.isAfter(end.toLocalDate())) {
            DayOfWeek day = currentDate.getDayOfWeek();

            // Determine time boundaries for the current day
            LocalDateTime dayStart = currentDate.equals(start.toLocalDate())
                    ? start
                    : currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.equals(end.toLocalDate())
                    ? end
                    : currentDate.plusDays(1).atStartOfDay();

            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                // Weekend: all time counts as weekend
                weekendMillis += Duration.between(dayStart, dayEnd).toMillis();
            } else {
                // Weekday: calculate peak overlaps
                for (LocalTime[] peak : peakPeriods) {
                    LocalDateTime peakStart = LocalDateTime.of(currentDate, peak[0]);
                    LocalDateTime peakEnd = LocalDateTime.of(currentDate, peak[1]);

                    LocalDateTime overlapStart = dayStart.isAfter(peakStart) ? dayStart : peakStart;
                    LocalDateTime overlapEnd = dayEnd.isBefore(peakEnd) ? dayEnd : peakEnd;

                    if (overlapStart.isBefore(overlapEnd)) {
                        peakMillis += Duration.between(overlapStart, overlapEnd).toMillis();
                    }
                }
            }

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        // Return categorized time in milliseconds
        Map<String, Long> result = new HashMap<>();
        result.put("peak", peakMillis);
        result.put("weekend", weekendMillis);
        return result;
    }

}
