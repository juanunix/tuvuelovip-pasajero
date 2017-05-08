package fourgeeks.tuvuelovip.pasajero.passanger.terms;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.passanger.TabView;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.SingleSubscriber;
import rx.Subscription;

/**
 * Created by alacret on 3/13/17.
 */
public class TermsView extends Fragment {

    private static final String TAG = TermsView.class.getSimpleName();
    private View view;
    private Subscription subscription;
    private TermsController controller;
    private ProgressBar progressBar;
    private Subscription acceptTermsSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_term, container, false);
        Button acceptButton = (Button) view.findViewById(R.id.accept);
        Button rejectButton = (Button) view.findViewById(R.id.reject);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        final TextView termsView = (TextView) view.findViewById(R.id.terms);

        controller = new TermsController(new TermsService());
        subscription = controller.getTerms().subscribe(new SingleSubscriber<Terms>() {
            @Override
            public void onSuccess(Terms value) {
                Log.d(TAG, "onCreateView:onSuccess");
                termsView.setText(value.text);
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onCreateView:onError:" + error.getLocalizedMessage());
                Toast.makeText(getActivity().getApplication(), error.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        if (Cache.termsAndConditionsWereAccepted())
            acceptButton.setVisibility(View.GONE);
        else
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptTerms();
                }
            });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        return view;
    }

    private void back() {
        getActivity().onBackPressed();
    }

    private void acceptTerms() {
        progressBar.setVisibility(View.VISIBLE);
        acceptTermsSubscription = controller.acceptTerms().subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void value) {
                Cache.setTermsAndConditionsWereAccepted(true);
                back();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onCreateView:onError:" + error.getLocalizedMessage());
                Toast.makeText(getActivity().getApplication(), error.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        if (acceptTermsSubscription != null && !acceptTermsSubscription.isUnsubscribed())
            acceptTermsSubscription.unsubscribe();
    }
}
