package fourgeeks.tuvuelovip.pasajero.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.util.Cache;

/**
 * Created by Carlos_Lopez on 1/5/17.
 */

public class JOR extends JsonObjectRequest {

    public JOR(URL url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url.getRequestType(), url.getURL(), jsonRequest, listener, errorListener);
    }


    public JOR(URL url, JSONObject jsonData, JSONApiResponseListener<JSONObject> listener) {
        super(url.getRequestType(), url.getURL(), jsonData, listener, listener);
    }

    public JOR(URL url, JSONApiResponseListener<JSONObject> listener) {
        super(url.getRequestType(), url.getURL(), listener, listener);
    }

    public JOR(URL url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url.getRequestType(), url.getURL(), listener, errorListener);
    }

    public JOR(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    public JOR(String url, int method, JSONObject data, JSONApiResponseListener<JSONObject> listener) {
        super(method, url, data, listener, listener);
    }

    public JOR(String url, int method, JSONApiResponseListener<JSONObject> listener) {
        super(method, url, listener, listener);
    }

    public JOR(String url, int method, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/json");
        String token = Cache.getUserToken();

        if (token != null) {
            Log.d("JOR","getHeaders:FIREBASE_TOKEN "+String.format("token %s",token));
            headers.put("Authorization", String.format("token %s", token));
        }
        return headers;
    }

}
