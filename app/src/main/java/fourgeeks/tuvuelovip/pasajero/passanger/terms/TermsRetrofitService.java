package fourgeeks.tuvuelovip.pasajero.passanger.terms;

import fourgeeks.tuvuelovip.pasajero.pojo.GenericResponse;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by alacret on 4/21/17.
 */

public interface TermsRetrofitService {
    @GET("terms/")
    Observable<Terms> getTerms();

    @PUT("/terms/accept/")
    Observable<Void> acceptTerms();

    @GET("/terms/is-accepted/")
    Observable<GenericResponse> areAccepted();
}
