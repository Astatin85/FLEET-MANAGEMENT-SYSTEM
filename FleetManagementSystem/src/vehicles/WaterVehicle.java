package vehicles;

public abstract class WaterVehicle extends Vehicle {
    private boolean hasSail;

    public boolean hasSail() {
        return hasSail;
    }


    public WaterVehicle(String id, String model, double maxSpeed, boolean hasSail) {
        super(id, model, maxSpeed);
        this.hasSail = hasSail;
    }
    public double estimateJourneyTime(double dist){

        if(getMaxSpeed()<= 0){
            return Double.POSITIVE_INFINITY;
        }
        double baseTime =  dist / getMaxSpeed();

        return baseTime * 1.15;
    }


}
