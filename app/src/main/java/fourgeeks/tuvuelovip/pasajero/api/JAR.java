package fourgeeks.tuvuelovip.pasajero.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.util.Cache;

/**
 * Created by Carlos_Lopez on 1/5/17.
 */

public class JAR extends JsonArrayRequest {


    public JAR(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public JAR(URL urlEnum, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        super(urlEnum.getRequestType(), urlEnum.getURL(), successListener, errorListener);
    }

    /*  */
    public JAR(URL urlEnum, JSONApiResponseListener<JSONArray> listener) {
        super(urlEnum.getRequestType(), urlEnum.getURL(), listener, listener);
    }

    public JAR(int method, String url, JSONApiResponseListener<JSONArray> listener) {
        super(method, url, listener, listener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/json");
        String token = Cache.getUserToken();

        if (token != null) {
            Log.d("JAR", "getHeaders: " + String.format("token %s", token));
            headers.put("Authorization", String.format("token %s", token));
        }
        return headers;
    }


}
