package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 3/23/17.
 */

public class FlightRequestsService implements FlightRequestsServiceInterface {

    private final FlightRequestsRetroFitService service;

    FlightRequestsService() {
        Retrofit retrofit = Cache.getAuthRetrofitInstance();
        service = retrofit.create(FlightRequestsRetroFitService.class);
    }

    @Override
    public Single<Void> getMyFlightRequests() {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.getFlightRequest().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<List<FlightRequest>>() {
                    @Override
                    public void onSuccess(List<FlightRequest> flightRequests) {
                        StateManager.instance().getFlightRequestsSubject().onNext(flightRequests);
                        singleSubscriber.onSuccess(null);
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
    public Single<Void> getMyFlightRequests(final String query) {
        Single<Void> single = Single.create(new Single.OnSubscribe<Void>() {

            @Override
            public void call(final SingleSubscriber<? super Void> singleSubscriber) {
                service.getFlightRequest(query).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle().subscribe(new SingleSubscriber<List<FlightRequest>>() {
                    @Override
                    public void onSuccess(List<FlightRequest> flightRequests) {
                        StateManager.instance().getFlightRequestsSubject().onNext(flightRequests);
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