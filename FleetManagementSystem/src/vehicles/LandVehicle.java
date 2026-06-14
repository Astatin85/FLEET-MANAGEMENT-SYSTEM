package vehicles;

public abstract class LandVehicle extends Vehicle {
    private int numWheels;

    public LandVehicle(String id, String model, double maxSpeed, int numWheels){
        super(id, model, maxSpeed);
        this.numWheels = numWheels;
    }

    public int getNumWheels() {
        return numWheels;

    }

    public double estimateJourneyTime(double dist){

        if(getMaxSpeed()<= 0){
            return Double.POSITIVE_INFINITY;
        }
        double baseTime =  dist / getMaxSpeed();

        return baseTime * 1.10;
    }
}
