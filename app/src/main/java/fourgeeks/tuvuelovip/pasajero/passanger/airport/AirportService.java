package fourgeeks.tuvuelovip.pasajero.passanger.airport;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.passanger.flight.FlightsRetroFitInterface;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.NotificationInfo;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import retrofit2.Retrofit;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 4/17/17.
 */

public class AirportService implements AirportServiceInterface{

    private final AirportRetroFitService service;

    public AirportService(){
        Retrofit retrofit = Cache.getAuthRetrofitInstance();
        service = retrofit.create(AirportRetroFitService.class);
    }

    @Override
    public Single<NotificationInfo> getNotificationsInfo() {
        Single<NotificationInfo> single = Single.create(new Single.OnSubscribe<NotificationInfo>() {

            @Override
            public void call(final SingleSubscriber<? super NotificationInfo> singleSubscriber) {
                service.getNotificationsInfo().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<NotificationInfo>() {
                    @Override
                    public void onSuccess(NotificationInfo notificationInfo) {
                        singleSubscriber.onSuccess(notificationInfo);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        singleSubscriber.onError(throwable);
                    }
                });
            }
        });
        return single;
    }

    @Override
    public Single<Void> setNotificationsInfo(final NotificationInfo notificationInfo) {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.setNotificationsInfo(notificationInfo).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<Void>() {
                    @Override
                    public void onSuccess(Void object) {
                        singleSubscriber.onSuccess(object);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        singleSubscriber.onError(throwable);
                    }
                });
            }
        });
        return single;
    }
}
