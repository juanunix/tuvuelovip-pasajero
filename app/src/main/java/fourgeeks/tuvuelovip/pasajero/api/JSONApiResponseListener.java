package fourgeeks.tuvuelovip.pasajero.api;

import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by alacret on 12/9/16.
 */

public abstract class JSONApiResponseListener<T> implements Response.Listener<T>, Response.ErrorListener {

    private ApiResponseListener<?> listener;

    public JSONApiResponseListener(ApiResponseListener<?> listener) {
        this.listener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String errorMsg = "";
        if (error.networkResponse != null) {
            errorMsg = new String(error.networkResponse.data);
        } else if (error instanceof NoConnectionError){
            errorMsg = "No conection";

        }else if (error instanceof TimeoutError) {
            errorMsg = "Time out Error";
        }else {
            errorMsg = error.getLocalizedMessage();
        }

        listener.onError(error, errorMsg);
        Log.e(this.getClass().getSimpleName(), ":onErrorResponse:" + errorMsg);
    }

}
