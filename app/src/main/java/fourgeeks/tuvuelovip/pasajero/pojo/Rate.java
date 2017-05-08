package fourgeeks.tuvuelovip.pasajero.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alacret on 4/17/17.
 */

public class Rate {

    public Rate() {
    }

    public Rate(int rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public Rate(String review) {
        this.review = review;
    }

    @SerializedName("rating")
    @Expose
    public int rating;

    @SerializedName("review")
    @Expose
    public String review;
}
