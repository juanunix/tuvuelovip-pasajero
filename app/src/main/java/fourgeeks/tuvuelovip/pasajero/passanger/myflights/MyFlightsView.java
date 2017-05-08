package fourgeeks.tuvuelovip.pasajero.passanger.myflights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import fourgeeks.tuvuelovip.pasajero.passanger.flight.FlightsView;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.R;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;


public class MyFlightsView extends Fragment {
    private static final String TAG = MyFlightsView.class.getSimpleName();
    private RecyclerView recycleView;
    private ProgressBar progressBar;
    private MyFlightsController controller;
    private Subscription subscription;
    private boolean queryMode = false;
    private MyFlightsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_misvuelos, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        controller = new MyFlightsController(new MyFlightsService());
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new MyFlightsAdapter(new ArrayList<Flight>(), this, controller);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        //Subscribe to the App global State
        subscription = StateManager.instance().getMyFlightsSubject().subscribe(new Observer<List<Flight>>() {

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<Flight> flights) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "getData:onResponse" + flights);
                adapter.updateData(flights);
                if (flights.size() > 0) {
                    Cache.setMyFlights(flights);
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
            controller.getMyFlights(query).subscribe(new SingleSubscriber<Void>() {
                @Override
                public void onSuccess(Void v) {
                    Log.e(TAG, "getData:onSuccess");
                }

                @Override
                public void onError(Throwable e) {
                    String msg = Util.msgFromRetrofitThrowable(MyFlightsView.this, e);
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
            controller.getMyFlights().subscribe(new SingleSubscriber<Void>() {
                @Override
                public void onSuccess(Void v) {
                    Log.e(TAG, "getData:onSuccess");
                }

                @Override
                public void onError(Throwable e) {
                    String msg = Util.msgFromRetrofitThrowable(MyFlightsView.this, e);
                    Log.e(TAG, "getData:onError:" + msg);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        else {
            Single<List<Flight>> flights = Cache.getMyFlights();
            if (flights != null) {
                flights.subscribe(new SingleSubscriber<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> flights) {
                        StateManager.instance().getMyFlightsSubject().onNext(flights);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = Util.msgFromRetrofitThrowable(MyFlightsView.this, e);
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