package vehicles;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import interfaces.*;

public class Airplane extends AirVehicle implements FuelConsumable, PassangerCarrier, CargoCarrier, Maintainable {

    private double fuelLevel;
    private final int passengerCap = 200;
    private int currPassengers;
    private final double cargoCap = 10000.0;
    private double currCargo;
    private boolean maintenanceNeeded;

    public Airplane(String id, String model, double maxSpeed, double maxAltitude) {
        super(id, model, maxSpeed, maxAltitude);
        this.fuelLevel = 0;
        this.currPassengers = 0;
        this.currCargo = 0;
        this.maintenanceNeeded = false;
    }

    @Override
    public double calculateFuelEfficiency() {
        return 5.0;
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
        System.out.println("Airplane " + getId() + " is flying at " + maxAltitude() + " feet for " + dist + " km.");
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
    public void boardPassengers(int count) throws OverloadException, InvalidOperationException {
        if (count < 0) {
            throw new InvalidOperationException("Cannot board a negative number of passengers.");
        }
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
