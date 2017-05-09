package fourgeeks.tuvuelovip.pasajero.api;

import com.android.volley.Request;

/**
 * Created by Carlos_Lopez on 1/5/17.
 */

public enum URL {

    MY_BUY_FLIGHT(0, "v3/mobile/flightHistory/"),
    MY_FLIGHT_REQUEST(Request.Method.GET, "v3/mobile/myFlightRequest/"),
    DELETE_FLIGHT_REQUEST(Request.Method.DELETE, "v3/mobile/myFlightRequest/"),
    OLD_MY_FLIGHT_REQUEST(Request.Method.GET, "myFlightRequest/"),
    FLIGHT_REQUEST(Request.Method.GET, "mobile/flightRequest/"),
    SAVE_FLIGHT_REQUEST(Request.Method.POST, "v3/mobile/flightRequest/"),
    UPDATE_FLIGHT_REQUEST(Request.Method.PUT, "v3/mobile/flightRequest/"),
    RECOVER_PASSWORD(Request.Method.POST, "mobile/recover-password/"),
    REQUEST_RECOVER_PASSWORD(Request.Method.POST, "mobile/request-recover-password/"),
    CHANGE_PASSWORD(1, "mobile/change-password/"),
    BOOK_FLIGHT(Request.Method.PUT, "bookFlight/"),
    FREE_FLIGHT(Request.Method.PUT, "freeFlight/"),
    AIRPORT(Request.Method.GET, "mobile/airport/"),
    FLIGHTS(Request.Method.GET, "v3/mobile/flight/"),
    SIGNUP_PASSENGER(1, "signup-passenger/"),
    LOGIN(Request.Method.POST, "login/"),
    COUNTRIES(Request.Method.GET, "country/"),
    PROFILE(Request.Method.GET, "mobile/profile/"),
    PAYPAL_VERIFY(Request.Method.POST, "paypal_execute/"),
    FREQUENT_AIRPORT(Request.Method.POST, "mobile/frequentAirport/"),
    MY_FREQUENT_AIRPORT(Request.Method.GET, "mobile/frequentAirport/"),
    MAX_SEATS(Request.Method.GET, "max-seats/"),
    ERASE_MY_FREQUENT_AIRPORT(Request.Method.DELETE, "mobile/frequentAirport/");

    //Method Type Volley  GET=0,POST=1,PUT=2,DELETE=3
    //MAIN URL

//    public final static String MAIN_URL = "http://192.168.0.19:8000/";
    //  public final static String MAIN_URL= "http://192.168.0.19:8080/";
      public final static String MAIN_URL = "https://dev-api.tuvuelovip.com/";
//    public final static String MAIN_URL = "http://198.211.99.79/";

    public final static String IMAGE = MAIN_URL + "image/";
    private Integer mTYPE;
    private String mFINAL;

    URL(Integer TYPE, String FINAL) {
        this.mTYPE = TYPE;
        this.mFINAL = (MAIN_URL + FINAL);
    }

    public String getURL() {
        return mFINAL;
    }

    public int getRequestType() {
        return mTYPE;
    }

}
