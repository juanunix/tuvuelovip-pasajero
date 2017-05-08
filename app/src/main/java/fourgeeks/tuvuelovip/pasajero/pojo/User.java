package fourgeeks.tuvuelovip.pasajero.pojo;

/**
 * Created by alacret on 3/13/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * User Pojo
 */
public class User {
    /**
     * Creates an empty User
     */
    public User(){}
    /**
     * Create a new User
     *
     * @param password
     * @param countryId
     * @param last_name
     * @param firstName
     * @param username
     * @param email
     * @param dni
     */
    public User(String password, int countryId, String last_name, String firstName, String username, String email, String dni, String passport) {
        this.password = password;
        this.countryId = countryId;
        this.last_name = last_name;
        this.firstName = firstName;
        this.username = username;
        this.email = email;
        this.dni = dni;
        this.passport = passport;
    }

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("first_name")
    @Expose
    public String firstName;

    @SerializedName("last_name")
    @Expose
    public String last_name;

    @SerializedName("country_id")
    @Expose
    public int countryId;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("passport")
    @Expose
    public String passport;

    @SerializedName("dni")
    @Expose
    public String dni;

    @SerializedName("os")
    @Expose
    public String os;

    @SerializedName("device_id")
    @Expose
    public String deviceId;

}
