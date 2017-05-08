package fourgeeks.tuvuelovip.pasajero.signup;

import fourgeeks.tuvuelovip.pasajero.pojo.FirebaseToken;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import fourgeeks.tuvuelovip.pasajero.pojo.UserFacebook;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by alacret on 3/13/17.
 */

public interface SignUpRetroFitService {
    @POST("signup-passenger/")
    Observable<User> createUser(@Body User user);

    @PUT("mobile/profile/update_token_push/")
    Observable<Void> updateToken(@Body FirebaseToken token);

    @HTTP(method = "DELETE", path = "mobile/profile/delete_token_push/", hasBody = true)
    Observable<Void> deleteToken(@Body FirebaseToken token);

    @POST("signup-facebook-passenger/")
    Observable<UserFacebook>createUserFacebook(@Body UserFacebook userFacebook);

}
