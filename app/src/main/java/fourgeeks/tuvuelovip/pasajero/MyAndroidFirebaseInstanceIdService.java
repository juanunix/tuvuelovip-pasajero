package fourgeeks.tuvuelovip.pasajero;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import fourgeeks.tuvuelovip.pasajero.pojo.FirebaseToken;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpService;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpServiceInterface;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import rx.SingleSubscriber;

public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Send To the server
        FirebaseToken token = new FirebaseToken(Cache.getFirebaseToken(), refreshedToken);
        SignUpServiceInterface service = new SignUpService();
        service.updateToken(token).subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void value) {
                Cache.setFirebaseToken(refreshedToken);
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        });

    }

}