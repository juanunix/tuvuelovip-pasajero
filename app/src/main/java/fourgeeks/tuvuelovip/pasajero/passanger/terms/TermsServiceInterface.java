package fourgeeks.tuvuelovip.pasajero.passanger.terms;

import fourgeeks.tuvuelovip.pasajero.pojo.GenericResponse;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import rx.Single;

/**
 * Created by alacret on 4/21/17.
 */

public interface TermsServiceInterface {
    Single<Terms> getTerms();

    Single<Void> acceptTerms();

    Single<GenericResponse> areAccepted();
}
