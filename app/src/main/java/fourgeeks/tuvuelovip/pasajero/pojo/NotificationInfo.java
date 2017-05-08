package fourgeeks.tuvuelovip.pasajero.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alacret on 4/17/17.
 */

public class NotificationInfo {

    public NotificationInfo(){}

    public NotificationInfo(boolean state){
        push = state;
    }

    @SerializedName("id")
    @Expose
    public long id;

    @SerializedName("push")
    @Expose
    public boolean push;
}
