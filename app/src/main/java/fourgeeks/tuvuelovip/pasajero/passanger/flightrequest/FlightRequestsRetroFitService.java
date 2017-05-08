package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alacret on 3/13/17.
 */

public interface FlightRequestsRetroFitService {
    @GET("v3/mobile/myFlightRequest/")
    Observable<List<FlightRequest>> getFlightRequest();

    @GET("v3/mobile/myFlightRequest/")
    Observable<List<FlightRequest>> getFlightRequest(@Query("search") String query);

}
