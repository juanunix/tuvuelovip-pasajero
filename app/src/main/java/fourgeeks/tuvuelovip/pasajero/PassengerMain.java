package fourgeeks.tuvuelovip.pasajero;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import fourgeeks.tuvuelovip.pasajero.passanger.airport.AirportsView;
import fourgeeks.tuvuelovip.pasajero.passanger.flight.FlightView;
import fourgeeks.tuvuelovip.pasajero.passanger.terms.TermsController;
import fourgeeks.tuvuelovip.pasajero.passanger.terms.TermsService;
import fourgeeks.tuvuelovip.pasajero.passanger.terms.TermsView;
import fourgeeks.tuvuelovip.pasajero.pojo.FirebaseToken;
import fourgeeks.tuvuelovip.pasajero.pojo.GenericResponse;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpService;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.passanger.TabView;
import fourgeeks.tuvuelovip.pasajero.passanger.profile.ProfileView;
import rx.SingleSubscriber;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class PassengerMain extends AppCompatActivity {

    private static final String TAG = PassengerMain.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private int currentNavigationItem = R.id.home;

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        Fragment fragmentById = fragmentManager.findFragmentById(R.id.sub_frame);
        if (backStackEntryCount == 1 && fragmentById instanceof TabView) {
            finish();
            System.exit(0);
        }
        super.onBackPressed();
        if (fragmentManager.findFragmentById(R.id.sub_frame) == null) {
            currentNavigationItem = R.id.home;
            Util.addRollbackableTransaction(R.id.sub_frame, fragmentManager, new TabView());
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.view_main);

        Cache.init(this);

        if (!Cache.isSesionActive()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }

        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);
        appBar.setNavigationIcon(R.drawable.icon_firts);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        setupDrawer(navigationView);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                appBar,
                R.string.accept,
                R.string.accept) {
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Util.addRollbackableTransaction(R.id.sub_frame, getSupportFragmentManager(), new TabView());

        handlePossibleIntent();
    }

    private void handlePossibleIntent() {
        String type = getIntent().getStringExtra("type");
        if (type == null)
            return;

        switch (type) {
            case Util.TERMS_NOTIFICATION_TYPE: {
                goToAproveTerms();
                break;
            }
            case Util.FLIGHT_NOTIFICATION_TYPE: {
                String flightId = getIntent().getStringExtra("flightId");
                if (flightId != null) {
                    FlightView fragment = new FlightView();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("flightId", flightId);
                    fragment.onCreate(bundle);
                    Util.addRollbackableTransaction(R.id.main_frame, getSupportFragmentManager(), fragment);
                } else
                    throw new RuntimeException("No viene la clave 'flightId'. Esta verga es culpa de Richard");

                break;
            }
            case Util.FLIGHT_TAKEN_NOTIFICATION_TYPE: {
                Util.addRollbackableTransaction(R.id.sub_frame, getSupportFragmentManager(), new TabView());
                break;
            }
        }

        // Prevent duplicity
        if (!type.equals(Util.TERMS_NOTIFICATION_TYPE))
            checkForAcceptedTerms();
    }

    private void goToAproveTerms() {
        Cache.setTermsAndConditionsWereAccepted(false);
        Util.replaceRollbackableTransaction(R.id.sub_frame, getSupportFragmentManager(), new TermsView());
    }

    private void checkForAcceptedTerms() {
        new TermsController(new TermsService()).areAccepted().subscribe(new SingleSubscriber<GenericResponse>() {
            @Override
            public void onSuccess(GenericResponse value) {
                if (!value.accepted)
                    goToAproveTerms();
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "checkForAcceptedTerms:onError:" + error.getLocalizedMessage());
                Toast.makeText(getApplication(), error.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDrawer(NavigationView mNv) {
        mNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(false);

                int selectedNavigationItem = item.getItemId();

                // if we are alredy there we do not need to do anything
                if (currentNavigationItem == selectedNavigationItem) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                //If is Close Session dont do anything else
                if (selectedNavigationItem == R.id.close_sesion) {
                    closeSession();
                    return true;
                }

                currentNavigationItem = selectedNavigationItem;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();

                if (selectedNavigationItem == R.id.favorite_airports)
                    Util.addRollbackableTransaction(R.id.sub_frame, fragmentManager, new AirportsView());

                if (selectedNavigationItem == R.id.profile)
                    Util.addRollbackableTransaction(R.id.sub_frame, fragmentManager, new ProfileView());

                if (selectedNavigationItem == R.id.home)
                    Util.addRollbackableTransaction(R.id.sub_frame, fragmentManager, new TabView());

                if (selectedNavigationItem == R.id.terms_and_conditions)
                    Util.addRollbackableTransaction(R.id.sub_frame, fragmentManager, new TermsView());

                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void closeSession() {
        final String firebaseToken = Cache.getFirebaseToken();
        new SignUpService().deleteToken(new FirebaseToken(firebaseToken)).subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void value) {
                Log.d(TAG, "closeSession: Token deleted");
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "closeSession: Error deleting Token");
            }
        });

        Cache.clearCache();

        try {
            Log.d(TAG, "closeSession: deleting Firebase Instance");
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            Log.e(TAG, "closeSession: deleting Firebase Instance:" + e.getLocalizedMessage());
        }

        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);
    }

}
