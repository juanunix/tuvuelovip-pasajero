package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.R;


public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.Holder> {

    private List<Flight> data = Collections.emptyList();
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private Map<String, Airport> airportsMap;

    public void updateData(List<Flight> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public FlightsAdapter(List<Flight> data, AppCompatActivity activity) {
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.activity = activity;
        this.data = data;

        List<Airport> airportList = null;
        try {
            airportList = Cache.getAirports();
        } catch (IOException e) {
            Log.e("FLIGHTSADAPTER", e.getLocalizedMessage());
        }
        airportsMap = new HashMap<>(airportList.size());
        for (Airport airport : airportList)
            this.airportsMap.put(String.valueOf(airport.id), airport);
    }

    @Override
    public FlightsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_flight, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Flight flight = data.get(position);
        holder.flightId = flight.getId();

        Airport outAirport = airportsMap.get(String.valueOf(flight.outAirportId));
        if (outAirport != null) {
            holder.iata_out_city.setText(outAirport.getCityName());
            holder.iata_out.setText(outAirport.getIata());
        }

        Airport inAirport = airportsMap.get(String.valueOf(flight.enterAirportId));
        airportsMap.get(String.valueOf(flight.enterAirportId));
        if (inAirport != null) {
            holder.iata_in_city.setText(inAirport.getCityName());
            holder.iata_in.setText(inAirport.getIata());
        }

        holder.airplane_model.setText(flight.getModelAirplane());
        holder.seats.setText(flight.getSeats());
        holder.flight_price.setText("$" + flight.getPrice());

        String outDate = flight.outDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
        SimpleDateFormat sdfHour = new SimpleDateFormat("k");

        sdf.setTimeZone(TimeZone.getDefault());
        try {
            Date outDateNative = sdf.parse(outDate);
            String day = sdfDay.format(outDateNative);
            String month = sdfMonth.format(outDateNative);
            String hour = sdfHour.format(outDateNative);
            holder.flight_date.setText(day + "\n" + month);
            holder.flight_schedule.setText(hour + ":00 ");
        } catch (ParseException e) {
            Log.e("FlightsAdapter", "onBindViewHolder:ParseException:" + e.getLocalizedMessage());
        }

        if(flight.images != null && flight.images.size() > 0) {
            String imageUrl = flight.images.get(0);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(activity).load(imageUrl).into(holder.airplane_photo);
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        //AIRPLANE
        ImageView airplane_photo;
        TextView airplane_model;
        //IATA
        TextView iata_out;
        TextView iata_in;
        TextView iata_out_city;
        TextView iata_in_city;
        //FLIGHT
        TextView flight_price;
        TextView flight_date;
        TextView flight_schedule, seats;
        String flightId = null;

        public Holder(View itemView) {
            super(itemView);

            //AIRPLANE
            airplane_photo = (ImageView) itemView.findViewById(R.id.airplane_photo);
            airplane_model = (TextView) itemView.findViewById(R.id.airplane_model);
            //IATA
            iata_out = (TextView) itemView.findViewById(R.id.iata_out);
            iata_in = (TextView) itemView.findViewById(R.id.iata_in);
            iata_out_city = (TextView) itemView.findViewById(R.id.iata_out_city);
            iata_in_city = (TextView) itemView.findViewById(R.id.iata_in_city);
            //FLIGHT
            flight_price = (TextView) itemView.findViewById(R.id.flight_price);
            flight_date = (TextView) itemView.findViewById(R.id.flight_date);
            flight_schedule = (TextView) itemView.findViewById(R.id.flight_schedule);
            seats = (TextView) itemView.findViewById(R.id.journey_seats);
            airplane_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    FlightView flightView = new FlightView();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("flightId", flightId);
                    flightView.onCreate(bundle);

                    transaction.addToBackStack(FlightView.class.getSimpleName());
                    transaction.add(R.id.main_frame, flightView, FlightView.class.getSimpleName());
                    transaction.commit();

                }

            });


        }

    }

}
