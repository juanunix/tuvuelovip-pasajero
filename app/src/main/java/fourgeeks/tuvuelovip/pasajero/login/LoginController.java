package fourgeeks.tuvuelovip.pasajero.login;

import fourgeeks.tuvuelovip.pasajero.api.URL;

/**
 * Created by leosantana on 26/04/17.
 */

public class LoginController {

    public static LoginRetroFitService getFacebookService(){
        return RetrofitFacebookClient.sendData(URL.MAIN_URL).create(LoginRetroFitService.class);
    }
}
