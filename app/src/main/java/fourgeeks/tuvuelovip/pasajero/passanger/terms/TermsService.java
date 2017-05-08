package fourgeeks.tuvuelovip.pasajero.passanger.terms;

import fourgeeks.tuvuelovip.pasajero.pojo.GenericResponse;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpRetroFitService;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import retrofit2.Retrofit;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 4/21/17.
 */

public class TermsService implements TermsServiceInterface {

    public Single<Terms> getTerms(){
        Retrofit retrofit = Cache.getRetrofitInstance();
        TermsRetrofitService service = retrofit.create(TermsRetrofitService.class);
        return service.getTerms().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<Void> acceptTerms() {
        Retrofit retrofit = Cache.getAuthRetrofitInstance();
        TermsRetrofitService service = retrofit.create(TermsRetrofitService.class);
        return service.acceptTerms().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }

    @Override
    public Single<GenericResponse> areAccepted() {
        Retrofit retrofit = Cache.getAuthRetrofitInstance();
        TermsRetrofitService service = retrofit.create(TermsRetrofitService.class);
        return service.areAccepted().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).toSingle();
    }
}
