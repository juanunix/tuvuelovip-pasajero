package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import rx.Single;

/**
 * Created by alacret on 3/27/17.
 */

interface FlightsServiceInterface {
    Single<Void> getFlights();
    Single<Void> getFlights(String query);
}
