package fourgeeks.tuvuelovip.pasajero.api;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APP extends Application {

    //GLOBAL
    private static APP instancie;
    private static final String TAG = APP.class.getSimpleName();
    private RequestQueue mRequestQueue;

    public static synchronized APP getInstance() {
        if (instancie == null) {
            instancie = new APP();
        }
        return instancie;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instancie = this;
    }


    //INIT Setup for VOLLEY 1
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    //INIT Setup for VOLLEY 2
    public <T> void addToRequestQueue(com.android.volley.Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        Log.e("Volley TAG", tag);
        getRequestQueue().add(req);
    }

}
