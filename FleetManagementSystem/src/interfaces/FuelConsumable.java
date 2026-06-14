package interfaces;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;

public interface FuelConsumable {

    void refuel(double amt) throws InvalidOperationException;
    double consumeFuel(double dist) throws InsufficientFuelException, InvalidOperationException;

    double getFuelLevel();

}
