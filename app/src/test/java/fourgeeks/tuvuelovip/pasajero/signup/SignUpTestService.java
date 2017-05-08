package fourgeeks.tuvuelovip.pasajero.signup;

import java.util.concurrent.Callable;

import fourgeeks.tuvuelovip.pasajero.pojo.Terms;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import rx.Single;

/**
 * Created by alacret on 3/13/17.
 */

public class SignUpTestService implements SignUpServiceInterface {
    @Override
    public Single<Terms> getTerms() {
        return Single.fromCallable(new Callable<Terms>() {
            @Override
            public Terms call() throws Exception {
                Terms terms = new Terms();
                terms.text = "Terminos y condiciones";
                return terms;
            }
        });
    }

    @Override
    public Single<User> createUser(final User user) {
        return Single.fromCallable(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return user;
            }
        });
    }
}
