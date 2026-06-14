package vehicles;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import interfaces.*;

public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable {

    private double fuelLevel;
    private final double cargoCap = 5000.0;
    private double currCargo;
    private boolean maintenanceNeeded;

    public Truck(String id, String model, double maxSpeed) {
        super(id, model, maxSpeed, 6);
        this.fuelLevel = 0;
        this.currCargo = 0;
        this.maintenanceNeeded = false;
    }

    @Override
    public double calculateFuelEfficiency() {
        double baseEfficiency = 8.0;
        if (this.currCargo > (this.cargoCap * 0.5)) {
            return baseEfficiency * 0.90;
        }
        return baseEfficiency;
    }

    @Override
    public void move(double dist) throws InvalidOperationException, InsufficientFuelException {
        if (dist < 0) {
            throw new InvalidOperationException("Distance can't be negative.");
        }

        double fuelReq = dist / calculateFuelEfficiency();
        if (fuelLevel < fuelReq) {
            throw new InsufficientFuelException("Not enough fuel to travel " + dist + " km.");
        }

        consumeFuel(dist);
        addMileage(dist);
        System.out.println("Truck " + getId() + " is hauling cargo for " + dist + " km.");
    }

    @Override
    public void refuel(double amt) throws InvalidOperationException {
        if (amt <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive.");
        }
        this.fuelLevel += amt;
    }

    @Override
    public double getFuelLevel() {
        return this.fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        double fuelConsumed = distance / calculateFuelEfficiency();
        if (fuelConsumed > this.fuelLevel) {
            throw new InsufficientFuelException("Attempted to consume more fuel than available.");
        }
        this.fuelLevel -= fuelConsumed;
        return fuelConsumed;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currCargo + weight > cargoCap) {
            throw new OverloadException("Cannot load " + weight + "kg. Exceeds remaining capacity of " + (cargoCap - currCargo) + "kg.");
        }
        this.currCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currCargo) {
            throw new InvalidOperationException("Cannot unload " + weight + "kg. Only " + currCargo + "kg is on board.");
        }
        this.currCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {
        return this.cargoCap;
    }

    @Override
    public double getCurrentCargo() {
        return this.currCargo;
    }

    @Override
    public void scheduleMaintenance() {
        this.maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {
        return getCurrentMileage() > 10000 || this.maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {
        this.maintenanceNeeded = false;
    }

    @Override
    public int compareTo(Vehicle  thatVehicle) {
    // Example: compare by id
        return this.getId().compareTo(thatVehicle.getId());
    }
}

