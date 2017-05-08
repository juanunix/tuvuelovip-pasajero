package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by alacret on 3/24/17.
 */

public interface FlightRequestsServiceInterface {
     Single<Void> getMyFlightRequests();
     Single<Void> getMyFlightRequests(String query);
}
