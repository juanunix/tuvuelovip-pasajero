package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;


public class FlightsView extends Fragment {
    private static final String TAG = "FlightsView";
    private RecyclerView recycleView;
    private ProgressBar progressBar;
    private FlightController controller;
    private Subscription subscription;
    private FlightsAdapter adapter;
    private boolean queryMode = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_flight, container, false);
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new FlightsAdapter(new ArrayList<Flight>(), (AppCompatActivity) getActivity());
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        controller = new FlightController(new FlightsService());
        setHasOptionsMenu(true);
        //Subscribe to the App global State
        subscription = StateManager.instance().getFlightsSubject().subscribe(new Observer<List<Flight>>() {

            @Override
            public void onCompleted() {
                Log.d(TAG, "onCreateView:onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onCreateView:onError" + e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<Flight> flights) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onCreateView:onNext" + flights);
                adapter.updateData(flights);
                if (flights.size() > 0) {
                    Cache.setFlights(flights);
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
            controller.getFlights(query).subscribe(new SingleSubscriber<Void>() {
                @Override
                public void onSuccess(Void v) {
                    Log.d(TAG, "getData:onSuccess");
                }

                @Override
                public void onError(Throwable e) {
                    String msg = Util.msgFromRetrofitThrowable(FlightsView.this, e);
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
            controller.getFlights().subscribe(new SingleSubscriber<Void>() {
                @Override
                public void onSuccess(Void v) {
                    Log.d(TAG, "getData:onSuccess");
                }

                @Override
                public void onError(Throwable e) {
                    String msg = Util.msgFromRetrofitThrowable(FlightsView.this, e);
                    Log.e(TAG, "getData:onError:" + msg);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        else {
            Single<List<Flight>> flights = Cache.getFlights();
            if (flights != null) {
                flights.subscribe(new SingleSubscriber<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> flights) {
                        StateManager.instance().getFlightsSubject().onNext(flights);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = Util.msgFromRetrofitThrowable(FlightsView.this, e);
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
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
