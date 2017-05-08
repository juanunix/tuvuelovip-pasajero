package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

/**
 * Created by alacret on 1/31/17.
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.passanger.flight.FlightView;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;

public class ExpandableFlightsRequestList extends BaseExpandableListAdapter {

    public static final String TAG = ExpandableFlightsRequestList.class.getSimpleName();
    private Fragment parentFragment;
    private ExpandableListView expandableView;
    private List<FlightRequest> data;
    private Map<String, Airport> airportsMap = null;

    public ExpandableFlightsRequestList(Fragment fragment, List<FlightRequest> result, ExpandableListView expandableView) {
        this.parentFragment = fragment;
        this.expandableView = expandableView;
        this.data = result;

        List<Airport> airportList = null;
        Log.d(TAG, "getting airports");
        try {
            airportList = Cache.getAirports();
        } catch (IOException e) {
            Log.e("ExpandableFlightsRList", e.getLocalizedMessage());
        }

        this.airportsMap = new HashMap<>(airportList.size());
        for (Airport airport : airportList)
            this.airportsMap.put(String.valueOf(airport.id), airport);
    }

    /**
     * Update the Data in the Adapter
     * @param newData
     */
    public void updateData(List<FlightRequest> newData){
        this.data = newData;
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPositition) {
        return this.data.get(groupPosition).responses.get(childPositition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View view, ViewGroup parent) {
        final Flight flight = (Flight) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parentFragment.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        Airport outAirport = airportsMap.get(String.valueOf(flight.outAirportId));
        if (outAirport != null)
            ((TextView) view.findViewById(R.id.outIata)).setText(outAirport.getIata());

        Airport inAirport = airportsMap.get(String.valueOf(flight.enterAirportId));
        if (inAirport != null)
            ((TextView) view.findViewById(R.id.inIata)).setText(inAirport.getIata());

        Date outDate = flight.outDateDate;
        if (outDate != null) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("d");
            SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
            SimpleDateFormat sdfHour = new SimpleDateFormat("k");
            String day = sdfDay.format(outDate);
            String month = sdfMonth.format(outDate);
            String hour = sdfHour.format(outDate);
            ((TextView) view.findViewById(R.id.outDate)).setText(day + " " + month);
            ((TextView) view.findViewById(R.id.outHour)).setText(hour + ":00 ");
        }

        ((TextView) view.findViewById(R.id.journey_seats)).setText(String.valueOf(flight.getSeats()));
        ((TextView) view.findViewById(R.id.flight_price)).setText(flight.getPrice() + "USD");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlightView detailView = new FlightView();
                Bundle bundle = new Bundle();
                bundle.putSerializable("flightId", flight.getId());
                detailView.onCreate(bundle);
                Util.addRollbackableTransaction(R.id.main_frame, parentFragment.getFragmentManager(), detailView, ViewTags.FLIGHT_DETAIL);
            }
        });

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.data.get(groupPosition).responses.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.data.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.data.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View itemView, ViewGroup parent) {
        final FlightRequest flightRequest = (FlightRequest) getGroup(groupPosition);
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) this.parentFragment.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.row_flight_request, null);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("flightRequest", flightRequest);
                Fragment flightRequestView = new FlightRequestView();
                flightRequestView.setArguments(bundle);
                Util.addRollbackableTransaction(R.id.sub_frame, parentFragment.getFragmentManager(),
                        flightRequestView, ViewTags.NEW_FLIGHT_REQUEST);
            }
        });

        TextView outHour = (TextView) itemView.findViewById(R.id.outHour);
        TextView outDateTextView = (TextView) itemView.findViewById(R.id.outDate);
        TextView outAirportName = (TextView) itemView.findViewById(R.id.outAirportName);
        TextView inAirportName = (TextView) itemView.findViewById(R.id.inAirportName);
        TextView inIata = (TextView) itemView.findViewById(R.id.inIata);
        TextView outIata = (TextView) itemView.findViewById(R.id.outIata);
        TextView seats = (TextView) itemView.findViewById(R.id.journey_seats);
        TextView answersNumber = (TextView) itemView.findViewById(R.id.answers_number);
        final ImageView answerButtonRed = (ImageView) itemView.findViewById(R.id.answers_button_red);
        final ImageView answerButtonGold = (ImageView) itemView.findViewById(R.id.answers_button_gold);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded)
                    expandableView.collapseGroup(groupPosition);
                else {
                    expandableView.expandGroup(groupPosition);
                    Cache.addFlighRequestViewed(flightRequest.id);
                    answerButtonRed.setVisibility(View.GONE);
                    answerButtonGold.setVisibility(View.VISIBLE);
                }
            }
        };

        answerButtonRed.setOnClickListener(listener);
        answerButtonGold.setOnClickListener(listener);

        Set<String> listOfFlightRequestViewed =  Cache.getListOfFlighRequestViewed();
        if(listOfFlightRequestViewed.contains(String.valueOf(flightRequest.id)) || flightRequest.num_responses == 0){
            answerButtonRed.setVisibility(View.GONE);
            answerButtonGold.setVisibility(View.VISIBLE);
        }else{
            answerButtonRed.setVisibility(View.VISIBLE);
            answerButtonGold.setVisibility(View.GONE);
        }

        Date outDate = flightRequest.getOutDate();
        if (outDate != null) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("d");
            SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
            SimpleDateFormat sdfHour = new SimpleDateFormat("k");
            String day = sdfDay.format(outDate);
            String month = sdfMonth.format(outDate);
            String hour = sdfHour.format(outDate);
            outDateTextView.setText(day + " " + month);
            outHour.setText(hour + ":00 ");
        }

        String inAirportId = String.valueOf(flightRequest.getEnterAirport_id());
        String outAirportId = String.valueOf(flightRequest.getOutAirport_id());
        Airport outAirport = airportsMap.get(outAirportId);
        if (outAirport != null) {
            Log.e("ExpandableFlightsRList", "ID: " + outAirportId + " not Found in airports list");
            outAirportName.setText(outAirport.getCityName());
            outIata.setText(outAirport.getIata());
        }
        Airport inAirport = airportsMap.get(inAirportId);
        if (inAirport != null) {
            Log.e("ExpandableFlightsRList", "ID: " + inAirportId + " not Found in airports list");
            inAirportName.setText(inAirport.getCityName());
            inIata.setText(inAirport.getIata());
        }
        seats.setText(String.valueOf(flightRequest.getSeats()));
        answersNumber.setText("(" + flightRequest.num_responses + ")");
        return itemView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}