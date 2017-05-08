package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;


public class MyFlightsRequestView extends Fragment {
    public static final String TAG = MyFlightsRequestView.class.getSimpleName();
    private ExpandableListView expandableListView;
    private ProgressBar progressBar;
    private MyFlightRequestController controller;
    private Subscription subscription;
    private ExpandableFlightsRequestList adapter;
    private boolean queryMode = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_flights_requests_view, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        adapter = new ExpandableFlightsRequestList(MyFlightsRequestView.this, new ArrayList<FlightRequest>(), expandableListView);
        expandableListView.setAdapter(adapter);
        setHasOptionsMenu(true);
        controller = new MyFlightRequestController(new FlightRequestsService());
        //Subscribe to the App global State
        subscription = StateManager.instance().getFlightRequestsSubject().subscribe(new Observer<List<FlightRequest>>() {

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<FlightRequest> flightRequests) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "getData:onResponse" + flightRequests);
                adapter.updateData(flightRequests);
                if (flightRequests.size() > 0) {
                    Cache.setMyFlightRequests(flightRequests);
                }
            }
        });

        getData();
        return view;
    }


    public void getData(String query) {
        Log.d(TAG, "getData");
        progressBar.setVisibility(View.VISIBLE);

        if (Util.isOnline(getContext()))
            controller.getMyFlightRequests(query).subscribe(new SingleSubscriber<Void>() {
                @Override
                public void onSuccess(Void v) {
                    Log.e(TAG, "getData:onSuccess");
                }

                @Override
                public void onError(Throwable e) {
                    String msg = Util.msgFromRetrofitThrowable(MyFlightsRequestView.this, e);
                    Log.e(TAG, "getData:onError:" + msg);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
    }

    public void getData() {
        Log.d(TAG, "getData");
        progressBar.setVisibility(View.VISIBLE);

        if (Util.isOnline(getContext()))
            controller.getMyFlightRequests().subscribe(new SingleSubscriber<Void>() {
                @Override
                public void onSuccess(Void v) {
                    Log.e(TAG, "getData:onSuccess");
                }

                @Override
                public void onError(Throwable e) {
                    String msg = Util.msgFromRetrofitThrowable(MyFlightsRequestView.this, e);
                    Log.e(TAG, "getData:onError:" + msg);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        else {
            Single<List<FlightRequest>> myFlightRequests = Cache.getMyFlightRequests();
            if (myFlightRequests != null) {
                myFlightRequests.subscribe(new SingleSubscriber<List<FlightRequest>>() {
                    @Override
                    public void onSuccess(List<FlightRequest> flightRequests) {
                        StateManager.instance().getFlightRequestsSubject().onNext(flightRequests);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = Util.msgFromRetrofitThrowable(MyFlightsRequestView.this, e);
                        Log.e(TAG, "getData:onError:" + msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryMode = true;
                getData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty()) {
                    if (queryMode == true) {
                        queryMode = false;
                        getData();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Update:
                getData();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
