package fourgeeks.tuvuelovip.pasajero.passanger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.FlightRequestView;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.MyFlightsRequestView;
import fourgeeks.tuvuelovip.pasajero.passanger.flight.FlightsView;
import fourgeeks.tuvuelovip.pasajero.passanger.myflights.MyFlightsView;
import fourgeeks.tuvuelovip.pasajero.R;


public class TabView extends Fragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_view, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_slider);
        FloatingActionButton request_button = (FloatingActionButton) view.findViewById(R.id.request_button);
        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Cache.termsAndConditionsWereAccepted())
                    Util.addRollbackableTransaction(R.id.main_frame, getFragmentManager(), new FlightRequestView());
                else
                    Toast.makeText(getActivity().getApplication(), getString(R.string.must_accept_terms), Toast.LENGTH_LONG).show();
            }
        });
        final PagerAdapter pagerAdapter = new MyFragmentAdapter(getFragmentManager(), getActivity());
        viewPager.setAdapter(pagerAdapter);
        return view;
    }

    public ViewPager getViewPager(){
        return viewPager;
    }

    private static class MyFragmentAdapter extends FragmentStatePagerAdapter {
        private Fragment[] tabs;
        private String[] names;

        public MyFragmentAdapter(FragmentManager fm, FragmentActivity activity) {
            super(fm);
            tabs = new Fragment[]{new FlightsView(), new MyFlightsRequestView(),
                    new MyFlightsView()};
            names = new String[]{activity.getString(R.string.availables_flights),
                    activity.getString(R.string.my_flight_requests),
                    activity.getString(R.string.my_flights)};
        }

        @Override
        public Fragment getItem(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public String getPageTitle(int position) {
            return names[position];
        }

    }


}
