package app;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import exceptions.InvalidOperationException;
import fleet.FleetManager;
import interfaces.FuelConsumable;
import vehicles.*;

public class Main {

    private static FleetManager fleetManager = new FleetManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Fleet Management System!");
        runDemo(); 

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleAddVehicle();
                    break;
                case 2:
                    handleRemoveVehicle();
                    break;
                case 3:
                    handleStartJourney();
                    break;
                case 4:
                    handleRefuelAll();
                    break;
                case 5:
                    handlePerformMaintenance();
                    break;
                case 6:
                    handleGenerateReport();
                    break;
                case 7:
                    handleSaveFleet();
                    break;
                case 8:
                    handleLoadFleet();
                    break;
                case 9:
                    handleSearchByType();
                    break;
                case 10:
                    handleListMaintenanceNeeds();
                    break;
                

                case 11:
                    fleetManager.sortFleetByModelName();
                    handleGenerateReport(); 
                    break;
                case 12:
                    fleetManager.sortFleetByMaxSpeed();
                    handleGenerateReport(); 
                    break;
                case 13:
                    fleetManager.sortFleetByEfficiency();
                    handleGenerateReport(); 
                    break;

                case 14:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            } // <-- close switch
        } // <-- close while
    } // <-- close main

    private static void displayMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Start Journey for all vehicles");
        System.out.println("4. Refuel All eligible vehicles");
        System.out.println("5. Perform Maintenance for all vehicles");
        System.out.println("6. Generate Fleet Report");
        System.out.println("7. Save Fleet to File");
        System.out.println("8. Load Fleet from File");
        System.out.println("9. Search by Vehicle Type");
        System.out.println("10. List Vehicles Needing Maintenance");  
        System.out.println("11. Sort Fleet by Model Name (then view report)");
        System.out.println("12. Sort Fleet by Max Speed (then view report)");
        System.out.println("13. Sort Fleet by Efficiency (then view report)");
        System.out.println("14. Exit");
        System.out.println("==========================");
    }

    private static void handleAddVehicle() {
        System.out.println("Enter vehicle type (Car, Truck, Bus, Airplane, CargoShip):");
        String type = scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        double maxSpeed = getDoubleInput("Enter Max Speed (km/h): ");
        scanner.nextLine(); // Consume newline

        try {
            Vehicle vehicle = null;
            switch (type.toLowerCase()) {
                case "car":
                    vehicle = new Car(id, model, maxSpeed);
                    break;
                case "truck":
                    vehicle = new Truck(id, model, maxSpeed);
                    break;
                case "bus":
                    vehicle = new Bus(id, model, maxSpeed);
                    break;
                case "airplane":
                    double maxAltitude = getDoubleInput("Enter Max Altitude (feet): ");
                    scanner.nextLine(); // Consume newline
                    vehicle = new Airplane(id, model, maxSpeed, maxAltitude);
                    break;
                case "cargoship":
                    System.out.print("Does it have a sail? (true/false): ");
                    boolean hasSail = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    vehicle = new CargoShip(id, model, maxSpeed, hasSail);
                    break;
                default:
                    System.out.println("Invalid vehicle type.");
                    return;
            }
            fleetManager.addVehicle(vehicle);
        } catch (InvalidOperationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleRemoveVehicle() {
        System.out.print("Enter the ID of the vehicle to remove: ");
        String id = scanner.nextLine();
        try {
            fleetManager.removeVehicle(id);
        } catch (InvalidOperationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleStartJourney() {
        double distance = getDoubleInput("Enter the distance for the journey (km): ");
        scanner.nextLine(); // Consume newline
        fleetManager.startAllJourneys(distance);
    }
    
    private static void handleRefuelAll() {
        double amount = getDoubleInput("Enter amount to refuel (liters): ");
        scanner.nextLine(); // Consume newline
        for (Vehicle v : fleetManager.getFleet()) {
            if (v instanceof FuelConsumable) {
                try {
                    ((FuelConsumable) v).refuel(amount);
                    System.out.println("Refueled " + v.getId() + " successfully.");
                } catch (InvalidOperationException e) {
                     System.err.println("Could not refuel " + v.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    private static void handlePerformMaintenance() {
        fleetManager.maintainAll();
    }

    private static void handleGenerateReport() {
        System.out.println(fleetManager.generateReport());
    }

    private static void handleSaveFleet() {
        System.out.print("Enter filename to save to (e.g., fleet_data.csv): ");
        String filename = scanner.nextLine();
        try {
            fleetManager.saveToFile(filename);
            System.out.println("Fleet saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    private static void handleLoadFleet() {
        System.out.print("Enter filename to load from (e.g., fleet_data.csv): ");
        String filename = scanner.nextLine();
        try {
            fleetManager.loadFromFile(filename);
            System.out.println("Fleet loaded successfully from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    private static void handleSearchByType() {
        System.out.print("Enter vehicle type to search for: ");
        String type = scanner.nextLine();
        List<Vehicle> results = fleetManager.searchByType(type);
        if (results.isEmpty()) {
            System.out.println("No vehicles found of type '" + type + "'.");
        } else {
            System.out.println("\n--- Found " + results.size() + " Vehicle(s) of type " + type + " ---");
            for (Vehicle v : results) {
                v.displayInfo();
            }
        }
    }

    private static void handleListMaintenanceNeeds() {
        List<Vehicle> results = fleetManager.getVehiclesNeedingMaintenance();
         if (results.isEmpty()) {
            System.out.println("No vehicles currently need maintenance.");
        } else {
            System.out.println("\n--- Vehicles Needing Maintenance ---");
            for (Vehicle v : results) {
                v.displayInfo();
            }
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
                scanner.nextLine(); 
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); 
            }
        }
    }

    private static void runDemo() {
        System.out.println("\n--- Running Initial Demo ---");
        try {
            fleetManager.addVehicle(new Car("C1", "Toyota Camry", 180.0));
            fleetManager.addVehicle(new Truck("T1", "Ford F-150", 160.0));
            fleetManager.addVehicle(new Airplane("A1", "Boeing 747", 900.0, 35000.0));
            System.out.println(fleetManager.generateReport());
            fleetManager.startAllJourneys(100); 


            System.out.println("\n--- Demo Complete ---\n");
        } catch (InvalidOperationException e) {
            System.err.println("Demo setup failed: " + e.getMessage());
        }
    }
}
