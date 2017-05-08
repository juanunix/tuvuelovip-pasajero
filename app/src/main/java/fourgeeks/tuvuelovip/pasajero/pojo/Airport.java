package fourgeeks.tuvuelovip.pasajero.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos_Lopez on 1/5/17.
 */

public class Airport implements Parcelable,Serializable {

    //Basic Airport
    public int id;
    public String iata;
    public String oaci;
    public String name;

    //City Key
    public int cityId;
    public String cityName;

    //Country Key
    public int countryId;
    public String countryName;


    public Airport() {

    }

    protected Airport(Parcel in) {
        id = in.readInt();
        iata = in.readString();
        oaci = in.readString();
        name = in.readString();
        cityId = in.readInt();
        cityName = in.readString();
        countryId = in.readInt();
        countryName = in.readString();
    }

    public static final Creator<Airport> CREATOR = new Creator<Airport>() {
        @Override
        public Airport createFromParcel(Parcel in) {
            return new Airport(in);
        }

        @Override
        public Airport[] newArray(int size) {
            return new Airport[size];
        }
    };

    public static Airport fromJsonObject(JSONObject object) throws JSONException {

        //Airport Instance
        Airport airport =new Airport();
        //Basic Airport
        airport.id = object.optInt("id");
        airport.iata = object.getString("iata");
        airport.oaci = object.getString("oaci");
        airport.name = object.getString("name");
        //City
        if(object.getJSONObject("city") != null)
        {
            JSONObject city = object.getJSONObject("city");
            airport.cityId = city.optInt("id");
            airport.cityName = city.optString("name");
            if(city.getJSONObject("country") != null)
            {
                JSONObject country = city.getJSONObject("country");
                airport.countryId = country.optInt("id");
                airport.countryName = country.optString("name");
            }
        }

        return airport;

    }

    public String getIata() {
        return iata;
    }
    public String getCityName() {
        return cityName;
    }

    public static List<Airport> fromJsonArray(JSONArray array) throws JSONException {
        List<Airport> airports = new ArrayList<>();
        for(int i = 0, j = array.length(); i < j; i++)
            airports.add(fromJsonObject(array.getJSONObject(i)));
        return  airports;
    }

    public String toString(){
        return name + " (" + iata + "), " + cityName + ", " + countryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(iata);
        parcel.writeString(oaci);
        parcel.writeString(name);
        parcel.writeInt(cityId);
        parcel.writeString(cityName);
        parcel.writeInt(countryId);
        parcel.writeString(countryName);
    }

    public int getId() {
        return id;
    }
}
