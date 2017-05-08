package fourgeeks.tuvuelovip.pasajero.passanger.myflights;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import fourgeeks.tuvuelovip.pasajero.passanger.myflights.confirmation.FlightNotTakenView;
import fourgeeks.tuvuelovip.pasajero.passanger.myflights.confirmation.FlightTakenView;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.SingleSubscriber;


public class MyFlightsAdapter extends RecyclerView.Adapter<MyFlightsAdapter.Holder> {

    public static final String TAG = MyFlightsAdapter.class.getSimpleName();
    private List<Flight> data = Collections.emptyList();
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private Map<String, Airport> airportsMap = null;
    private MyFlightsController controller;
    private MyFlightsView view;

    public void updateData(List<Flight> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public MyFlightsAdapter(List<Flight> data, MyFlightsView view, MyFlightsController controller) {
        this.view = view;
        AppCompatActivity activity = (AppCompatActivity) view.getActivity();
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.activity = activity;
        this.data = data;
        this.airportsMap = new HashMap<>();
        this.controller = controller;

        List<Airport> airportList = null;
        Log.d(TAG, "getting airports");
        try {
            airportList = Cache.getAirports();
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        for (Airport airport : airportList)
            this.airportsMap.put(String.valueOf(airport.id), airport);
    }

    @Override
    public MyFlightsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_misvuelos, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final Flight flight = data.get(position);

        // Airplane
        holder.airplane_model.setText(flight.modelAirplane);
        holder.seats.setText(flight.seats);

        //Airports
        Airport airport = airportsMap.get(String.valueOf(flight.outAirportId));
        if (airport != null) {
            holder.iata_out.setText(airport.getIata());
            holder.iata_out_city.setText(airport.getCityName());
        }

        airport = airportsMap.get(String.valueOf(flight.enterAirportId));
        if (airport != null) {
            holder.iata_in.setText(airport.getIata());
            holder.iata_in_city.setText(airport.getCityName());
        }

        //Dates
        String outDate = flight.outDate;
        SimpleDateFormat sdf = Util.getDjangoDateFormatWithOutZ();
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfHour = new SimpleDateFormat("h:mm");
        SimpleDateFormat sdfAmPm = new SimpleDateFormat("a");

        sdfAmPm.setTimeZone(TimeZone.getDefault());
        try {
            Date outDateNative = sdf.parse(outDate);
            String day = sdfDay.format(outDateNative);
            String month = sdfMonth.format(outDateNative);
            String year = sdfYear.format(outDateNative);
            String hour = sdfHour.format(outDateNative);
            String amPm = sdfAmPm.format(outDateNative);

            holder.date.setText(activity.getString(R.string.date) + ": " + day + "/" + month + "/" + year);
            holder.outHour.setText(hour + " " + amPm);
        } catch (ParseException e) {
            Log.e(TAG, "onBindViewHolder:ParseException:" + e.getLocalizedMessage());
        }
        holder.flightPrice.setText("$ " + String.valueOf(flight.price));

        //STATE
        holder.flightState.setText(flight.disponibility);
        holder.paymentMethod.setText(flight.payment.paymentMethod);

        holder.cancelAction.setVisibility(View.GONE);
        holder.takenAction.setVisibility(View.GONE);

        if(flight.disponibilityCode == null){
            Log.e(TAG, "No 'disponibilityCode available for Flight: " +  flight.id);
            return;
        }

        switch (flight.disponibilityCode) {
            case Util.FLIGHT_FLIGHT_STATE: {
                if(flight.disponibilityPassenger.equals(Util.FLIGHT_PASSANGER_NOT_YET_QUALIFIED)) {
                    holder.takenAction.setVisibility(View.VISIBLE);
                    holder.takenAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            takenOrNoTaken(flight);
                        }
                    });
                }

                break;
            }
            case Util.FLIGHT_PAY_STATE: {
                holder.cancelAction.setVisibility(View.VISIBLE);
                holder.cancelAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelFlight(flight);
                    }
                });
                break;
            }
        }


//        if (flight.disponibility.equals(Util.FLIGHT_CANCEL_STATE))
//            holder.cancelAction.setVisibility(View.GONE);
//
//        if (flight.disponibility.equals(Util.FLIGHT_EXPIRED_STATE))
//            holder.cancelAction.setVisibility(View.GONE);
//
        /*
        holder.flightId = flight.getId();
        holder.iata_out_city.setText(flight.getOutAirportId());
        holder.iata_in_city.setText(flight.getEnterAirportId());
        holder.iata_out.setText(flight.getOutIata());
        holder.iata_in.setText(flight.getEnterIata());

        holder.airplane_model.setText(flight.getModelAirplane());
        holder.seats.setText(flight.getSeats());
        holder.flight_price.setText("$" + flight.getPrice());


        String imageUrl = flight.imageUrl;

        if(imageUrl != null && !imageUrl.isEmpty()){
//            Picasso.with(activity).load(flight.imageUrl).into(holder.airplane_photo);
        }
        */
    }

    private void takenOrNoTaken(final Flight flight) {
        final Bundle bundle = new Bundle();
        bundle.putLong("flightId", Long.valueOf(flight.id));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.flight_taken_question))
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FlightTakenView fragment = new FlightTakenView();
                        fragment.onCreate(bundle);
                        Util.addRollbackableTransaction(R.id.main_frame, activity.getSupportFragmentManager(), fragment);
                    }
                })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FlightNotTakenView fragment = new FlightNotTakenView();
                        fragment.onCreate(bundle);
                        Util.addRollbackableTransaction(R.id.main_frame, activity.getSupportFragmentManager(), fragment);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void cancelFlight(final Flight flight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.cancel_flight_confirmation))
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        controller.cancelFlight(flight.id).subscribe(new SingleSubscriber<Void>() {
                            @Override
                            public void onSuccess(Void value) {
                                MyFlightsAdapter.this.view.getData();
                            }

                            @Override
                            public void onError(Throwable error) {
                                Toast.makeText(activity.getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        //AIRPLANE
        TextView airplane_model;
        //IATA
        TextView iata_out, iata_in, iata_out_city, iata_in_city;
        //
        TextView seats, flightPrice, outHour, date, paymentMethod;
        //
        TextView flightState;
        //
        TextView cancelAction, takenAction;


        public Holder(View itemView) {
            super(itemView);

            //AIRPLANE
            airplane_model = (TextView) itemView.findViewById(R.id.airplane_model);
            seats = (TextView) itemView.findViewById(R.id.journey_seats);
            //IATA
            iata_out = (TextView) itemView.findViewById(R.id.iata_out);
            iata_in = (TextView) itemView.findViewById(R.id.iata_in);
            iata_out_city = (TextView) itemView.findViewById(R.id.iata_out_city);
            iata_in_city = (TextView) itemView.findViewById(R.id.iata_in_city);
            //Dates
            date = (TextView) itemView.findViewById(R.id.date_flight);
            outHour = (TextView) itemView.findViewById(R.id.hora_salida);
            //FLIGHT
            flightPrice = (TextView) itemView.findViewById(R.id.flight_price);
            //
            flightState = (TextView) itemView.findViewById(R.id.disponibility);
            paymentMethod = (TextView) itemView.findViewById(R.id.metodo_pago);

            cancelAction = (TextView) itemView.findViewById(R.id.cancel_action);
            takenAction = (TextView) itemView.findViewById(R.id.taken_action);

        }

    }

}

