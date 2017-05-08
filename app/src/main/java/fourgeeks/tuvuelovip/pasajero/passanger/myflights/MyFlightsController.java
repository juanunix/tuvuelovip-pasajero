package fourgeeks.tuvuelovip.pasajero.passanger.myflights;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.Rate;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.Single;

/**
 * Created by alacret on 3/14/17.
 */

public class MyFlightsController {
    private MyFlightsServiceInterface service;

    public MyFlightsController(MyFlightsServiceInterface service) {
        this.service = service;

    }

    public Single<Void> getMyFlights() {
        return service.getMyFlights();
    }

    public Single<Void> getMyFlights(String query) {
        return service.getMyFlights(query);
    }

    public Single<Void> cancelFlight(String id) {
        return service.cancelFlight(id);
    }

    public Single<Void> rateFlight(long flightId, int rating, String review) {
        if(rating < 1 || rating > 5)
            return Util.createOnErrorSingle(String.valueOf(R.string.rate_error));

        if(review == null || review.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.review_error));

        return service.rateFlight(flightId, new Rate(rating, review));
    }

    public Single<Void> rateFlightNotTaken(long flightId, String review) {
        if(review == null || review.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.review_error));

        return service.rateFlightNotTaken(flightId, new Rate(review));
    }
}
