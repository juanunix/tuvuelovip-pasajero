package fourgeeks.tuvuelovip.pasajero.login;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.JSONApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.api.URL;


/**
 * Created by alacret on 12/9/16.
 */

public final class RecoverPasswordModel {

    private RecoverPasswordModel(){}

    public static void requestRecoverPassword(String email, final ApiResponseListener<String> listener){

        JSONApiResponseListener<JSONObject> jsonListener= new JSONApiResponseListener<JSONObject>(listener) {

            @Override
            public void onResponse(JSONObject response) {
//                                    listener.onResponse("username");
                try {
                    listener.onResponse(response.getString("email"));
                } catch (JSONException e) {
                    Log.e("RecoverPasswordModel", "requestRecoverPassword:onResponse:JSONException:" + e.getLocalizedMessage());
                }
            }
        };

        JSONObject data = new JSONObject();
        try {
            data.put("email", email);
        } catch (JSONException e) {
            Log.e("RecoverPasswordModel", "requestRecoverPassword:onResponse:JSONException:" + e.getMessage());
        }

        JOR request = new JOR(URL.REQUEST_RECOVER_PASSWORD, data, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void recoverPassword(String code, String password, final ApiResponseListener<String> listener){

        JSONApiResponseListener<JSONObject> jsonListener= new JSONApiResponseListener<JSONObject>(listener) {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("RecoverPasswordModel", "recoverPassword:onResponse:" + response);
                try {
                    listener.onResponse(response.getString("username"));
                } catch (JSONException e) {
                    Log.e("RecoverPasswordModel", "recoverPassword:onResponse:JSONException:" + e.getLocalizedMessage());
                }
            }
        };

        JSONObject data = new JSONObject();
        try {
            data.put("code", code);
            data.put("password", password);
        } catch (JSONException e) {
            Log.e("RecoverPasswordModel", "recoverPassword:onResponse:JSONException:" + e.getMessage());
        }

        JOR request = new JOR(URL.RECOVER_PASSWORD, data, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

}
