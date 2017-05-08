package fourgeeks.tuvuelovip.pasajero.passanger.myflights;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.pojo.Rate;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import retrofit2.Retrofit;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 3/14/17.
 */

public class MyFlightsService implements MyFlightsServiceInterface {

    private final MyFlightsRetroFitInterface service;

    public MyFlightsService(){
        Retrofit retrofit = Cache.getAuthRetrofitInstance();
        service = retrofit.create(MyFlightsRetroFitInterface.class);
    }

    @Override
    public Single<Void> getMyFlights() {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.getMyFlights().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> myFlights) {
                        StateManager.instance().getMyFlightsSubject().onNext(myFlights);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        singleSubscriber.onError(throwable);
                    }
                });
            }
        });
        return single.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Void> getMyFlights(final String query) {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.getMyFlights(query).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> myFlights) {
                        StateManager.instance().getMyFlightsSubject().onNext(myFlights);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        singleSubscriber.onError(throwable);
                    }
                });
            }
        });
        return single.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Void> cancelFlight(String id) {
        return service.cancelFlight(id).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<Void> rateFlight(long flightId, Rate rate) {
        return service.rateFlight(flightId, rate).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<Void> rateFlightNotTaken(long flightId, Rate rate) {
        return service.rateFlightNotTaken(flightId, rate).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }
}
