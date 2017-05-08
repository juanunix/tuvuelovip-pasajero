package fourgeeks.tuvuelovip.pasajero.signup;

import fourgeeks.tuvuelovip.pasajero.pojo.FirebaseToken;
import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import rx.Observable;
import rx.Single;

/**
 * Created by alacret on 3/13/17.
 */

public interface SignUpServiceInterface {
    Single<User> createUser(User user);
    Single<Void> updateToken(FirebaseToken token);
    Single<Void> deleteToken(FirebaseToken token);
}
