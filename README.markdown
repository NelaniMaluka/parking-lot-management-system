# 🚗 Parking Management System

A Java-based **Parking Management System** that simulates a multi-floor parking lot with real-time vehicle tracking, dynamic fee calculation, and separation of service and model layers.

## 🧱 Project Architecture

```
src/
├── model/
│   ├── ParkingFloor.java
│   ├── ParkingSpot.java
│   ├── Vehicle.java
│   ├── Ticket.java
│   └── SpotType.java # Enum for CAR, BIKE, TRUCK
│
├── service/
│   ├── ParkVehicleService.java
│   ├── UnparkVehicleService.java
│   └── FeeCalculationService.java
│
└── state/
    └── ParkingState.java # Central state manager
```

## 🧩 System Overview

The system manages **5 parking floors**, each containing multiple parking spots.  

Each `ParkingSpot` can hold a single `Vehicle`, which is associated with a `Ticket` that records:
- Entry time (`timeIn`)
- Exit time (`timeOut`)
- Parking fees (based on duration and type)

### 🏢 Floors
- There are **5 floors** in total.
- **Floor 1** includes specialized zones for:
  - 🚙 **Car spots**
  - 🏍️ **Bike spots**
  - 🚛 **Truck spots**
- Floors 2–5 can be configured for general car parking.

## ⚙️ Core Components

### 1️⃣ `ParkingState`
Acts as a **central data store** for the current parking lot.
- Stores lists of:
  - `parkedVehicles`
  - `completedVehicles` (vehicles that have left and paid)
- Maintains all `ParkingFloor` instances and provides getters/setters for each.

### 2️⃣ `ParkingFloor`
Represents a floor in the parking building.
- Holds multiple `ParkingSpot` objects.
- Each spot has an ID, type (car/bike/truck), and occupancy status.

### 3️⃣ `ParkingSpot`
Represents an individual spot.
- Linked to a single `Vehicle`
- Can be occupied or free
- Different spot types:
  - `CAR_SPOT`
  - `BIKE_SPOT`
  - `TRUCK_SPOT`

### 4️⃣ `Vehicle`
Represents a parked vehicle.
- Attributes: `numberPlate`, `vehicleType`, and a linked `Ticket`
- When a vehicle is parked, a ticket is created with `timeIn`.
- When unparked, the `timeOut` is recorded, and fees are calculated.

### 5️⃣ `Ticket`
Contains all billing and timing information for a parked vehicle.
- `timeIn` and `timeOut` are recorded using `LocalDateTime`
- Duration is broken down into:
  - Peak hours
  - Off-peak hours
  - Weekend hours
- Each category has a specific hourly rate.

## 💰 Fee Calculation Logic

The `FeeCalculationService` (or similar logic in `UnparkVehicleService`) computes parking costs based on **time spent** and **day type**:

| Category     | Time Range               | Rate (R/hour) |
|--------------|--------------------------|---------------|
| 🕕 Peak       | 06:00–09:00, 16:00–19:00 | 17.50         |
| 🌙 Off-Peak   | All other weekday times  | 15.00         |
| 📅 Weekend    | Saturday & Sunday (all day) | 20.00      |

Each fee is rounded to **two decimal places** using `BigDecimal` for financial precision.

## 🧠 Example Flow

1. A vehicle enters → `ParkVehicleService` assigns a free spot and creates a `Ticket`.
2. When the vehicle leaves → `UnparkVehicleService`:
   - Calculates total time parked
   - Categorizes time into peak/off-peak/weekend
   - Computes total fee
   - Moves the vehicle from `parkedVehicles` → `completedVehicles`
   - Frees the parking spot

## 🧾 Sample Usage

```java
public class Main {
    public static void main(String[] args) {
        UnparkVehicleService unparkService = new UnparkVehicleService();

        // Unpark after simulation delay
        unparkService.unparkVehicle("NBR 617 GP");

        // View total revenue
        System.out.println("Total Revenue: R" + ParkingState.getTotalRevenue());
    }
}
```

### 🧮 Example Fee Breakdown
| Duration Type | Hours | Rate  | Cost (R) |
|---------------|-------|-------|----------|
| Peak          | 2.0   | 17.50 | 35.00    |
| Off-Peak      | 3.0   | 15.00 | 45.00    |
| Weekend       | 0.0   | 20.00 | 0.00     |
| **Total**     | —     | —     | **R80.00** |

## 🧰 Technologies Used
- Java 17+
- Collections API
- java.time (LocalDateTime, Duration)
- BigDecimal for rounding
- Streams & Lambdas for filtering and mapping

## 🧼 Future Improvements
- Database persistence (JDBC or JPA)
- REST API integration (Spring Boot)
- Vehicle type-based pricing
- Real-time dashboard for spot availability

## 📊 UML Diagram
Below is a simplified UML class diagram showing the relationships between core components:

```plaintext
+----------------+       +----------------+
|  ParkingState  |<>---->|  ParkingFloor  |
+----------------+ 1    * +----------------+
| -parkedVehicles|       | -spots         |
| -completedVehicles|    |                |
| -floors        |       +----------------+
+----------------+              |
                               | 1
                               | *
                       +----------------+
                       |  ParkingSpot   |
                       +----------------+
                       | -id            |
                       | -type          |
                       | -isOccupied    |
                       | -vehicle       |
                       +----------------+
                               |
                               | 0..1
                               |
                       +----------------+
                       |    Vehicle     |
                       +----------------+
                       | -numberPlate   |
                       | -vehicleType   |
                       | -ticket        |
                       +----------------+
                               |
                               | 1
                               |
                       +----------------+
                       |     Ticket     |
                       +----------------+
                       | -timeIn        |
                       | -timeOut       |
                       | -fee           |
                       +----------------+
```

## 👨‍💻 Author
**Nelani Maluka**  
Final-Year Cyber Security & Software Development Student  
Passionate about clean architecture and practical systems design.