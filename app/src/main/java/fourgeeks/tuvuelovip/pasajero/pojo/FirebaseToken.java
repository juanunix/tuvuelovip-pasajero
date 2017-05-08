package fourgeeks.tuvuelovip.pasajero.pojo;

/**
 * Created by alacret on 3/13/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * FirebaseToken Pojo
 */
public class FirebaseToken {
    /**
     * Creates an empty Token
     */
    public FirebaseToken(){
    }


    public FirebaseToken(String oldToken, String token){
        this.oldToken = oldToken;
        this.token = token;
    }


    public FirebaseToken(String token){
        this.token = token;
    }

    @SerializedName("old_registration_id")
    @Expose
    public String oldToken;

    @SerializedName("registration_id")
    @Expose
    public String token;



}
