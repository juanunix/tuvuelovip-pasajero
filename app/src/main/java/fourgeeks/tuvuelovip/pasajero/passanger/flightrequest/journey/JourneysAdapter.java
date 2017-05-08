package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fourgeeks.tuvuelovip.pasajero.pojo.Journey;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.FlightRequestView;

/**
 * Created by alacret on 1/28/17.
 */

public class JourneysAdapter extends ArrayAdapter<Journey> {
    private final Context context;
    private final Journey[] values;
    private final FlightRequestView flightRequestView;
    private int seats;
    private TextView iataOutTextView, iataInTextView, cityNameOutTextView,
            cityNameInTextView, dateTextView, seatsTextView, hourTextView;
    private ImageView buttonDelete;

    public JourneysAdapter(Context context, Journey[] values, int seats, FlightRequestView flightRequestView) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.seats = seats;
        this.flightRequestView = flightRequestView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_journey, parent, false);
        iataOutTextView = (TextView) view.findViewById(R.id.iata_out);
        cityNameOutTextView = (TextView) view.findViewById(R.id.iata_out_city);
        iataInTextView = (TextView) view.findViewById(R.id.iata_in);
        cityNameInTextView = (TextView) view.findViewById(R.id.iata_in_city);
        seatsTextView = (TextView) view.findViewById(R.id.journey_seats);
        dateTextView = (TextView) view.findViewById(R.id.date_flight);
        hourTextView = (TextView) view.findViewById(R.id.hora_salida);
        buttonDelete = (ImageView) view.findViewById(R.id.button_delete_journey);

        Journey journey = this.values[position];
        iataOutTextView.setText(journey.outAirportIata);
        cityNameOutTextView.setText(journey.outAirportCityName);
        iataInTextView.setText(journey.inAirportIata);
        cityNameInTextView.setText(journey.inAirportCityName);
        seatsTextView.setText(String.valueOf(seats));

        Date outDate = journey.outDate;
        Log.d("JourneysAdapter","getView:" + outDate);
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yy");
        SimpleDateFormat sdfHour = new SimpleDateFormat("k");

        String day = sdfDay.format(outDate);
        String month = sdfMonth.format(outDate);
        String hour = sdfHour.format(outDate);
        String year = sdfYear.format(outDate);
        Log.d("JourneysAdapter","getView:" + day);
        Log.d("JourneysAdapter","getView:" + month);
        Log.d("JourneysAdapter","getView:" + year);
        Log.d("JourneysAdapter","getView:" + hour);
        dateTextView.setText(day + "/" + month + "/" + year);
        hourTextView.setText(hour + ":00 ");

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightRequestView.deleteJourney(position);
            }
        });

        return view;
    }




}
