package fleet;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import interfaces.FuelConsumable;
import interfaces.Maintainable;
import vehicles.*;

public class FleetManager {

    private List<Vehicle> fleet;

    private static class VehicleSpeedComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return Double.compare(v1.getMaxSpeed(), v2.getMaxSpeed());
        }
    }

    private static class VehicleModelComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return v1.getModel().compareTo(v2.getModel());
        }
    }

    public FleetManager() {
        this.fleet = new ArrayList<>();
    }

    public void addVehicle(Vehicle v) throws InvalidOperationException {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getId().equalsIgnoreCase(v.getId())) {
                throw new InvalidOperationException("Vehicle with ID " + v.getId() + " already exists in the fleet.");
            }
        }
        fleet.add(v);
        System.out.println(v.getClass().getSimpleName() + " with ID " + v.getId() + " added to the fleet.");
    }

    public void removeVehicle(String id) throws InvalidOperationException {
        boolean removed = fleet.removeIf(v -> v.getId().equalsIgnoreCase(id));
        if (!removed) {
            throw new InvalidOperationException("Vehicle with ID " + id + " not found in the fleet.");
        }
        System.out.println("Vehicle with ID " + id + " removed from the fleet.");
    }

    public void startAllJourneys(double distance) {
        System.out.println("\n--- Starting all journeys for " + distance + " km ---");
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
            } catch (InvalidOperationException | InsufficientFuelException e) {
                System.err.println("Could not move vehicle " + v.getId() + ": " + e.getMessage());
            }
        }
        System.out.println("--- All journeys complete ---");
    }

    public double getTotalFuelConsumption(double distance) {
        double totalFuel = 0;
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                totalFuel += distance / v.calculateFuelEfficiency();
            }
        }
        return totalFuel;
    }

    public void maintainAll() {
        System.out.println("\n--- Performing maintenance check on all vehicles ---");
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) {
                    m.performMaintenance();
                    System.out.println("Maintenance performed on vehicle " + v.getId() + ".");
                }
            }
        }
        System.out.println("--- Maintenance check complete ---");
    }

    public List<Vehicle> searchByType(String type) {
        return fleet.stream()
                .filter(v -> v.getClass().getSimpleName().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public void sortFleetByEfficiency() {
        Collections.sort(fleet);
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n========== Fleet Status Report ==========\n");
        report.append("Total Vehicles in Fleet: ").append(fleet.size()).append("\n");

        if (fleet.isEmpty()) {
            report.append("Fleet is currently empty.\n");
        } else {

            Set<String> distinctModels = fleet.stream()
                    .map(Vehicle::getModel)
                    .collect(Collectors.toCollection(TreeSet::new)); // TreeSet automatically sorts
            
            report.append("Total Distinct Models: ").append(distinctModels.size()).append("\n");
            report.append("Models List: ").append(distinctModels).append("\n\n");

            try {
                Vehicle fastestVehicle = Collections.max(fleet, new VehicleSpeedComparator());
                Vehicle slowestVehicle = Collections.min(fleet, new VehicleSpeedComparator());
    
                report.append("--- Performance Stats ---\n");
                report.append(String.format("Fastest Vehicle: %s (ID: %s) at %.2f km/h\n", 
                    fastestVehicle.getModel(), fastestVehicle.getId(), fastestVehicle.getMaxSpeed()));
                report.append(String.format("Slowest Vehicle: %s (ID: %s) at %.2f km/h\n", 
                    slowestVehicle.getModel(), slowestVehicle.getId(), slowestVehicle.getMaxSpeed()));
            } catch (Exception e) {
                 report.append("--- Performance Stats ---\n");
                 report.append("Could not determine fastest/slowest vehicle: " + e.getMessage() + "\n");
            }
            
            report.append("\n--- Current Fleet Details ---\n");
            for (Vehicle v : fleet) {
                report.append(String.format("ID: %-8s | Type: %-12s | Model: %-15s | Mileage: %-10.2f km\n",
                        v.getId(), v.getClass().getSimpleName(), v.getModel(), v.getCurrentMileage()));
            }
        }
        report.append("=========================================\n");
        return report.toString();
    }
    
    public List<Vehicle> getVehiclesNeedingMaintenance() {
        return fleet.stream()
                .filter(v -> v instanceof Maintainable && ((Maintainable) v).needsMaintenance())
                .collect(Collectors.toList());
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Vehicle v : fleet) {
                writer.write(v.toCsvString());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            fleet.clear(); // Clear current fleet before loading
            while ((line = reader.readLine()) != null) {
                try {
                    Vehicle vehicle = VehicleFactory.createVehicleFromCsv(line);
                    addVehicle(vehicle);
                } catch (Exception e) {
                    System.err.println("Could not load vehicle from line: '" + line + "'. Error: " + e.getMessage());
                }
            }
        }
    }
     public List<Vehicle> getFleet() {
        return new ArrayList<>(fleet); 
    }
    public void sortFleetByMaxSpeed() {
        Collections.sort(fleet, new VehicleSpeedComparator());
        System.out.println("Fleet sorted by max speed (slowest to fastest).");
    }

    public void sortFleetByModelName() {
        Collections.sort(fleet, new VehicleModelComparator());
        System.out.println("Fleet sorted by model name (alphabetical).");
    }
}

class VehicleFactory {
    public static Vehicle createVehicleFromCsv(String csvLine) {
        String[] data = csvLine.split(",");
        String type = data[0];
        String id = data[1];
        String model = data[2];
        double maxSpeed = Double.parseDouble(data[3]);

        switch (type.toLowerCase()) {
            case "car":
                return new Car(id, model, maxSpeed);
            case "truck":
                return new Truck(id, model, maxSpeed);
            case "bus":
                return new Bus(id, model, maxSpeed);
            case "airplane":
                double maxAltitude = Double.parseDouble(data[3]);
                return new Airplane(id, model, maxSpeed, maxAltitude);
            case "cargoship":
                boolean hasSail = Boolean.parseBoolean(data[4]);
                return new CargoShip(id, model, maxSpeed, hasSail);
            default:
                throw new IllegalArgumentException("Unknown vehicle type in CSV: " + type);
        }
    }
}
