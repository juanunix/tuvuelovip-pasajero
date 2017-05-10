package fourgeeks.tuvuelovip.pasajero.signup;

import fourgeeks.tuvuelovip.pasajero.pojo.FirebaseToken;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import fourgeeks.tuvuelovip.pasajero.pojo.UserFacebook;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    @FormUrlEncoded
    Call<UserFacebook> createUserFacebook(@Field("country_id")int country_id,@Field("last_name")String last_name,
                                          @Field("first_name")String first_name,@Field("username")String username,
                                          @Field("email")String email,@Field("dni")String dni,
                                          @Field("facebook_access_token")String facebookToken,@Field("facebookId")String facebookId);

}
