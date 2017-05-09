package fourgeeks.tuvuelovip.pasajero.signup;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fourgeeks.tuvuelovip.pasajero.PassengerMain;
import fourgeeks.tuvuelovip.pasajero.login.LoginView;
import fourgeeks.tuvuelovip.pasajero.passanger.terms.TermsService;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import rx.SingleSubscriber;
import rx.Subscription;

/**
 * Created by alacret on 3/13/17.
 */
public class TermsFloatingView extends DialogFragment {

    public static final String TAG = TermsFloatingView.class.getSimpleName();
    private View view;
    private Subscription subscription;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        view = inflater.inflate(R.layout.view_term, container, false);
        Button acceptButton = (Button) view.findViewById(R.id.accept);
        Button rejectButton = (Button) view.findViewById(R.id.reject);
        final TextView termsView = (TextView) view.findViewById(R.id.terms);

        TermsFloatingController controller = new TermsFloatingController(new TermsService());
        controller.getTerms().subscribe(new SingleSubscriber<Terms>() {
            @Override
            public void onSuccess(Terms value) {
                Log.d(TAG,"onCreateView:onSuccess");
                termsView.setText(value.text);
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG,"onCreateView:onError:" + error.getLocalizedMessage());
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cache.setTermsAndConditionsWereAccepted(true);
                getFragmentManager().popBackStack();
               goToLogin();
                Toast.makeText(getActivity(),"inicia secion para comenzar!",Toast.LENGTH_LONG);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
    private void goToLogin(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LoginView loginView = new LoginView();
        transaction.addToBackStack("termsFloatin");
        transaction.add(R.id.holder_content,loginView, "loginview");
        transaction.commit();
    }
}
