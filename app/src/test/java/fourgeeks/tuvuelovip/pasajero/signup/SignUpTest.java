package fourgeeks.tuvuelovip.pasajero.signup;

import org.junit.Before;
import org.junit.Test;

import fourgeeks.tuvuelovip.pasajero.pojo.User;
import fourgeeks.tuvuelovip.pasajero.R;
import rx.SingleSubscriber;

import static org.junit.Assert.*;

/**
 * Tests SignUpController
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SignUpTest {

    private SignUpController controller;

    @Before
    public void prepare(){
        controller = new SignUpController(new SignUpTestService());
    }

    @Test
    public void emailIsEmptyOrIncorrect() throws Exception {
        controller.createUser("","","","",0,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Email can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.email_error));
            }
        });
        controller.createUser("alacret","","","",1,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Email must be properly formed");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.email_error));
            }
        });
    }

    @Test
    public void usernameIsEmpty() throws Exception {
        controller.createUser("alacret@gmail.com","","","",1,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Username can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.username_error));
            }
        });
    }

    @Test
    public void firstNameIsEmpty() throws Exception {
        controller.createUser("alacret@gmail.com","alacret","","",0,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("First Name can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.firstname_error));
            }
        });
    }

    @Test
    public void lastNameIsEmpty() throws Exception {
        controller.createUser("alacret@gmail.com","alacret","Angel","",0,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Last Name can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.lastname_error));
            }
        });
    }

    @Test
    public void countryIdIsEmptyOrIncorrect() throws Exception {
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",0,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Country ID can't be null");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.country_error));
            }
        });
    }

    @Test
    public void passwordIsEmptyOrIncorrect() throws Exception {
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",1,"","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Password can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.password_error));
            }
        });
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",1,"password","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Password must be valid");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.valid_password_error));
            }
        });
    }

    @Test
    public void passwordConfirmatioIsEmptyOrIncorrect() throws Exception {
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",1,"password123","","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Password Confirmation can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.password_error));
            }
        });
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",1,"password123","password12","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("Password Confirmation must be valid");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.matching_password_error));
            }
        });
    }

    @Test
    public void dniIsEmptyOrNull() throws Exception {
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",1,"password123","password123","").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("DNI can't be empty");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.dni_error));
            }
        });
        controller.createUser("alacret@gmail.com","alacret","Angel","Lacret",1,"password123","password123",null).subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User value) {
                fail("DNI can't be null");
            }

            @Override
            public void onError(Throwable error) {
                assertEquals(error.getMessage(),String.valueOf(R.string.dni_error));
            }
        });
    }

    @Test
    public void userIsValid() throws Exception {
        final String email = "alacret@gmail.com";
        controller.createUser(email,"alacret","Angel","Lacret",1,"password123","password123","12341234").subscribe(new SingleSubscriber<User>() {
            @Override
            public void onSuccess(User user) {
                assertEquals(user.email, email);
            }

            @Override
            public void onError(Throwable error) {
                fail("User shouyld be created in this case");
            }
        });
    }

}