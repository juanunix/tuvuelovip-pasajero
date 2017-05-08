package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import rx.Single;

/**
 * Created by alacret on 3/27/17.
 */

class FlightController {
    private FlightsServiceInterface service;

    FlightController(FlightsServiceInterface service) {
        this.service = service;
    }

    public Single<Void> getFlights() {
        return service.getFlights();
    }

    public Single<Void> getFlights(String query) {
        return service.getFlights(query);
    }
}
