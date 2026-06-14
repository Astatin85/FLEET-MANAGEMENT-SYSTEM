package vehicles;

import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;

public abstract class Vehicle implements Comparable<Vehicle>{
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    public Vehicle(String id, String model, double maxSpeed, double currentMileage) {
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("id can not be null or empty.");
        }
        this.id = id;
        this.maxSpeed = maxSpeed;
        this.model = model;
        this.currentMileage = 0.0;
        
    }

    public abstract void move(double distance) throws InvalidOperationException, InsufficientFuelException;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);

    public Vehicle(String id, String model, double maxSpeed){
        
        if (id == null || model == null) {
            throw new IllegalArgumentException("Vehicle ID and model cannot be null.");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
    }

    public void displayInfo(){
        System.out.printf("ID: %-8s | Model: %-15s | Max Speed: %-7.2f km/h | Mileage: %-.2f km/h", this.id, this.model, this.maxSpeed, this.currentMileage);
    }

    public double getCurrentMileage(){
        return this.currentMileage;
    }
    
    public String getId(){
        return this.id;
    }
    public String getModel() {
        return model;
    }
    public double getMaxSpeed(){
        return this.maxSpeed;
    }

    public void addMileage(double dist){
        if(dist >0){
            this.currentMileage += dist;
        }
    }

    public String toCsvString() {

        return String.format("%s,%s,%s,%.2f",
            this.getClass().getSimpleName(),
            getId(),
            getModel(),
            getMaxSpeed());
    }

    public int comparedTo(Vehicle thatVehicle){
        return Double.compare(this.calculateFuelEfficiency(), thatVehicle.calculateFuelEfficiency());
    }
}