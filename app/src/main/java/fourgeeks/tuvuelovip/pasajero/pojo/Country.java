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
 * Created by Carlos_Lopez on 1/6/17.
 */

public class Country implements Parcelable,Serializable {

    public Integer id;
    public String name;
    public Country(int id, String name){
        this.id = id;
        this.name = name;
    }

    protected Country(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public static Country fromJsonObject(JSONObject object) throws JSONException {
        int id = object.getInt("id");
        String name = object.getString("name");
        return new Country(id,name);
    }

    public static List<Country> fromJsonArray(JSONArray array) throws JSONException {
        List<Country> countries = new ArrayList<>();
        for(int i = 0, j = array.length(); i < j; i++)
            countries.add(fromJsonObject(array.getJSONObject(i)));
        return  countries;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
