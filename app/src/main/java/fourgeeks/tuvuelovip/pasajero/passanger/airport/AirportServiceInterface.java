package fourgeeks.tuvuelovip.pasajero.passanger.airport;

import fourgeeks.tuvuelovip.pasajero.pojo.NotificationInfo;
import rx.Single;

/**
 * Created by alacret on 4/17/17.
 */

public interface AirportServiceInterface {

    Single<NotificationInfo> getNotificationsInfo();

    Single<Void> setNotificationsInfo(NotificationInfo notificationInfo);
}
