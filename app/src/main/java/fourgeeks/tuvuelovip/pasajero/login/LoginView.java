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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import fourgeeks.tuvuelovip.pasajero.pojo.SocialData;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpViewFacebook;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;
import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.JOR;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.PassengerMain;
import fourgeeks.tuvuelovip.pasajero.login.recoverpassword.RequestRecoverPasswordView;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpView;
import fourgeeks.tuvuelovip.pasajero.R;
import retrofit2.Call;
import retrofit2.Callback;


public class LoginView extends Fragment {

    private JOR JORequest;
    private View view;
    private TextInputEditText user, password;
    private TextView forgot_password, signup;
    private Button init_button;
    private ProgressBar progressBar;
    private CallbackManager callbackM;
    private LinearLayout facebookView;
    private LoginButton loginButton;
    private LoginRetroFitService service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //callBack de facebook
        FacebookSdk.sdkInitialize(getActivity());
        callbackM = CallbackManager.Factory.create();

        view = inflater.inflate(R.layout.view_login, container, false);
        user = (TextInputEditText) view.findViewById(R.id.user);
        password = (TextInputEditText) view.findViewById(R.id.password);
        forgot_password = (TextView) view.findViewById(R.id.forgot_password);
        signup = (TextView) view.findViewById(R.id.signup);
        init_button = (Button) view.findViewById(R.id.init_button);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        Cache.setTermsAndConditionsWereAccepted(false);
        facebookView = (LinearLayout) view.findViewById(R.id.facebook_view);
        loginButton=(LoginButton)view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        initializeFacebookManager();
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

        service = LoginController.getFacebookService();
        facebookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessToken.getCurrentAccessToken()==null) {
                    Log.i("Facebook Result","you aren't log in");
                    loginButton.performClick();
                    loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email","id"));
                }else {
                   /*Log.i("Facebook Result","you are log in");
                   LoginManager.getInstance().logOut();
                   Log.i("Facebook Result","you are not");*/
                    Toast.makeText(getActivity(), "ya tienes una cuenta iniciada en facebook", Toast.LENGTH_LONG).show();
                    //sendFacebookUser(AccessToken.getCurrentAccessToken().getToken(),null,null,null);

                }



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

    private void initializeFacebookManager(){
        LoginManager.getInstance().registerCallback(callbackM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try{
                                    sendFacebookUser(AccessToken.getCurrentAccessToken().getToken(),object.getString("first_name")
                                            ,object.getString("last_name"),object.getString("email"),object.getString("id"));



                                    //Toast.makeText(getActivity(),"name: "+object.getString("name")+"\nemail: "+object.getString("email"),Toast.LENGTH_LONG).show();

                                }catch (Exception e){
                                    Log.e("Error in JsonFacebook: ",e.getMessage());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(),"Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackM.onActivityResult(requestCode,resultCode,data);
    }

    private void sendFacebookUser(final String access_token, final String name, final String lastName, final String email,
                                  final String userId){
        Log.i("SendFacebookUser",access_token);
        service.sendFacebook(access_token).enqueue(new Callback<SocialData>() {
            @Override
            public void onResponse(Call<SocialData> call, retrofit2.Response<SocialData> response) {
                Log.i("onResponse code",""+response.code());
                Log.i("onResponse rq",call.request().toString());
                if(response.isSuccessful()){
                    Log.i("SendFacebookUser", "Response from server " + response.body().getRegistered());
                    if(response.body().getRegistered()==false) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        SignUpViewFacebook signup_view = new SignUpViewFacebook();
                        transaction.addToBackStack("view_login");
                        transaction.add(R.id.holder_content, signup_view, "signup_view_facebook");
                        Bundle bundle = new Bundle();
                        bundle.putString("name",name);
                        bundle.putString("last",lastName);
                        bundle.putString("email",email);
                        bundle.putString("token",access_token);
                        bundle.putString("userID",userId);
                        signup_view.setArguments(bundle);
                        transaction.commit();

                        Log.i("SendFacebookUser", "code: " + response.code());

                    }else{
                        //get the token , safe it in the variable , and send the user to the PassengerMain.activity
                        Cache.setUserToken(response.body().getToken());
                        Intent goToPassenger = new Intent(getActivity(), PassengerMain.class);
                        startActivity(goToPassenger);
                        getActivity().finish();


                    }
                }

            }

            @Override
            public void onFailure(Call<SocialData> call, Throwable t) {
                Log.e("SendFacebookUser","I coudn't conected code:"+
                        call.hashCode()+" request :"+call.request().toString());
            }
        });
    }

}
