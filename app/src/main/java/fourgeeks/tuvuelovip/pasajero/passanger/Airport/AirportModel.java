package fourgeeks.tuvuelovip.pasajero.passanger.airport;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.JAR;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.JSONApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;

/**
 * Created by antonio on 29/01/17.
 */
@Deprecated
public final class AirportModel {

    private AirportModel() {
    }

    public static void getAirportModel(final ApiResponseListener<List<Airport>> listener) {

        JSONApiResponseListener<JSONArray> jsonListener = new JSONApiResponseListener<JSONArray>(listener) {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ProfileModel", "getProfile:onResponse:" + response);
                List<Airport> airports = new ArrayList<>();

                try {
                    airports = Airport.fromJsonArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                listener.onResponse(airports);
            }
        };

        JAR request = new JAR(URL.MY_FREQUENT_AIRPORT, jsonListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void saveAirport(Airport airport, final ApiResponseListener<Airport> listener) {
        Log.d("ProfileModel", "saveProfile");
        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ProfileModel", "saveProfile:onResponse:" + response);
                Airport airport = new Airport();
                try {
                    airport = Airport.fromJsonObject(response);
                } catch (JSONException e) {
                    Log.e("ProfileModel", "saveProfile:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                    return;
                }
                listener.onResponse(airport);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };
        JSONObject airportAsJson = new JSONObject();
        try {
            airportAsJson.put("id", airport.getId());
        } catch (JSONException e) {
            Log.e("ProfileModel", "saveProfile:JSONException:" + e.getLocalizedMessage());
            listener.onError(null, e.getLocalizedMessage());
            return;
        }
        Log.d("ProfileModel", "saveProfile:JSON:" + airportAsJson);

        JOR request = new JOR(URL.FREQUENT_AIRPORT.getURL(),
                Request.Method.POST, airportAsJson, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void deleteAirport(Airport airport, final ApiResponseListener<Airport> listener) {

        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Airport airport = new Airport();
//                try {
//                    airport = Airport.fromJsonObject2(response);
//                } catch (JSONException e) {
//                    Log.e("ProfileModel", "saveProfile:onResponse:JSONException:" + e.getLocalizedMessage());
//                    listener.onError(null,e.getLocalizedMessage());
//                    return;
//                }
                listener.onResponse(airport);
            }
        };
        JSONObject airportAsJson = new JSONObject();

        try {
            airportAsJson.put("id", airport.getId());
        } catch (JSONException e) {
            Log.e("ProfileModel", "saveProfile:JSONException:" + e.getLocalizedMessage());
            listener.onError(null, e.getLocalizedMessage());
            return;
        }

        JOR request = new JOR(URL.ERASE_MY_FREQUENT_AIRPORT.getURL() + airport.getId() + "/",
                URL.ERASE_MY_FREQUENT_AIRPORT.getRequestType(),
                jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

}
