package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.JAR;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.JSONApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;


/**
 * Created by alacret on 12/9/16.
 */
@Deprecated
public final class FlightRequestModel {

    public static final String TAG = "FlightRequestModel";

    private FlightRequestModel() {
    }

    public static void getMyFlightRequests(final ApiResponseListener<List<FlightRequest>> listener) {
        JSONApiResponseListener<JSONArray> jsonListener = new JSONApiResponseListener<JSONArray>(listener) {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "getMyFlightRequests:onResponse" + response);
                try {
                    List<FlightRequest> data = FlightRequest.fromJsonArray(response);
                    listener.onResponse(data);
                } catch (JSONException e) {
                    Log.e(TAG, "getMyFlightRequests:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e(TAG, "getMyFlightRequests:onResponse:ParseException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                }
            }
        };

        JAR request = new JAR(URL.MY_FLIGHT_REQUEST, jsonListener);
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void saveFlightRequest(FlightRequest flightRequest, final ApiResponseListener<FlightRequest> listener) {
        JSONObject requestAsJson = flightRequest.toJSON();

        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "saveFlightRequest:onResponse:" + response.toString());
                try {
                    listener.onResponse(FlightRequest.fromJsonObject(response));
                } catch (JSONException e) {
                    Log.e(TAG, "saveFlightRequest:onResponse:JSONException:" + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e(TAG, "saveFlightRequest:onResponse:ParseException:" + e.getLocalizedMessage());
                }
            }
        };

        JOR request = new JOR(URL.SAVE_FLIGHT_REQUEST, requestAsJson, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void updateFlightRequest(FlightRequest flightRequest, final ApiResponseListener<FlightRequest> listener) {
        JSONObject requestAsJson = flightRequest.toJSON();

        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "updateFlightRequest:onResponse:" + response.toString());
                Cache.setNeedToReloadFlightRequest(true);
                try {
                    listener.onResponse(FlightRequest.fromJsonObject(response));
                } catch (JSONException e) {
                    Log.e(TAG, "updateFlightRequest:onResponse:JSONException:" + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e(TAG, "updateFlightRequest:onResponse:ParseException:" + e.getLocalizedMessage());
                }
            }
        };

        String url = URL.UPDATE_FLIGHT_REQUEST.getURL() + flightRequest.id + "/";
        JOR request = new JOR(url, URL.UPDATE_FLIGHT_REQUEST.getRequestType(), requestAsJson, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void deleteFlightRequest(final FlightRequest flightRequest, final ApiResponseListener<FlightRequest> listener) {
        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "deleteFlightRequest:onResponse:" + response.toString());
                listener.onResponse(flightRequest);
            }
        };

        String url = URL.DELETE_FLIGHT_REQUEST.getURL() + flightRequest.id + "/";
        JOR request = new JOR(url, URL.DELETE_FLIGHT_REQUEST.getRequestType(), jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }
}
