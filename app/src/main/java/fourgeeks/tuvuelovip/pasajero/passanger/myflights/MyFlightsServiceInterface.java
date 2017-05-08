package fourgeeks.tuvuelovip.pasajero.passanger.myflights;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.Rate;
import rx.Single;

/**
 * Created by alacret on 3/14/17.
 */

interface MyFlightsServiceInterface {
    Single<Void> getMyFlights();
    Single<Void> getMyFlights(String query);
    Single<Void> cancelFlight(String id);
    Single<Void> rateFlight(long flightId, Rate rate);
    Single<Void> rateFlightNotTaken(long flightId, Rate rate);
}
