package vehicles;


import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;
import exceptions.OverloadException;

import interfaces.*;

public class Car extends LandVehicle implements FuelConsumable, Maintainable, PassangerCarrier {
	
    private double fuelLevel;
    private final int passengerCap = 5;
    private int currPassengers;
    private boolean maintenanceNeeded;

    public Car(String id, String model, double maxSpeed){
        super(id, model, maxSpeed, 4);
        this.fuelLevel = 0;
        this.maintenanceNeeded = false;
        this.currPassengers = 0;
    }

    public void move(double dist) throws InvalidOperationException, InsufficientFuelException{
        if (dist<0){
            throw new InvalidOperationException("Distance can't be negative.");
        } 

        double fuelReq = dist / calculateFuelEfficiency();
        if(fuelLevel < fuelReq){
            throw new InsufficientFuelException("not enough fuel to travel " + dist + " km.");
        }

        consumeFuel(dist);
        addMileage(dist);

        System.out.println("car " +getId() + " is driving on the road for "+ dist + " km.");
    }

    public double calculateFuelEfficiency(){
        return 15.0;
    }

    // Implementations of Interfaces--

    public void refuel(double amt) throws InvalidOperationException{
        if(amt <= 0){
            throw new InvalidOperationException("refuel amount must be positive.");
        }

        this.fuelLevel += amt;
        System.out.println("car " + getId() + " has been refueled. Current fuel level: " + String.format("%.2f", fuelLevel) + " liters.");
    }

    public double getFuelLevel(){
        return this.fuelLevel;
    }

    public double consumeFuel(double distance) throws InsufficientFuelException {
        double fuelConsumed = distance / calculateFuelEfficiency();
        if (fuelConsumed > this.fuelLevel) {
            throw new InsufficientFuelException("Attempted to consume more fuel than available.");
        }
        this.fuelLevel -= fuelConsumed;
        return fuelConsumed;
    }


    public void scheduleMaintenance() {
        this.maintenanceNeeded = true;
        System.out.println("Maintenance scheduled for Car " + getId() + ".");
    }

    public boolean needsMaintenance() {
        return getCurrentMileage() > 10000 || this.maintenanceNeeded;
    }

    public void performMaintenance() {
        this.maintenanceNeeded = false;
        System.out.println("Maintenance performed on Car " + getId() + ". Ready to go!");
    }




    public void boardPassengers(int count) throws OverloadException {
        if (currPassengers + count > passengerCap) {
            throw new OverloadException("Cannot board " + count + " passengers. "
                + "Exceeds capacity of " + passengerCap + ".");
        }
        this.currPassengers += count;
        System.out.println(count + " passengers boarded Car " + getId() + ". Current passengers: " + currPassengers);
    }

    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currPassengers) {
            throw new InvalidOperationException("Cannot disembark " + count + " passengers. "
                + "Only " + currPassengers + " are on board.");
        }
        this.currPassengers -= count;
        System.out.println(count + " passengers disembarked Car " + getId() + ". Current passengers: " + currPassengers);
    }
    public int getPassengerCapacity() {
        return this.passengerCap;
    }

    public int getCurrentPassengers() {
        return this.currPassengers;
    }

    @Override
public int compareTo(Vehicle thatVehicle) {
    return this.getId().compareTo(thatVehicle.getId());
}

}
