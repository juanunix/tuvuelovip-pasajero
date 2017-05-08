package fourgeeks.tuvuelovip.pasajero.passanger.profile;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;

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

public final class ProfileModel {

    private ProfileModel() {
    }

    public static void getProfile(final ApiResponseListener<Profile> listener) {

        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ProfileModel", "getProfile:onResponse:" + response);
                Profile profile;
                try {
                    profile = Profile.fromJsonObject(response);
                } catch (JSONException e) {
                    Log.e("ProfileModel", "getProfile:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                    return;
                }
                listener.onResponse(profile);
            }
        };

        JOR request = new JOR(URL.PROFILE, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void saveProfile(Profile profile, final ApiResponseListener<Profile> listener) {
        Log.d("ProfileModel", "saveProfile");
        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ProfileModel", "saveProfile:onResponse:" + response);
                Profile profile;
                try {
                    profile = Profile.fromJsonObject(response);
                } catch (JSONException e) {
                    Log.e("ProfileModel", "saveProfile:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                    return;
                }
                listener.onResponse(profile);
            }
        };
        JSONObject profileAsJson;
        try {
            profileAsJson = profile.toJSONObject();
        } catch (JSONException e) {
            Log.e("ProfileModel", "saveProfile:JSONException:" + e.getLocalizedMessage());
            listener.onError(null, e.getLocalizedMessage());
            return;
        }
        Log.d("ProfileModel", "saveProfile:JSON:" + profileAsJson);

        JOR request = new JOR(URL.PROFILE.getURL() + String.valueOf(profile.id) + "/",
                Request.Method.PUT, profileAsJson, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void getContactProfile(final ApiResponseListener<Profile> listener, Profile profile) {
        JSONApiResponseListener<JSONObject> jsonApiResponseListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Profile profile = new Profile();
                try {
                    profile = Profile.contactFromJsonObject(response);
                } catch (JSONException e) {
                    listener.onError(null, e.getLocalizedMessage());
                }
                listener.onResponse(profile);
            }
        };

        JOR request = new JOR(URL.PROFILE.getURL() + profile.id + "/getContact/", URL.PROFILE.getRequestType(), jsonApiResponseListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }

    public static void saveContactProfile(Profile profile, Profile contact, final ApiResponseListener<Profile> listener) {
        Log.d("ProfileModel", "saveContactProfile");
        JSONApiResponseListener<JSONObject> jsonListener = new JSONApiResponseListener<JSONObject>(listener) {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ProfileModel", "saveContactProfile:onResponse:" + response);
                Profile profile;
                try {
                    profile = Profile.contactFromJsonObject(response);
                } catch (JSONException e) {
                    Log.e("ProfileModel", "saveContactProfile:onResponse:JSONException:" + e.getLocalizedMessage());
                    listener.onError(null, e.getLocalizedMessage());
                    return;
                }
                listener.onResponse(profile);
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                if (e.getMessage() != null) {
                    Log.d("", "onErrorResponse: " + e.getLocalizedMessage());
                }

            }
        };
        JSONObject profileAsJson;
        try {
            profileAsJson = contact.contactToJSONObject();
        } catch (JSONException e) {
            Log.e("ProfileModel", "saveContactProfile:JSONException:" + e.getLocalizedMessage());
            listener.onError(null, e.getLocalizedMessage());
            return;
        }
        Log.d("ProfileModel", "saveContactProfile:JSON:" + profileAsJson);
        JOR request = new JOR(URL.PROFILE.getURL() + String.valueOf(profile.id) + "/contact/",
                Request.Method.PUT, profileAsJson, jsonListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }
}