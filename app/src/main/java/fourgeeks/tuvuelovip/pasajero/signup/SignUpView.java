package fourgeeks.tuvuelovip.pasajero.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.pojo.Country;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.SingleSubscriber;
import rx.Subscription;


public class SignUpView extends Fragment {

    public static final String TAG = "SignUpView";
    private List<String> countriesNamesList;
    private Map<String, Integer> countryMap;
    private SignUpController controller;
    private List<Country> countries = new ArrayList<>();
    private View view;
    private AutoCompleteTextView countryAuto;
    private TextInputEditText email, username, firtsName, lastName, password, password_confirm,
            dni;
    private Button signupButton;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.view_singup, container, false);
        email = (TextInputEditText) view.findViewById(R.id.email);
        username = (TextInputEditText) view.findViewById(R.id.username);
        firtsName = (TextInputEditText) view.findViewById(R.id.firts_name);
        lastName = (TextInputEditText) view.findViewById(R.id.last_name);
        countryAuto = (AutoCompleteTextView) view.findViewById(R.id.country_auto);
        password = (TextInputEditText) view.findViewById(R.id.password);
        password_confirm = (TextInputEditText) view.findViewById(R.id.password_confirm);
        dni = (TextInputEditText) view.findViewById(R.id.dni);
        signupButton = (Button) view.findViewById(R.id.singup_button);
        //
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
                if (Cache.termsAndConditionsWereAccepted()) {
                    saveUser();
                } else {
                    showTerms();
                }
            }
        });
        return view;

    }

    private void saveUser() {
        subscription = controller.createUser(email.getText().toString(), username.getText().toString(),
                firtsName.getText().toString(), lastName.getText().toString(),
                countryMap.get(countryAuto.getText().toString()), password.getText().toString(), password_confirm.getText().toString(),
                dni.getText().toString()).subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                Log.d(TAG, "saveUser:onSuccess:" + value);
                Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }

            @Override
            public void onError(Throwable e) {
                String msg = Util.msgFromRetrofitThrowable(SignUpView.this, e);
                Log.e(TAG, "saveUser:onError:" + msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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

}
