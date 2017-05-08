package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Journey;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Util;


public class JourneyView extends Fragment implements JourneyEmitter {

    private CalendarView calenderView;
    private Date lastDate;
    private int maxSeats;

    private TextView saveJourneyButton;
    private AutoCompleteTextView airportIn, airportOut;
    private TextInputEditText seatsNumberTextEdit, exitHourTextInput;
    private Map<String, Airport> airportsMapByName;

    private List<JourneyListener> listeners = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.view_new_journey, container, false);

        calenderView = (CalendarView) view.findViewById(R.id.calendar);
        airportIn = (AutoCompleteTextView) view.findViewById(R.id.in_airport);
        airportOut = (AutoCompleteTextView) view.findViewById(R.id.out_airport);
        seatsNumberTextEdit = (TextInputEditText) view.findViewById(R.id.seats_number);

        exitHourTextInput = (TextInputEditText) view.findViewById(R.id.exit_hour);
        saveJourneyButton = (TextView) view.findViewById(R.id.button_save);
        TextView cancelButton = (TextView) view.findViewById(R.id.button_edit);
//        //SETTING THE CURRENT DATE
        calenderView.setDate(new Date().getTime());
//        dia_seleccionado = new Date(calendar.getTime().getTime());
//
//        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
//                calendar.set(i,  i1, i2, 0, 0);
//                dia_seleccionado = new Date(calendar.getTime().getTime());
//            }
//        });
//
        Bundle arguments = getArguments();
        airportsMapByName = (HashMap<String, Airport>) arguments.getSerializable("airportsMapByName");
        String[] airportsNames = arguments.getStringArray("airportsNames");
        int seats = arguments.getInt("seats");
        if (seats != 0)
            seatsNumberTextEdit.setText(String.valueOf(seats));

        int localMaxSeats = 0;
        try {
            localMaxSeats = Cache.getMaxSeats();
        } catch (IOException e) {
            Log.e("JOURNEYVIEW","onCreateView:getMaxSeats:" + e.getLocalizedMessage());
        }finally {
            if(localMaxSeats != 0)
                maxSeats = localMaxSeats;
        }
        if (seats != 0)
            seatsNumberTextEdit.setText(String.valueOf(seats));
        lastDate = (Date) arguments.getSerializable("lastDate");
        if (lastDate != null)
            calenderView.setDate(lastDate.getTime());

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.airport_list_item, airportsNames);
        airportIn.setAdapter(adapter);
        airportOut.setAdapter(adapter);

        saveJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveJourney();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(saveJourneyButton.getWindowToken(), 0);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void saveJourney() {
        //First: The Request
        Journey journey = new Journey();
        //HACK por que si nunca selecciona una fecha debe ser la de hoy
//        if(calenderView.getDate() == null)
//            request.setOutDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);// Ayer
        Date outDate = new Date(calenderView.getDate());
        if (!(outDate.after(calendar.getTime()))) {
            Toast.makeText(getActivity().getApplicationContext(), "La fecha tiene que ser superior al dia de ayer",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (lastDate != null) {
            if (outDate.before(lastDate)) {
                Toast.makeText(getActivity().getApplicationContext(), "La fecha no puede ser menor a la fecha de la ruta anterior",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        String outAirportName = airportOut.getText().toString();
        if (outAirportName.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Seleccione un Aeropuerto de Salida",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Airport outAirport = airportsMapByName.get(outAirportName);
        if (outAirport == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Seleccione un Aeropuerto de Salida válido",
                    Toast.LENGTH_LONG).show();
            return;
        }
        journey.outAirportId = outAirport.id;

        String inAirportName = airportIn.getText().toString();
        if (inAirportName.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Seleccione un Aeropuerto de Entrada",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Airport inAirport = airportsMapByName.get(inAirportName);
        if (inAirport == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Seleccione un Aeropuerto de Entrada válido",
                    Toast.LENGTH_LONG).show();
            return;
        }
        journey.inAirportId = inAirport.id;

        if (journey.outAirportId == journey.inAirportId ){
            Toast.makeText(getActivity().getApplicationContext(), "El Aeropuerto de Entrada no puede " +
                    "ser igual al Aeropuerto de Salida",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String seatsText = seatsNumberTextEdit.getText().toString();
        if (seatsText.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Indique el número de asientos",
                    Toast.LENGTH_LONG).show();
            return;
        }

        int seatsNumber;
        try {
            seatsNumber = Integer.valueOf(seatsText);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Indique el número de asientos",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(seatsNumber > maxSeats){
            Toast.makeText(getActivity().getApplicationContext(), String.format("Actualmente en el " +
                    "sistema, no existen aviones con (%d) asientos (Máximo: %d)", seatsNumber, maxSeats),
                    Toast.LENGTH_LONG).show();
            return;
        }
        journey.seats = seatsNumber;

        String exitHourText = exitHourTextInput.getText().toString();
        if (exitHourText.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Indique la hora de salida",
                    Toast.LENGTH_LONG).show();
            return;
        }

        int exitHour = -1;

        try{
            exitHour = Integer.valueOf(exitHourText);
        }catch(NumberFormatException e){
            Toast.makeText(getActivity().getApplicationContext(), "Indique un hora de salida válida (24hrs)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (exitHour > 23 || exitHour < 0) {
            Toast.makeText(getActivity().getApplicationContext(), "Indique un hora de salida válida (24hrs)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat sdfHour = new SimpleDateFormat("k");
        try {
            sdfHour.parse(exitHourText);
        } catch (ParseException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Por favor ingrese la hora de salida en formato de 24 horas",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (Util.isToday(outDate)) { // El vuelo es hoy
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String hourProposed = sdf2.format(new Date()) + " " + exitHourText + ":00";
            Date hourProposedAsDate = null;
            try {
                hourProposedAsDate = sdf.parse(hourProposed);
            } catch (ParseException e) {
                Log.e("JOURNEYVIEW","saveJourney:ParseException:" + e.getLocalizedMessage());
            }
            calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 4);
            Date todayAfterFourHours = new Date(calendar.getTime().getTime());

            if (!hourProposedAsDate.after(todayAfterFourHours)) {
                Toast.makeText(getActivity().getApplicationContext(), "Para vuelos el dia de hoy, tiene que haber al menos 4 horas de anticipación",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        calendar.setTime(outDate);
        Calendar finalCalendar = Calendar.getInstance();
        finalCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), Integer.valueOf(exitHourText), 0);
        journey.outDate = finalCalendar.getTime();

        for (JourneyListener listener : listeners)
            listener.onJourney(journey);

        getFragmentManager().popBackStack();

    }

    @Override
    public void addJourneyListener(JourneyListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeJourneyListener(JourneyListener listener) {
        listeners.remove(listener);
    }
}
