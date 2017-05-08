package fourgeeks.tuvuelovip.pasajero.passanger.myflights.confirmation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.passanger.myflights.MyFlightsController;
import fourgeeks.tuvuelovip.pasajero.passanger.myflights.MyFlightsService;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import rx.SingleSubscriber;


public class FlightNotTakenView extends Fragment {
    public static final String TAG = FlightNotTakenView.class.getSimpleName();
    private View view;
    private Button buttonCancel, buttonSave;
    private long flightId = 0;
    private ProgressBar progressBar;
    private MyFlightsController controller;
    private EditText reviewText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            flightId = savedInstanceState.getLong("flightId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.flight_not_taken_view, container, false);
        buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        reviewText = (EditText) view.findViewById(R.id.comments);

        controller = new MyFlightsController(new MyFlightsService());

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSave();
            }
        });
        return view;
    }

    private void confirmSave() {
        final FragmentActivity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.confirm_rating))
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        save(activity);
                    }
                })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getFragmentManager().popBackStack();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void save(final FragmentActivity activity) {
        progressBar.setVisibility(View.VISIBLE);
        controller.rateFlightNotTaken(flightId, reviewText.getText().toString()).subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void value) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, getString(R.string.flight_rated), Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
                controller.getMyFlights().subscribe(new SingleSubscriber<Void>() {
                    @Override
                    public void onSuccess(Void value) {

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                progressBar.setVisibility(View.GONE);
                String msg = Util.msgFromRetrofitThrowable(FlightNotTakenView.this, error);
                Log.e(TAG, "confirmSave:onError:" + msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
