package fourgeeks.tuvuelovip.pasajero.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fourgeeks.tuvuelovip.pasajero.util.Util;


public class Journey implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("outAirport_id")
    @Expose
    public int outAirportId;
    @SerializedName("outDate")
    @Expose
    public Date outDate;
    @SerializedName("enterAirport_id")
    @Expose
    public int inAirportId;
    public String outAirportIata,
            outAirportCityName, inAirportIata, inAirportCityName;
    @SerializedName("seats")
    @Expose
    public int seats;

    public Journey() {
    }

    public Journey(Parcel in) {
    }

    @Override
    public String toString() {
        return "Journey [outAirport_id = " + outAirportId + ", outDate = " + outDate + ", enterAirport_id = " + inAirportId + "]";
    }

    public JSONObject toJson() {
        JSONObject journeyAsJson = new JSONObject();
        try {
            journeyAsJson.put("outAirport_id", outAirportId);
        } catch (JSONException e) {
            Log.e("JOURNEY", "toJSON:outAirport_id:" + e.getLocalizedMessage());
            return null;
        }
        try {
            journeyAsJson.put("enterAirport_id", inAirportId);
        } catch (JSONException e) {
            Log.e("JOURNEY", "toJSON:enterAirport_id:" + e.getLocalizedMessage());
            return null;
        }
        SimpleDateFormat djangoDateFormat = Util.getDjangoDateFormatWithOutZ();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            journeyAsJson.put("outDate", djangoDateFormat.format(outDate));
        } catch (JSONException e) {
            Log.e("JOURNEY", "toJSON:outDate:JSONException:" + e.getLocalizedMessage());
            return null;
        }
        //HACK mientras richard arregla este peo
        try {
            journeyAsJson.put("seats", 0);
        } catch (JSONException e) {
            Log.e("JOURNEY", "toJSON:enterAirport_id:" + e.getLocalizedMessage());
            return null;
        }
        return journeyAsJson;
    }

    public static Journey fromJsonObject(JSONObject object) throws JSONException {
        Journey journey = new Journey();

        journey.outAirportId = object.getInt("outAirport_id");
        String outDate = object.getString("outDate");
        SimpleDateFormat djangoDateFormat = Util.getDjangoDateFormatWithOutZ();
        try {
            journey.outDate = djangoDateFormat.parse(outDate);
        } catch (ParseException e) {
            Log.e("Journey", "Imposible parsear la fecha del server: " + outDate);
        }
        journey.inAirportId = object.getInt("enterAirport_id");


        return journey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(outDate);
        parcel.writeInt(outAirportId);
        parcel.writeString(outAirportIata);
        parcel.writeString(outAirportCityName);
        parcel.writeInt(inAirportId);
        parcel.writeString(inAirportIata);
        parcel.writeString(inAirportCityName);
        parcel.writeInt(seats);
    }

    public static final Parcelable.Creator<Journey> CREATOR
            = new Parcelable.Creator<Journey>() {

        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };
}
