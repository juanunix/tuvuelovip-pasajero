package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by alacret on 3/23/17.
 */

public class MyFlightRequestController {

    private FlightRequestsServiceInterface service;

    public MyFlightRequestController(FlightRequestsServiceInterface service){
        this.service = service;
    }

    public Single<Void> getMyFlightRequests() {
        return service.getMyFlightRequests();
    }

    public Single<Void> getMyFlightRequests(String query) {
        return service.getMyFlightRequests(query);
    }
}
