package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.passanger.myflights.MyFlightsRetroFitInterface;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import retrofit2.Retrofit;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 3/27/17.
 */

class FlightsService implements FlightsServiceInterface {
    private final FlightsRetroFitInterface service;

    FlightsService(){
        Retrofit retrofit = Cache.getAuthRetrofitInstance();
        service = retrofit.create(FlightsRetroFitInterface.class);
    }

    @Override
    public Single<Void> getFlights() {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.getFlights().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> flightRequests) {
                        StateManager.instance().getFlightsSubject().onNext(flightRequests);
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
    public Single<Void> getFlights(final String query) {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.getFlights(query).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> flightRequests) {
                        StateManager.instance().getFlightsSubject().onNext(flightRequests);
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
}
