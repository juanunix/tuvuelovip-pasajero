package fourgeeks.tuvuelovip.pasajero.api;

import com.android.volley.VolleyError;

/**
 * Created by alacret on 12/9/16.
 */

public abstract class ApiResponseListener<T> {
    public abstract void onResponse(T result);
    public abstract void onError(VolleyError error, String message);
}
