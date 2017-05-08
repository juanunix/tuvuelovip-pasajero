package fourgeeks.tuvuelovip.pasajero.passanger.terms;

import fourgeeks.tuvuelovip.pasajero.pojo.GenericResponse;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpServiceInterface;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 3/13/17.
 */

public class TermsController {
    private TermsServiceInterface service;

    public TermsController(TermsServiceInterface service){
        this.service = service;
    }

    public Single<Terms> getTerms(){
        return service.getTerms();
    }

    public Single<Void> acceptTerms() {
        return service.acceptTerms();
    }

    public Single<GenericResponse> areAccepted() {
        return service.areAccepted();
    }
}
