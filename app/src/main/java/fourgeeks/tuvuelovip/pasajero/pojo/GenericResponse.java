package fourgeeks.tuvuelovip.pasajero.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alacret on 4/17/17.
 */

public class GenericResponse {

    @SerializedName("accepted")
    @Expose
    public boolean accepted;
}
