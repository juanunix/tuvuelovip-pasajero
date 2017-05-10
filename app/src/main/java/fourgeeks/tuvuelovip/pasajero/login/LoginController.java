package fourgeeks.tuvuelovip.pasajero.login;

import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpRetroFitService;

/**
 * Created by leosantana on 26/04/17.
 */

public class LoginController {

    public static LoginRetroFitService getFacebookService(){
        return RetrofitFacebookClient.sendData(URL.MAIN_URL).create(LoginRetroFitService.class);
    }
    public static SignUpRetroFitService getSignupFacebookService(){
        return RetrofitFacebookClient.sendData(URL.MAIN_URL).create(SignUpRetroFitService.class);
    }
}
