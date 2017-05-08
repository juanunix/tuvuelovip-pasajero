package fourgeeks.tuvuelovip.pasajero.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;
import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.PassengerMain;
import fourgeeks.tuvuelovip.pasajero.login.recoverpassword.RequestRecoverPasswordView;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpView;
import fourgeeks.tuvuelovip.pasajero.R;


public class LoginView extends Fragment {

    private JOR JORequest;
    private View view;
    private TextInputEditText user, password;
    private TextView forgot_password, signup;
    private Button init_button;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_login, container, false);
        user = (TextInputEditText) view.findViewById(R.id.user);
        password = (TextInputEditText) view.findViewById(R.id.password);
        forgot_password = (TextView) view.findViewById(R.id.forgot_password);
        signup = (TextView) view.findViewById(R.id.signup);
        init_button = (Button) view.findViewById(R.id.init_button);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        Cache.setTermsAndConditionsWereAccepted(false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_passanger();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassenger();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

    }

    public void init_passanger() {
        //TODO: Pasar al controlador y a REtrofit
        final String usernameText = user.getText().toString();
        final String passwordText = password.getText().toString();

        if (usernameText.isEmpty()) {
            user.setError(getString(R.string.username_error));
            return;
        }

        if (passwordText.isEmpty()) {
            password.setError(getString(R.string.password_error));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        JSONObject LOGIN = new JSONObject();

        String token = FirebaseInstanceId.getInstance().getToken();
        Cache.setFirebaseToken(token);
        try {
            LOGIN.put("username", usernameText);
            LOGIN.put("password", passwordText);
            LOGIN.put("os", "ANDROID");
            LOGIN.put("device_id", token);

        } catch (JSONException e) {
            Log.e("LoginView", "init_passanger:JSONException:" + e.getLocalizedMessage());
        }

        JORequest = new JOR(URL.LOGIN, LOGIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("LoginView", "init_passanger:onResponse:" + response);
                progressBar.setVisibility(View.GONE);
                try {
                    Cache.setUserToken(response.getString("token"));
                    Cache.preloadCache();
                    Cache.setTermsAndConditionsWereAccepted(false);
                    Intent intent = new Intent(getActivity(), PassengerMain.class);
                    startActivity(intent);
                    getActivity().finish();
                } catch (JSONException e) {
                    Log.e("LoginView", "init_passanger:onResponse:JSONException:" + e.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                String errorMessage = getString(R.string.server_error);
                if (error instanceof TimeoutError) {
                    Log.e("LoginView", "init_passanger:onErrorResponse:TimeoutError:" + error.getLocalizedMessage());
                    errorMessage = getString(R.string.timeout_error);
                } else if (error instanceof NoConnectionError) {
                    Log.e("LoginView", "init_passanger:onErrorResponse: No Conection");
                    errorMessage = "No se detecto una conexión";
                } else if (error instanceof AuthFailureError) {
                    Log.e("LoginView", "init_passanger:onErrorResponse: Autentication error");
                    errorMessage = "Este usuario no ha sido aprobado";
                } else if (error.networkResponse.statusCode == 400) {
                    Log.e("LoginView", "init_passanger:onErrorResponse:" + new String(error.networkResponse.data));
                    errorMessage = new String(error.networkResponse.data).replace("\"", "").replace("'", "").replace("[", "").replace("]", "");
                }

                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
        JORequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        APP.getInstance().addToRequestQueue(JORequest, URL.LOGIN.getURL());
    }


    public void forgotPassenger() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        RequestRecoverPasswordView forgot_view = new RequestRecoverPasswordView();
        transaction.addToBackStack(ViewTags.LOGIN);
        transaction.add(R.id.holder_content, forgot_view, ViewTags.FORGOT);
        transaction.commit();
    }

    public void signup() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SignUpView signup_view = new SignUpView();
        transaction.addToBackStack("view_login");
        transaction.add(R.id.holder_content, signup_view, "signup_view");
        transaction.commit();
    }

}
