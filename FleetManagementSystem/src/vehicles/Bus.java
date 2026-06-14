package vehicles;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import interfaces.*;
public class Bus extends LandVehicle implements FuelConsumable, PassangerCarrier, CargoCarrier, Maintainable {

    private double fuelLevel;
    private final int passengerCap = 50;
    private int currPassengers;
    private final double cargoCap = 500.0;
    private double currCargo;
    private boolean maintenanceNeeded;

    public Bus(String id, String model, double maxSpeed) {
        super(id, model, maxSpeed, 6);
        this.fuelLevel = 0;
        this.currPassengers = 0;
        this.currCargo = 0;
        this.maintenanceNeeded = false;
    }
    

    @Override
    public double calculateFuelEfficiency() {
        return 10.0;
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
        System.out.println("Bus " + getId() + " is transporting passengers and cargo for " + dist + " km.");
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
    public void boardPassengers(int count) throws OverloadException {
        if (currPassengers + count > passengerCap) {
            throw new OverloadException("Cannot board " + count + " passengers. Exceeds capacity of " + passengerCap + ".");
        }
        this.currPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currPassengers) {
            throw new InvalidOperationException("Cannot disembark " + count + " passengers. Only " + currPassengers + " are on board.");
        }
        this.currPassengers -= count;
    }

    @Override
    public int getPassengerCapacity() {
        return this.passengerCap;
    }

    
    @Override
    public int getCurrentPassengers() {
        return this.currPassengers;
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
public int compareTo(Vehicle thatVehicle) {
    return this.getId().compareTo(thatVehicle.getId());

    }
}
