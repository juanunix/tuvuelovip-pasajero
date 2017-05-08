package fourgeeks.tuvuelovip.pasajero.signup;

import fourgeeks.tuvuelovip.pasajero.passanger.terms.TermsController;
import fourgeeks.tuvuelovip.pasajero.passanger.terms.TermsServiceInterface;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import rx.Observable;
import rx.Single;

/**
 * Created by alacret on 3/13/17.
 */

public class TermsFloatingController {
    private TermsServiceInterface service;

    public TermsFloatingController(TermsServiceInterface service){
        this.service = service;
    }

    public Single<Terms> getTerms(){
        return service.getTerms();
    }
}
