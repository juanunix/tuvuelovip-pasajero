package fourgeeks.tuvuelovip.pasajero;

import java.util.ArrayList;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import rx.subjects.BehaviorSubject;

/**
 * Created by alacret on 3/23/17.
 */

/**
 * Manejador de Estado
 */
public class StateManager{
    private static StateManager instance = new StateManager();

    private StateManager(){}

    /**
     * get the instance of the State Manager
     * @return
     */
    public static StateManager instance(){
        return instance;
    }

    private BehaviorSubject<List<FlightRequest>> flightRequestsSubject = BehaviorSubject.create(((List<FlightRequest>) new ArrayList<FlightRequest>()));

    /**
     * Get the subject for the FlightRequests state
     * @return the subject
     */
    public BehaviorSubject<List<FlightRequest>> getFlightRequestsSubject() {
        return flightRequestsSubject;
    }

    private BehaviorSubject<List<Flight>> flightsSubject = BehaviorSubject.create(((List<Flight>) new ArrayList<Flight>()));

    /**
     * * Get the subject for the Flights state
     * @return the subject
     */

    public BehaviorSubject<List<Flight>> getFlightsSubject() {
        return flightsSubject;
    }

    private BehaviorSubject<List<Flight>> myFlightsSubject = BehaviorSubject.create(((List<Flight>) new ArrayList<Flight>()));

    /**
     * * Get the subject for the My Flights state
     * @return the subject
     */

    public BehaviorSubject<List<Flight>> getMyFlightsSubject() {
        return myFlightsSubject;
    }

}
