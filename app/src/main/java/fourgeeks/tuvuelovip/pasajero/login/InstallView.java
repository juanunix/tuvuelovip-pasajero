package fourgeeks.tuvuelovip.pasajero.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fourgeeks.tuvuelovip.pasajero.PassengerMain;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;
import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.JAR;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Country;
import fourgeeks.tuvuelovip.pasajero.R;

public class InstallView extends Fragment {

    private JAR JARequest;
    private View view;
    private TextView install_text;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_install, container, false);
        install_text = (TextView) view.findViewById(R.id.install_text);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMaxSeats();
    }

    public void getAiports() {
        Log.d("InstallView", "getAiports");

        JARequest = new JAR(URL.AIRPORT.getRequestType(), URL.AIRPORT.getURL(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("InstallView", "getCountries:onResponse:" + response);
                try {
                    // TODO: Pass login into Cache
                    Reservoir.putAsync("AIRPORTS", Airport.fromJsonArray(response), new ReservoirPutCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("InstallView", "getAiports:onResponse:onSuccess");
                            initApp();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e("InstallView", "getAiports:onResponse:onFailure:" + e.getLocalizedMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.e("InstallView", "getAiports:onResponse:JSONException:" + e.getLocalizedMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("InstallView", "getAiports:onErrorResponse:" + String.valueOf(error));
            }

        });

        install_text.setText(getString(R.string.airport_install));

        JARequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(JARequest, "API");

    }

    private void initApp() {
        Cache.setAppInstalled(true);
        Cache.preloadCache();
        Util.replaceNonRollbackableTransaction(R.id.holder_content, getFragmentManager(), new LoginView(), ViewTags.INSTALL);
    }

    public void getCountries() {
        Log.d("InstallView", "getCountries");

        JARequest = new JAR(URL.COUNTRIES.getRequestType(), URL.COUNTRIES.getURL(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("InstallView", "getCountries:onResponse");

                try {
                    // TODO: Pass login into Cache
                    Reservoir.putAsync("COUNTRIES", Country.fromJsonArray(response), new ReservoirPutCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("InstallView", "getCountries:onResponse:onSuccess");
                            getAiports();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e("InstallView", "getCountries:onResponse:onFailure:" + e.getLocalizedMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.e("InstallView", "getCountries:onResponse:JSONException:");
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("InstallView", "getCountries:onErrorResponse:" + String.valueOf(error));
            }

        });

        install_text.setText(getString(R.string.country_install));

        JARequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(JARequest, "API");
    }

    public void getMaxSeats() {
        Log.d("InstallView", "getMaxSeats");
        JOR request = new JOR(URL.MAX_SEATS, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("InstallView", "getMaxSeats:onResponse:" + response);
                try {
                    // TODO: Pass login into Cache
                    Reservoir.putAsync("MAX-SEATS", response.getInt("max_seats"), new ReservoirPutCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("InstallView", "getMaxSeats:onResponse:onSuccess");
                            getCountries();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e("InstallView", "getMaxSeats:onResponse:onFailure:" + e.getLocalizedMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.e("InstallView", "getMaxSeats:onResponse:JSONException:" + e.getLocalizedMessage());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("InstallView", "getMaxSeats:onErrorResponse:" + String.valueOf(error));
            }

        });

        install_text.setText(getString(R.string.seats_install));

        request.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        APP.getInstance().addToRequestQueue(request, "API");
    }
}
