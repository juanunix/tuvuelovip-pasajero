package fourgeeks.tuvuelovip.pasajero.passanger.flight;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.JAR;
import fourgeeks.tuvuelovip.pasajero.api.JSONApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;


/**
 * Created by alacret on 12/9/16.
 */

public final class FlightModel {
    private FlightModel(){}

    @Deprecated
    public static void getFlights(final ApiResponseListener<List<Flight>> listener) {
        Log.d("FlightModel", "getFlights");

        JSONApiResponseListener<JSONArray> jsonListener = new JSONApiResponseListener<JSONArray>(listener) {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("FlightModel", "getFlights:onResponse:" + response);
                List<Flight> data = null;
                try {
                    data = Flight.fromJsonArray(response);
                    listener.onResponse(data);
                } catch (JSONException e) {
                    Log.e("FlightModel", "getFlights:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                }
            }
        };

        JAR request = new JAR(URL.FLIGHTS, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    @Deprecated
    public static void getMyFlights(final ApiResponseListener<List<Flight>> listener){
        Log.d("FlightModel", "getFlightsFromServer");

        JSONApiResponseListener<JSONArray> jsonApiResponseListener = new JSONApiResponseListener<JSONArray>(listener) {
            @Override
            public void onResponse(JSONArray response) {
                List<Flight> data;
                try {
                    data = Flight.fromJsonArrayCOMPRADO(response);
                    listener.onResponse(data);
                } catch (JSONException e) {
                    Log.e("FlightModel", "getData:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                }
            }
        };

        JAR request = new JAR(URL.MY_BUY_FLIGHT, jsonApiResponseListener);
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        APP.getInstance().addToRequestQueue(request, "API");
    }
}
