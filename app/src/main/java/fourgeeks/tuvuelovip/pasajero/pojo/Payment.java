package fourgeeks.tuvuelovip.pasajero.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alacret on 3/14/17.
 */

public class Payment {
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("paymentId")
    @Expose
    public String paymentId;
    @SerializedName("paymentMethod")
    @Expose
    public String paymentMethod;
    @SerializedName("date")
    @Expose
    public String date;
}
