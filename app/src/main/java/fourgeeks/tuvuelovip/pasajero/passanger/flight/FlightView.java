package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey.JourneysInFlightViewAdapter;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Comfort;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.Journey;
import fourgeeks.tuvuelovip.pasajero.util.ImageAdapter;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.ImageSliderPagerAdapter;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.SingleSubscriber;


public class FlightView extends Fragment {

    private View view;
    private static PayPalConfiguration config;
    private Button buyFlightButton;
//    private ImageView image;
    private String flightId = null;
    private TextView dateTextView, hourTextView, outIataTextView, inIataTextView,
            outCityName, inCityName, modeloTextView, asientosTextView,
            fabYearTextView, claseTextView, obsTextView, termsTextView,
            ageTextView, licenseTextView, certfTextView, flightHrsTextView;
    private GridView comfortsGridView;
    private ProgressBar progressBar;
    //TextView campos del piloto
    private Flight flight;
    private Map<String, Airport> airportsMap = new HashMap<>();
    private ListView journesysInFlightList;
    public static final String TAG = FlightView.class.getSimpleName();
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            String localFlightId = (String) savedInstanceState.getSerializable("flightId");
            if (localFlightId != null)
                flightId = localFlightId;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null; // now cleaning up!
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nelson_detail_view, container, false);
        buyFlightButton = (Button) view.findViewById(R.id.comprar_vuelo_button);
//        image = (ImageView) view.findViewById(R.id.flight_detail_image);
        dateTextView = (TextView) view.findViewById(R.id.flight_details_date);
        hourTextView = (TextView) view.findViewById(R.id.flight_details_hour);
        journesysInFlightList = (ListView) view.findViewById(R.id.journeys_list_flight_view);
//
        outIataTextView = (TextView) view.findViewById(R.id.codigo_aeropuerto_salida);
        inIataTextView = (TextView) view.findViewById(R.id.codigo_aeropuerto_arrivo);
        outCityName = (TextView) view.findViewById(R.id.nombre_ciudad_salida);
        inCityName = (TextView) view.findViewById(R.id.nombre_ciudad_arrivo);
        modeloTextView = (TextView) view.findViewById(R.id.modelo_content);
        asientosTextView = (TextView) view.findViewById(R.id.asientos_content);
        fabYearTextView = (TextView) view.findViewById(R.id.fabrication_year_content);
        claseTextView = (TextView) view.findViewById(R.id.clase_content);
        obsTextView = (TextView) view.findViewById(R.id.observaciones_content);
        termsTextView = (TextView) view.findViewById(R.id.terms);
        comfortsGridView = (GridView) view.findViewById(R.id.comforts_grid);

        //findview TV campos del piloto
        ageTextView = (TextView) view.findViewById(R.id.age_content);
        licenseTextView = (TextView) view.findViewById(R.id.license_content);
        certfTextView = (TextView) view.findViewById(R.id.medic_certificate_content);
        flightHrsTextView = (TextView) view.findViewById(R.id.flight_hours_content);
        //
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        //Slider
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_slider);

        List<Airport> airportList = null;
        try {
            airportList = Cache.getAirports();
        } catch (IOException e) {
            Log.e("FLIGHTVIEW", e.getLocalizedMessage());
        }

        for (Airport airport : airportList)
            this.airportsMap.put(String.valueOf(airport.id), airport);

        getFlightDetails();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        //PAYPAL INIT
        config = new PayPalConfiguration();
//        config.environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
        config.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
//        config.environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
        config.clientId("AVXHu52w-5rcLKi5kKzyGcCSBu_P_wlIrgnDDJBW2IkqVUOJ_pp7AwxjNYa0MDIFixxLtYDZKhvHyRdl");
//        config.clientId("AYW1s6eRRn7m3XyygIdZ8n89Yq-pM6-9_ErBPX0x1N57jJV_kX506nIpwzfc4TL1gE7TK3Jyz7xuEgql");

        Intent service = new Intent(getActivity(), PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(service);

        buyFlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Cache.termsAndConditionsWereAccepted())
                    shop();
                else
                    Toast.makeText(getActivity().getApplication(), getString(R.string.must_accept_terms), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void shop() {
        progressBar.setVisibility(View.VISIBLE);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("FlightView", "shop:onResponse:" + response);
                progressBar.setVisibility(View.GONE);
                try {
                    String price = flight.getPrice();
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(price), "USD", "Tu Vuelo VIP", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    // send the same configuration for restart resiliency
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    Log.e("FlightView", "shop:onResponse:JSONException:" + e.getLocalizedMessage());
                    return;
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                String errorMessage = getString(R.string.server_error);
                if (error instanceof TimeoutError) {
                    Log.e("FlightView", "shop:onErrorResponse:TimeoutError:" + error.getLocalizedMessage());
                    errorMessage = getString(R.string.timeout_error);
                } else if (error.networkResponse.statusCode == 400) {
                    Log.e("FlightView", "shop:onErrorResponse:" + new String(error.networkResponse.data));
                    errorMessage = new String(error.networkResponse.data).replace("\"", "").replace("'", "").replace("[", "").replace("]", "");
                }
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        };
        JOR request = new JOR(URL.BOOK_FLIGHT.getURL() + flightId + "/", Request.Method.PUT, listener, errorListener);
        APP.getInstance().addToRequestQueue(request, "API " + flightId);
    }


    private void freeFlight() {
        progressBar.setVisibility(View.VISIBLE);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("FlightView", "freeFlight:onResponse:" + response);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.payment_error), Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getString(R.string.server_error);
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError) {
                    Log.e("FlightView", "freeFlight:onErrorResponse:TimeoutError:" + error.getLocalizedMessage());
                    errorMessage = getString(R.string.timeout_error);
                } else if (error.networkResponse.statusCode == 400) {
                    Log.e("FlightView", "freeFlight:onErrorResponse:" + new String(error.networkResponse.data));
                    errorMessage = new String(error.networkResponse.data).replace("\"", "").replace("'", "").replace("[", "").replace("]", "");
                }
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        };
        JOR request = new JOR(URL.FREE_FLIGHT.getURL() + flightId + "/", Request.Method.PUT, listener, errorListener);
        APP.getInstance().addToRequestQueue(request, "API");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                JSONObject jsonObject = confirm.toJSONObject();

                String state = null;
                try {
                    state = jsonObject.getJSONObject("response").getString("state");
                } catch (JSONException e) {
                    Log.e("FlightView", "onActivityResult:JSONException:" + e.getLocalizedMessage());
                    freeFlight();
                }

                if (state.equals("approved")) {
                    savePaymentData(jsonObject);
                } else
                    freeFlight();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void savePaymentData(JSONObject jsonObject) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "savePaymentData");
        try {
            jsonObject.put("flight_id", flightId);
        } catch (JSONException e) {
            Log.e(TAG, "savePaymentData:JSONException:" + e.getLocalizedMessage());
        }
        String paypalData = jsonObject.toString();
        Log.d(TAG, "savePaymentData:" + paypalData);

        JOR request = new JOR(URL.PAYPAL_VERIFY, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "savePaymentData:onActivityResult:onResponse:" + response.toString());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Vuelo comprado con exito!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        new FlightsService().getFlights().subscribe(new SingleSubscriber<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Log.e(TAG, "getData:onSuccess");
                            }

                            @Override
                            public void onError(Throwable e) {
                                String msg = Util.msgFromRetrofitThrowable(FlightView.this, e);
                                Log.e(TAG, "savePaymentData:onError:" + msg);
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "savePaymentData:onErrorResponse:");
                        progressBar.setVisibility(View.GONE);
                        String errorMessage = getString(R.string.server_error);
                        if (error instanceof TimeoutError) {
                            Log.e(TAG, "savePaymentData:onErrorResponse:TimeoutError:" + error.getLocalizedMessage());
                            errorMessage = getString(R.string.timeout_error);
                        } else if (error.networkResponse.statusCode == 400) {
                            Log.e(TAG, "savePaymentData:onErrorResponse:" + new String(error.networkResponse.data));
                            errorMessage = new String(error.networkResponse.data).replace("\"", "").replace("'", "").replace("[", "").replace("]", "");
                        }

                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public void getFlightDetails() {
        progressBar.setVisibility(View.VISIBLE);
        JOR request = new JOR(URL.FLIGHTS.getURL() + flightId,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FlightView", "getFlightDetails:onResponse:" + response);
                        progressBar.setVisibility(View.GONE);
                        try {
                            flight = Flight.fromJsonObject(response);
                        } catch (JSONException e) {
                            Log.e("FlightView", "getFlightDetails:onResponse:JSONException:" + e.getLocalizedMessage());
                            return;
                        }
                        populateView(flight);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        String errorMessage = getString(R.string.server_error);
                        if (error instanceof TimeoutError) {
                            Log.e("FlightView", "init_passanger:onErrorResponse:TimeoutError:" + error.getLocalizedMessage());
                            errorMessage = getString(R.string.timeout_error);
                        } else if (error.networkResponse.statusCode == 400) {
                            Log.e("FlightView", "init_passanger:onErrorResponse:" + new String(error.networkResponse.data));
                            errorMessage = new String(error.networkResponse.data).replace("\"", "").replace("'", "").replace("[", "").replace("]", "");
                        }
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        APP.getInstance().addToRequestQueue(request, "API");
    }

    private void populateView(Flight flight) {
//        String imageUrl = flight.imageUrl;
//
//        if (imageUrl != null && !imageUrl.isEmpty())
//            Picasso.with(getActivity()).load(imageUrl).into(image);

        String outDate = flight.outDate;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf = Util.getDjangoDateFormatWithOutZ();
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
        SimpleDateFormat sdfHour = new SimpleDateFormat("h:mm");
        SimpleDateFormat sdfAmPm = new SimpleDateFormat("a");
        try {
            Date outDateNative = sdf.parse(outDate);
            String day = sdfDay.format(outDateNative);
            String month = sdfMonth.format(outDateNative);
            String hour = sdfHour.format(outDateNative);
            String amPm = sdfAmPm.format(outDateNative);

            dateTextView.setText(day + month);
            hourTextView.setText(hour + " " + amPm);
        } catch (ParseException e) {
            Log.e("FlightView", "getData:ParseException:" + e.getLocalizedMessage());
        }

        Airport outAirport = airportsMap.get(String.valueOf(flight.outAirportId));
        if (outAirport != null) {
            outCityName.setText(outAirport.getCityName());
            outIataTextView.setText(outAirport.getIata());
        }

        Airport inAirport = airportsMap.get(String.valueOf(flight.enterAirportId));
        if (inAirport != null) {
            inCityName.setText(inAirport.getCityName());
            inIataTextView.setText(inAirport.getIata());
        }

        modeloTextView.setText(flight.getModelAirplane());
        asientosTextView.setText(flight.getSeats());
        fabYearTextView.setText(String.valueOf(flight.year));
        claseTextView.setText(flight.classAirplane);
        if (!(flight.observations == null || flight.observations.equals("null")))
            obsTextView.setText(flight.observations);
        if (!(flight.terms == null || flight.terms.equals("null")))
            termsTextView.setText(flight.terms);
        buyFlightButton.setText("COMPRAR ( $ " + flight.price + " )");

        //Extras
        SimpleDateFormat sdfyear = new SimpleDateFormat("yyyy");
        ageTextView.setText(flight.getPilotAge());
        //
        Date licence = flight.licence;
        if(licence !=null) {
            String licenceAsString = String.format("%s/%s/%s", sdfDay.format(licence),
                    sdfMonth.format(licence), sdfyear.format(licence));
            licenseTextView.setText(licenceAsString);
        }
        //
        Date certificate = flight.certificate;
        if(certificate !=null) {
            String certificateAsString = String.format("%s/%s/%s", sdfDay.format(certificate),
                    sdfMonth.format(certificate), sdfyear.format(certificate));
            certfTextView.setText(certificateAsString);
        }
        //
        flightHrsTextView.setText(flight.getPilotFlightHours());

        Journey[] journeys = new Journey[flight.journeys.size()];
        int i = 0;
        for (Journey journey : flight.journeys) {
            Airport airport = airportsMap.get(String.valueOf(journey.outAirportId));
            journey.outAirportIata = airport.iata;
            journey.outAirportCityName = airport.cityName;
            airport = airportsMap.get(String.valueOf(journey.inAirportId));
            journey.inAirportIata = airport.iata;
            journey.inAirportCityName = airport.cityName;
            journeys[i++] = journey;
        }
        JourneysInFlightViewAdapter journeysAdapter = new JourneysInFlightViewAdapter(getActivity(), journeys);
        journesysInFlightList.setAdapter(journeysAdapter);

        //COMFORTS
        List<Comfort> comforts = flight.comforts;
        String[] comfortsUrls = new String[comforts.size()];
        String[] comfortsDesc = new String[comforts.size()];

        i = 0;
        for (Comfort confort : comforts)
            comfortsUrls[i++] = confort.image;

        i = 0;
        for (Comfort confort : comforts)
            comfortsDesc[i++] = confort.name;

        ImageAdapter comfortsAdapter = new ImageAdapter(getContext(), comfortsUrls, comfortsDesc);
        comfortsGridView.setAdapter(comfortsAdapter);

        //Slider
//        flight.images.add("http://www.0800flor.net/wp-content/uploads/2013/09/529717_315819048540146_2067951071_n.png");
//        flight.images.add("https://4geeksacademy.co/wp-content/uploads/sites/1/2015/12/logo1-01-02-1.png");
        ImageSliderPagerAdapter mCustomPagerAdapter = new ImageSliderPagerAdapter(this.getActivity(), flight.images);
        viewPager.setAdapter(mCustomPagerAdapter);

        Util.setListViewHeightBasedOnChildren(journesysInFlightList);
        Util.setGridViewHeightBasedOnChildren(comfortsGridView);

    }

}
