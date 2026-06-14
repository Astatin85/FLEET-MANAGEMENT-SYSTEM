package interfaces;

import exceptions.InvalidOperationException;
import exceptions.OverloadException;

public interface PassangerCarrier {
    void boardPassengers(int count) throws OverloadException, InvalidOperationException;

    int getCurrentPassengers();
    int getPassengerCapacity();
    void disembarkPassengers(int count) throws InvalidOperationException;

}
