package fourgeeks.tuvuelovip.pasajero.passanger.airport;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.NotificationInfo;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by alacret on 4/17/17.
 */

public interface AirportRetroFitService {
    @GET("/v3/mobile/profile/get-notify/")
    Observable<NotificationInfo> getNotificationsInfo();

    @PUT("/v3/mobile/profile-pilot/set-notify/")
    Observable<Void> setNotificationsInfo(@Body NotificationInfo notificationInfo);
}
