package vehicles;

public  abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    public AirVehicle(String id, String model, double maxSpeed, double maxAltitute){
        super(id, model, maxSpeed);
        this.maxAltitude = maxAltitute;
    }

    public double maxAltitude() {
        return maxAltitude;

    }

    public double estimateJourneyTime(double dist){

        if(getMaxSpeed()<= 0){
            return Double.POSITIVE_INFINITY;
        }
        double baseTime =  dist / getMaxSpeed();

        return baseTime * 0.95;
    }
}
