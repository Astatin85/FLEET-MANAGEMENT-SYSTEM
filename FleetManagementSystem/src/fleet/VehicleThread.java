package fleet;

import vehicles.Vehicle;
import interfaces.FuelConsumable;
import javax.swing.SwingUtilities;



public class VehicleThread implements Runnable {

    private Vehicle vehicle;

    private Highway highway;
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private Runnable updateGuiCallback;



    public VehicleThread(Vehicle vehicle, Highway highway, Runnable updateGuiCallback) {
        this.vehicle = vehicle;
        this.highway = highway;

        this.updateGuiCallback = updateGuiCallback;

    }

    public void start() {
        running = true;
        paused = false;
        new Thread(this).start();

    }

    public void stop() {
        running = false;
    }


    public void refuelAndResume() {
        if (vehicle instanceof FuelConsumable) {
            try {

                ((FuelConsumable) vehicle).refuel(50.0); 
                paused = false; 
            } catch (Exception e) {}
        }

    }

    @Override
    public void run() {
        while (running) {
            if (!paused) {
                try {
                    int distanceStep = 5; 
                    vehicle.move(distanceStep); 

                    highway.addDistance(distanceStep);

                    SwingUtilities.invokeLater(updateGuiCallback);

                    Thread.sleep(100); 

                } catch (Exception e) {
                    paused = true;
                    SwingUtilities.invokeLater(updateGuiCallback);
                }
            } else {
                
                try { 

                    Thread.sleep(200); 
                } catch (InterruptedException e) {}
            }
        }


    }

}