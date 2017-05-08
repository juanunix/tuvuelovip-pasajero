package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.StateManager;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import fourgeeks.tuvuelovip.pasajero.pojo.Journey;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey.JourneyEmitter;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey.JourneyListener;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey.JourneyView;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey.JourneysAdapter;
import rx.SingleSubscriber;


public class FlightRequestView extends Fragment implements JourneyListener, Serializable {

    private static final String TAG = FlightRequestView.class.getSimpleName();

    private View view;
    private Map<String, Airport> airportsMapByName = new HashMap<>();
    private Map<String, Airport> airportsMapById = new HashMap<>();
    private String[] airportsNames;

    private List<Journey> journeysList = new ArrayList<>();
    private int i;
    private ListView listView;
    private int seats;
    private ProgressBar progressBar;
    private FlightRequest flightRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_new_flight_request, container, false);
        listView = (ListView) view.findViewById(R.id.journeys_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        populateList();
        populateAirports();

        Button buttonNewJourney = (Button) view.findViewById(R.id.button_edit);
        buttonNewJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewJourneyForm();
            }
        });

        Button buttonSaveFlightRequest = (Button) view.findViewById(R.id.button_save);
        buttonSaveFlightRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrUpdate();
            }
        });

        Button buttonDelete = (Button) view.findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // If editing and has responses, you can't edit the Flight Request
        if (isNotEditable()) {
            buttonNewJourney.setVisibility(View.GONE);
            buttonSaveFlightRequest.setVisibility(View.GONE);
        }

        // if no editing show the form
        if (!isEditing())
            showNewJourneyForm();
        else { // if Editting
            buttonDelete.setVisibility(View.VISIBLE);
        }
        setHasOptionsMenu(true);
        return view;
    }

    private void saveOrUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Por favor, confirme para salvar la solicitud de vuelo!")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isEditing())
                            updateFlightRequest();
                        else
                            saveFlightRequest();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Por favor, confirme para eliminar la solicitud de vuelo!")
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressBar.setVisibility(View.VISIBLE);
                        FlightRequestModel.deleteFlightRequest(flightRequest, new ApiResponseListener<FlightRequest>() {
                            @Override
                            public void onResponse(FlightRequest result) {
                                reloadFlightRequests(dialog);
                            }

                            @Override
                            public void onError(VolleyError error, String message) {
                                Log.e("FlightRequestView", "delete:deleteFlightRequest:onError:" + message);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                reloadFlightRequests(dialog);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void reloadFlightRequests(final DialogInterface dialog) {
        new FlightRequestsService().getMyFlightRequests().subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void v) {
                Log.e(TAG, "getData:onSuccess");
                clean();
            }

            @Override
            public void onError(Throwable e) {
                String msg = Util.msgFromRetrofitThrowable(FlightRequestView.this, e);
                Log.e(TAG, "getData:onError:" + msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                clean();

            }

            private void clean() {
                dialog.dismiss();
                getFragmentManager().popBackStack();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean isNotEditable() {
        return isEditing() && flightRequest.num_responses > 0;
    }

    private boolean isEditing() {
        return flightRequest != null;
    }

    private void populateAirports() {
        Log.d("FlightRequestView", "populateAirports");
        List<Airport> airports;
        try {
            airports = Cache.getAirports();
        } catch (IOException e) {
            Log.e("FlightRequestView", "populateAirports:IOException:" + e.getLocalizedMessage());
            return;
        }

        airportsNames = new String[airports.size()];
        int i = 0;

        for (Airport airport : airports) {
            airportsMapByName.put(airport.toString(), airport);
            airportsMapById.put(String.valueOf(airport.id), airport);
            airportsNames[i++] = airport.toString();
        }
    }

    private void populateList() {
        Journey[] journeys = new Journey[journeysList.size()];
        int i = 0;
        for (Journey journey : journeysList) {
            Airport airport = airportsMapById.get(String.valueOf(journey.outAirportId));
            journey.outAirportIata = airport.iata;
            journey.outAirportCityName = airport.cityName;
            airport = airportsMapById.get(String.valueOf(journey.inAirportId));
            journey.inAirportIata = airport.iata;
            journey.inAirportCityName = airport.cityName;
            journeys[i++] = journey;
        }
        JourneysAdapter journeysAdapter = new JourneysAdapter(getActivity(), journeys, seats, this);

        listView.setAdapter(journeysAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            flightRequest = (FlightRequest) arguments.getSerializable("flightRequest");
            List<Journey> journeys = new ArrayList<>();
            journeys.add(flightRequest.toJourney());
            if (flightRequest.journeys != null)
                journeys.addAll(flightRequest.journeys);
            journeysList = journeys;
            seats = flightRequest.getSeats();
            populateAirports();
        }
    }

    private void saveFlightRequest() {
        if (journeysList.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "No hay rutas que guardar", Toast.LENGTH_LONG);
            return;
        }
        Journey journey = journeysList.get(0);
        FlightRequest localFlightRequest = new FlightRequest();
        localFlightRequest.setOutDate(journey.outDate);
        localFlightRequest.setEnterAirport_id(journey.inAirportId);
        localFlightRequest.setOutAirport_id(journey.outAirportId);
        localFlightRequest.setSeats(seats);

        if (journeysList.size() > 1)
            for (int i = 1, j = journeysList.size(); i < j; i++)
                localFlightRequest.addJourney(journeysList.get(i));

        progressBar.setVisibility(View.VISIBLE);
        FlightRequestModel.saveFlightRequest(localFlightRequest, new ApiResponseListener<FlightRequest>() {
            @Override
            public void onResponse(FlightRequest result) {
                progressBar.setVisibility(View.GONE);
                List<FlightRequest> value = StateManager.instance().getFlightRequestsSubject().getValue();
                value.add(result);
                StateManager.instance().getFlightRequestsSubject().onNext(value);
                getFragmentManager().popBackStack();
            }

            @Override
            public void onError(VolleyError error, String message) {
                Log.e("FlightRequestView", "saveFlightRequest:onError" + error.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
            }
        });
    }

    private void updateFlightRequest() {
        if (journeysList.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.no_journeys), Toast.LENGTH_LONG);
            return;
        }
        Journey journey = journeysList.get(0);
        FlightRequest localFlightRequest = new FlightRequest();
        localFlightRequest.id = flightRequest.id;
        localFlightRequest.setOutDate(journey.outDate);
        localFlightRequest.setEnterAirport_id(journey.inAirportId);
        localFlightRequest.setOutAirport_id(journey.outAirportId);
        localFlightRequest.setSeats(seats);

        if (journeysList.size() > 1)
            for (int i = 1, j = journeysList.size(); i < j; i++)
                localFlightRequest.addJourney(journeysList.get(i));

        progressBar.setVisibility(View.VISIBLE);
        FlightRequestModel.updateFlightRequest(localFlightRequest, new ApiResponseListener<FlightRequest>() {
            @Override
            public void onResponse(FlightRequest result) {
                progressBar.setVisibility(View.GONE);
                getFragmentManager().popBackStack();
            }

            @Override
            public void onError(VolleyError error, String message) {
                Log.e("FlightRequestView", "saveFlightRequest:onError" + error.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
            }
        });
    }

    private void showNewJourneyForm() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragmentByTag = new JourneyView();

        ((JourneyEmitter) fragmentByTag).addJourneyListener(this);

        Bundle bundle = new Bundle();
        bundle.putStringArray("airportsNames", airportsNames);
        bundle.putSerializable("airportsMapByName", (Serializable) airportsMapByName);
        bundle.putInt("seats", seats);
        //Por si acaso ya hay un numero de asientos establecido y fecha de vuelos previos
        if (journeysList.size() > 0) {
            Journey lastJourney = journeysList.get(journeysList.size() - 1);
            bundle.putSerializable("lastDate", lastJourney.outDate);
        }
        fragmentByTag.setArguments(bundle);

        transaction.addToBackStack(JourneyView.class.getSimpleName());
        transaction.add(R.id.main_frame, fragmentByTag, JourneyView.class.getSimpleName());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onJourney(Journey journey) {
        seats = journey.seats;
        journeysList.add(journey);
        populateList();
    }

    public void deleteJourney(final int positionInAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Por favor, confirme borrar la ruta!")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        journeysList.remove(positionInAdapter);
                        populateList();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
