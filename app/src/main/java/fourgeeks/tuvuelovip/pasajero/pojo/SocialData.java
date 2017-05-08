package fourgeeks.tuvuelovip.pasajero.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by leosantana on 26/04/17.
 */

public class SocialData {
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("registered")
    @Expose
    public Boolean registered;
    @SerializedName("Token")
    @Expose
    public String Token;

    public SocialData(){}

    public SocialData(String mClientId, String mCode){

        //clientId = mClientId;
        //code=mCode;
    }
    public void setClientId(String mClientId){
        user_id = mClientId;
    }
    public String getUser_id(){
          return user_id;
    }
    public void setCode(String mCode){
        access_token=mCode;
    }
    public String getAccess_token(){
        return access_token;
    }

    public boolean getRegistered(){
        return registered;
    }
    public String getToken(){
        return Token;
    }



}
