package fourgeeks.tuvuelovip.pasajero.signup;

import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.pojo.User;
import fourgeeks.tuvuelovip.pasajero.R;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * SignUp Controller
 * <p>
 * Class for the methods of the SignUp View
 * <p>
 * Created by alacret on 3/13/17.
 */

public class SignUpController {
    private SignUpServiceInterface service;

    /**
     * Construct a new SignUp Controller with the service implementation
     *
     * @param signUpService The SignUpService implementation
     */
    public SignUpController(SignUpServiceInterface signUpService) {
        this.service = signUpService;
    }

    /**
     * Create a User
     * @param email
     * @param username
     * @param firstName
     * @param lastName
     * @param password
     * @param passwordConfirmation
     * @param countryId
     * @return and Observable for creating a User
     */
    public Single<User> createUser(String email, String username, String firstName,
                                   String lastName, Integer countryId, String password,
                                   String passwordConfirmation, String dni) {

        if (email.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.email_error));

        if (!Util.isEmailValid(email))
            return Util.createOnErrorSingle(String.valueOf(R.string.email_error));

        if (username.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.username_error));

        if (firstName.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.firstname_error));

        if (lastName.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.lastname_error));

        if (countryId == null || countryId == 0)
            return Util.createOnErrorSingle(String.valueOf(R.string.country_error));

        if (password.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.password_error));

        if (!Util.isAlphaNumeric(password))
            return Util.createOnErrorSingle(String.valueOf(R.string.valid_password_error));

        if (passwordConfirmation.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.password_error));

        if (!passwordConfirmation.equals(password))
            return Util.createOnErrorSingle(String.valueOf(R.string.matching_password_error));

        if (dni == null || dni.isEmpty())
            return Util.createOnErrorSingle(String.valueOf(R.string.dni_error));

        User user = new User(password, countryId, lastName, firstName, username, email, dni, dni);

        return service.createUser(user);
    }
}
