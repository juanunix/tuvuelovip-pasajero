package fourgeeks.tuvuelovip.pasajero.passanger.airport;

import fourgeeks.tuvuelovip.pasajero.pojo.NotificationInfo;
import rx.Single;

/**
 * Created by alacret on 4/17/17.
 */

public class AirportController {
    private AirportServiceInterface service;

    public AirportController(AirportServiceInterface service) {
        this.service = service;
    }
    public Single<NotificationInfo> getNotificationsInfo(){
        return service.getNotificationsInfo();
    }
    public Single<Void> setNotificationInfo(boolean notificationInfo) {
        return service.setNotificationsInfo(new NotificationInfo(notificationInfo));
    }
}
