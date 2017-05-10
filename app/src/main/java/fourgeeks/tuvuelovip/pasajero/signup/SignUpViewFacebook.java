package fourgeeks.tuvuelovip.pasajero.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.login.LoginRetroFitService;
import fourgeeks.tuvuelovip.pasajero.pojo.Country;
import fourgeeks.tuvuelovip.pasajero.pojo.UserFacebook;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.SingleSubscriber;
import rx.Subscription;


public class SignUpViewFacebook extends Fragment {

    public static final String TAG = "SignUpViewFacebook";
    private List<String> countriesNamesList;
    private Map<String, Integer> countryMap;
    private SignUpController controller;
    private List<Country> countries = new ArrayList<>();
    private View view;
    private AutoCompleteTextView countryAuto;
    private TextInputEditText email, username, firtsName, lastName,dni;
    private Button signupButton;
    private Subscription subscription;
    private String facebookToken,facebookUserId;
    private Bundle bundle;
    private SignUpRetroFitService service;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.view_singup_facebook, container, false);
        bundle = getArguments();
        email = (TextInputEditText) view.findViewById(R.id.email);
        username = (TextInputEditText) view.findViewById(R.id.username);
        firtsName = (TextInputEditText) view.findViewById(R.id.firts_name);
        lastName = (TextInputEditText) view.findViewById(R.id.last_name);
        countryAuto = (AutoCompleteTextView) view.findViewById(R.id.country_auto);
        dni = (TextInputEditText) view.findViewById(R.id.dni);
        signupButton = (Button) view.findViewById(R.id.singup_button);

        insertarDatos();
        SignUpService signUpService = new SignUpService();
        controller = new SignUpController(signUpService);
        try {
            //TODO Optimizar y guardar un mapa en la cache
            countries = Cache.getCountries();
            countriesNamesList = new ArrayList<>(countries.size());
            countryMap = new HashMap<>(countries.size());
            for (Country country : countries) {
                countriesNamesList.add(country.name);
                countryMap.put(country.name, country.id);
            }
        } catch (IOException e) {
            Log.e(TAG, "onCreateView:" + e.getLocalizedMessage());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, countriesNamesList);
        countryAuto.setAdapter(adapter);

        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkFields()){
                    saveUser();

                    if (Cache.termsAndConditionsWereAccepted()) {

                    } else {
                        showTerms();
                    }

                }
            }
        });
        Cache.setTermsAndConditionsWereAccepted(false);

        return view;
    }

    private void saveUser() {
        Log.i("facebook","info: pais: "+countryAuto.getText().toString()+
                " apellido:"+lastName.getText().toString()+" nombre:"+firtsName.getText().toString()+
                " usuario: "+username.getText().toString()+" email:"+email.getText().toString()+
                " dni:"+dni.getText().toString()+" token"+ facebookToken+ "id: "+facebookUserId);

        service.createUserFacebook(countryMap.get(countryAuto.getText().toString()),
                lastName.getText().toString(),firtsName.getText().toString(),
                username.getText().toString(),email.getText().toString(),
                dni.getText().toString(), facebookToken,facebookUserId).enqueue(new Callback<UserFacebook>() {

            @Override
            public void onResponse(Call<UserFacebook> call, Response<UserFacebook> response) {
                Toast.makeText(getActivity(),"Registro exitoso",Toast.LENGTH_LONG);
                Log.i("tag","code: "+response.code());
                Log.i("tag","call "+call.request().toString());
            }

            @Override
            public void onFailure(Call<UserFacebook> call, Throwable t) {
               Log.i("SignUpFacebook","I coud not connect");
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    private void showTerms() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = new TermsFloatingView();
        newFragment.show(ft, "dialog");
    }
    private boolean checkFields(){

        boolean respuesta=true;

        if(email.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),getString(R.string.email_error),Toast.LENGTH_SHORT).show();
            respuesta=false;

        }
        else if(username.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),getString(R.string.username_error),Toast.LENGTH_SHORT).show();
            respuesta=false;

        }
        else if(firtsName.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.firstname_error,Toast.LENGTH_SHORT).show();
            respuesta=false;

        }
        else if(lastName.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.last_name,Toast.LENGTH_SHORT).show();
            respuesta=false;

        }
        else if(dni.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.dni_error,Toast.LENGTH_SHORT).show();
            respuesta=false;

        }else if(countryAuto.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),R.string.country_error,Toast.LENGTH_SHORT).show();
            respuesta=false;
        }
        Log.i(TAG, "respuesta :" + respuesta);
        return respuesta;

    }
    private void insertarDatos(){

       if(bundle.getInt("code") == 0) {
           email.setText(bundle.getString("email"));
           firtsName.setText(bundle.getString("name"));
           lastName.setText(bundle.getString("last"));
           facebookToken = bundle.getString("token");
           facebookUserId = bundle.getString("userID");
            Toast.makeText(getActivity(), getString(R.string.facebook_help), Toast.LENGTH_LONG).show();
            Log.i("SignUpViewFacebook", "Info: token : " + bundle.getString("token") + " UserID: " + bundle.getString("userID"));
        }else{
            facebookToken = AccessToken.getCurrentAccessToken().getToken();
            facebookUserId=AccessToken.getCurrentAccessToken().getUserId();
            Log.i("SignUpViewFacebook", "Info: token : " + facebookToken + " UserID: " + facebookUserId);

        }
    }

}
