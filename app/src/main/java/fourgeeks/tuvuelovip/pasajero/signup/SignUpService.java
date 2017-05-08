package fourgeeks.tuvuelovip.pasajero.signup;

import android.util.Log;

import fourgeeks.tuvuelovip.pasajero.pojo.FirebaseToken;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import fourgeeks.tuvuelovip.pasajero.pojo.UserFacebook;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import retrofit2.Retrofit;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 3/13/17.
 */

public class SignUpService implements SignUpServiceInterface {

    private final SignUpRetroFitService service;

    public SignUpService(){
        Retrofit retrofit = Cache.getRetrofitInstance();
        service = retrofit.create(SignUpRetroFitService.class);
    }

    @Override
    public Single<User> createUser(User user) {
        return service.createUser(user).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<Void> updateToken(FirebaseToken token) {
        return service.updateToken(token).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<Void> deleteToken(FirebaseToken token) {
        return service.deleteToken(token).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<UserFacebook> createUserFacebook(UserFacebook userFacebook) {
        return service.createUserFacebook(userFacebook).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }
}
