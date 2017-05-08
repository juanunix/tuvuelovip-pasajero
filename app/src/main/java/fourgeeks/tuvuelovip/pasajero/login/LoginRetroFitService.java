package fourgeeks.tuvuelovip.pasajero.login;

import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alacret on 3/13/17.
 */

public interface LoginRetroFitService {
    @GET("profile/update-token/{oldToken}/{newToken}")
    Observable<Void> updateToken(@Path("oldToken") String oldToken,@Path("newToken") String newToken);

    @GET("profile/remove-token/{token}")
    Observable<Terms> removeToken(@Path("token") String token);
}
