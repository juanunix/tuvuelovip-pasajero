package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alacret on 3/14/17.
 */

public interface FlightsRetroFitInterface {

    @GET("v3/mobile/flight/")
    Observable<List<Flight>> getFlights();

    @GET("v3/mobile/flight/")
    Observable<List<Flight>> getFlights(@Query("search") String query);


}
