package fourgeeks.tuvuelovip.pasajero.passanger.myflights;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.Rate;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alacret on 3/14/17.
 */

public interface MyFlightsRetroFitInterface {

    @GET("v3/mobile/flightHistory/")
    Observable<List<Flight>> getMyFlights();

    @GET("v3/mobile/flightHistory/")
    Observable<List<Flight>> getMyFlights(@Query("search") String query);

    @POST("v3/mobile/flightHistory/{flightId}/cancel/")
    Observable<Void> cancelFlight(@Path("flightId") String flightId);

    @POST("/v3/mobile/flight/{flightId}/send-rating-passenger/")
    Observable<Void> rateFlight(@Path("flightId") long flightId, @Body Rate rate);

    @POST("/v3/mobile/flight/{flightId}/not-flyed-passenger/")
    Observable<Void> rateFlightNotTaken(@Path("flightId") long flightId, @Body Rate rate);
}