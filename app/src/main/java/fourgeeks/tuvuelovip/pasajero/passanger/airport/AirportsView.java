package fourgeeks.tuvuelovip.pasajero.passanger.airport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.pojo.NotificationInfo;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.SingleSubscriber;

public class AirportsView extends Fragment {

    private static final String TAG = "AirportsView";

    private View view;
    private AutoCompleteTextView aero_1;
    private AutoCompleteTextView aero_2;
    private AutoCompleteTextView aero_3;
    private AutoCompleteTextView aero_4;
    private AutoCompleteTextView aero_5;
    private Button guardar;
    private View borrar1;
    private View borrar2;
    private View borrar3;
    private View borrar4;
    private View borrar5;
    private ProgressBar progressBar;
    //CARGADO MAIN
    private List<Airport> airports = new ArrayList<>();
    private List<Airport> myAirports = new ArrayList<>();
    private List<String> airportNames = new ArrayList<>();
    private Map<String, Airport> airportsMapByName = new HashMap<>();
    private Map<String, Airport> airportsMapById = new HashMap<>();

    private Airport airportSelected1;
    private Airport airportSelected2;
    private Airport airportSelected3;
    private Airport airportSelected4;
    private Airport airportSelected5;
    //NOTIFICATION
    private AirportController controller;
    private Switch notificationSwitch;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.view_aeropuertos, container, false);
        aero_1 = (AutoCompleteTextView) view.findViewById(R.id.aero_1);
        aero_2 = (AutoCompleteTextView) view.findViewById(R.id.aero_2);
        aero_3 = (AutoCompleteTextView) view.findViewById(R.id.aero_3);
        aero_4 = (AutoCompleteTextView) view.findViewById(R.id.aero_4);
        aero_5 = (AutoCompleteTextView) view.findViewById(R.id.aero_5);
        guardar = (Button) view.findViewById(R.id.guardar);
        borrar1 = view.findViewById(R.id.borrar1);
        borrar2 = view.findViewById(R.id.borrar2);
        borrar3 = view.findViewById(R.id.borrar3);
        borrar4 = view.findViewById(R.id.borrar4);
        borrar5 = view.findViewById(R.id.borrar5);
        progressBar = (ProgressBar) view.findViewById(R.id.airport_progress);
        notificationSwitch = (Switch) view.findViewById(R.id.notifications);

        controller = new AirportController(new AirportService());

        getNotificationsInfo();
        getAirports();
        setEraseButton();
        setText();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.setNotificationInfo(isChecked).subscribe();
            }
        });
        return view;
    }

    private void getAirports() {
        progressBar.setVisibility(View.VISIBLE);
        AirportModel.getAirportModel(new ApiResponseListener<List<Airport>>() {
            @Override
            public void onResponse(List<Airport> result) {

                myAirports = result;
                if (!myAirports.isEmpty()) {
                    List<TextView> textViews = new ArrayList<TextView>();
                    textViews.add(aero_1);
                    textViews.add(aero_2);
                    textViews.add(aero_3);
                    textViews.add(aero_4);
                    textViews.add(aero_5);
                    for (int i = 0; i < myAirports.size(); i++) {
                        textViews.get(i).setText(myAirports.get(i).toString());
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(VolleyError error, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setText() {
        try {
            airports = Cache.getAirports();
            for (Airport airport : airports) {
                airportsMapByName.put(airport.toString(), airport);
                airportsMapById.put(String.valueOf(airport.id), airport);
                airportNames.add(airport.toString());
            }


            if (!airports.isEmpty()) {
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.airport_list_item, airportNames);

                aero_1.setAdapter(adapter);
                aero_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        airportSelected1 = searchAirportId(adapter.getItem(i));
                    }
                });

                aero_2.setAdapter(adapter);
                aero_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        airportSelected2 = searchAirportId(adapter.getItem(i));
                    }
                });

                aero_3.setAdapter(adapter);
                aero_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        airportSelected3 = searchAirportId(adapter.getItem(i));
                    }
                });

                aero_4.setAdapter(adapter);
                aero_4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        airportSelected4 = searchAirportId(adapter.getItem(i));
                    }
                });

                aero_5.setAdapter(adapter);
                aero_5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        airportSelected5 = searchAirportId(adapter.getItem(i));
                    }
                });

            }
        } catch (IOException e) {

        }
    }

    private Airport searchAirportId(String airport) {
        return airportsMapByName.get(airport);
    }

    private void setEraseButton() {
        borrar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aero_1.getText().toString().isEmpty()) {
                    erase(aero_1, searchAirportId(aero_1.getText().toString()));
                }
            }
        });

        borrar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aero_2.getText().toString().isEmpty()) {
                    erase(aero_2, searchAirportId(aero_2.getText().toString()));
                }
            }
        });

        borrar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aero_3.getText().toString().isEmpty()) {
                    erase(aero_3, searchAirportId(aero_3.getText().toString()));
                }
            }
        });

        borrar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aero_4.getText().toString().isEmpty()) {
                    erase(aero_4, searchAirportId(aero_4.getText().toString()));
                }
            }
        });

        borrar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aero_5.getText().toString().isEmpty()) {
                    erase(aero_5, searchAirportId(aero_5.getText().toString()));
                }
            }
        });

    }

    private void erase(TextView text, Airport airport) {
        text.setText("");

        AirportModel.deleteAirport(airport, new ApiResponseListener<Airport>() {
            @Override
            public void onResponse(Airport result) {

            }

            @Override
            public void onError(VolleyError error, String message) {

            }
        });

    }

    private void save() {
        if (!aero_1.getText().toString().isEmpty()) {
            if (airportSelected1 == null) {
                airportSelected1 = searchAirportId(aero_1.getText().toString());
            }
            sendAirport(airportSelected1);
        }

        if (!aero_2.getText().toString().isEmpty()) {
            if (airportSelected2 == null) {
                airportSelected2 = searchAirportId(aero_2.getText().toString());
            }
            sendAirport(airportSelected2);
        }

        if (!aero_3.getText().toString().isEmpty()) {
            if (airportSelected3 == null) {
                airportSelected3 = searchAirportId(aero_3.getText().toString());
            }
            sendAirport(airportSelected3);
        }

        if (!aero_4.getText().toString().isEmpty()) {
            if (airportSelected4 == null) {
                airportSelected4 = searchAirportId(aero_4.getText().toString());
            }
            sendAirport(airportSelected4);
        }

        if (!aero_5.getText().toString().isEmpty()) {
            if (airportSelected5 == null) {
                airportSelected5 = searchAirportId(aero_5.getText().toString());
            }
            sendAirport(airportSelected5);
        }

    }

    private void sendAirport(Airport airport) {
        progressBar.setVisibility(View.VISIBLE);
        AirportModel.saveAirport(airport, new ApiResponseListener<Airport>() {
            @Override
            public void onResponse(Airport result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Guardado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VolleyError error, String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getNotificationsInfo() {
        progressBar.setVisibility(View.VISIBLE);
        controller.getNotificationsInfo().subscribe(new SingleSubscriber<NotificationInfo>() {
            @Override
            public void onSuccess(NotificationInfo value) {
                notificationSwitch.setEnabled(true);
                notificationSwitch.setChecked(value.push);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                String msg = Util.msgFromRetrofitThrowable(AirportsView.this, e);
                Log.e(TAG, "getNotificationsInfo:onError:" + msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
