package fourgeeks.tuvuelovip.pasajero.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.util.Util;

/**
 * Created by Carlos_Lopez on 1/6/17.
 */

public class Flight implements Parcelable, Serializable {

    public static final String TAG = Flight.class.getSimpleName();
    @SerializedName("payment")
    @Expose
    public Payment payment;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("enterAirport_id")
    @Expose
    public int enterAirportId;
    @SerializedName("seats")
    @Expose
    public String seats;
    @SerializedName("outAirport_id")
    @Expose
    public int outAirportId;

    @SerializedName("modelAirplane")
    @Expose
    public String modelAirplane;
    //PUBLIC
    public String imageUrl;
    @SerializedName("outDate")
    @Expose
    public String outDate;
    public Date outDateDate;
    @SerializedName("type_flight")
    @Expose
    public String type;
    public int year;
    public String classAirplane;
    @SerializedName("observations")
    @Expose
    public String observations;
    //Pilot
    @SerializedName("licence")
    @Expose
    public Date licence;

    @SerializedName("certificate")
    @Expose
    public Date certificate;

    @SerializedName("age_pilot")
    @Expose
    public String age;
    @SerializedName("flight_hours")
    @Expose
    public String flightHours;
    //
    @SerializedName("terms")
    @Expose
    public String terms;
    @SerializedName("journey")
    @Expose
    public List<Journey> journeys;
    @SerializedName("comforts")
    @Expose
    public List<Comfort> comforts;

    @SerializedName("disponibility")
    @Expose
    public String disponibility;

    @SerializedName("disponibility_code")
    @Expose
    public String disponibilityCode;

    @SerializedName("disponibility_passenger")
    @Expose
    public String disponibilityPassenger;

    @SerializedName("disponibility_pilot")
    @Expose
    public String disponibilityPilot;

    public String payment_method;
    public String payment_date;

    @SerializedName("images")
    @Expose
    public List<String> images;
    @SerializedName("images1")
    @Expose
    public List<String> images1;
    @SerializedName("images2")
    @Expose
    public List<String> images2;

    @SerializedName("status")
    @Expose
    public String status;

    public Flight() {
    }

    protected Flight(Parcel in) {
        id = in.readString();
        price = in.readString();
        enterAirportId = in.readInt();
        outDate = in.readString();
        seats = in.readString();
        outAirportId = in.readInt();
        modelAirplane = in.readString();
        imageUrl = in.readString();
        type = in.readString();
        disponibility = in.readString();
        payment_method = in.readString();
        payment_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(price);
        parcel.writeInt(enterAirportId);
        parcel.writeString(outDate);
        parcel.writeString(seats);
        parcel.writeInt(outAirportId);
        parcel.writeString(modelAirplane);
        parcel.writeString(imageUrl);
        parcel.writeString(type);
        parcel.writeString(disponibility);
        parcel.writeString(payment_method);
        parcel.writeString(payment_date);
    }


    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }


    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getModelAirplane() {
        return modelAirplane;
    }
    //Pilot
    public String getPilotAge() { return age; }
    public String getPilotFlightHours() { return flightHours; }
    //

    public void setModelAirplane(String modelAirplane) {
        this.modelAirplane = modelAirplane;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", price = " + price + ", enterAirportId = " + enterAirportId + ", outIata +  outDate = " + outDate + ", seats = " + seats + ", outAirportId = " + outAirportId + ", modelAirplane = " + modelAirplane + "]";
    }

    public static Flight fromJsonObjectCOMPRADO(JSONObject object) throws JSONException {
        Flight flight = new Flight();

        flight.id = String.valueOf(object.getInt("id"));
        flight.outAirportId = object.optInt("outAirport_id");
        flight.enterAirportId = object.optInt("enterAirport_id");
        flight.modelAirplane = object.getString("modelAirplane");
        flight.seats = object.getString("seats");
        flight.price = object.getString("price");
        flight.outDate = object.getString("outDate");

        flight.year = object.optInt("year", 0);
        flight.classAirplane = object.optString("classAirplane", null);
        flight.observations = object.optString("observations", null);
        flight.terms = object.optString("terms", null);

        //PAYMENT INFORMATION
        flight.disponibility = object.optString("disponibility", null);
        //flight.payment_date =  object.getJSONObject("payment").getString("date");
        //flight.payment_method =  object.getJSONObject("payment").getString("paymentMethod");
        List<Journey> journeys = new ArrayList<>();

        try {
            JSONArray journey = object.getJSONArray("journey");
            if (journey.length() > 0) {
                for (int i = 0, j = journey.length(); i < j; i++) {
                    JSONObject o = journey.getJSONObject(i);
                    journeys.add(Journey.fromJsonObject(o));
                }
            }

        } catch (JSONException e) {
            Log.d("Flight", "fromJsonObject: No journeys! really?");
        }
        flight.journeys = journeys;

        List<Comfort> comforts = new ArrayList<>();
        try {
            JSONArray jsonComforts = object.getJSONArray("comforts");
            if (jsonComforts.length() > 0) {
                for (int i = 0, j = jsonComforts.length(); i < j; i++) {
                    JSONObject o = jsonComforts.getJSONObject(i);
                    comforts.add(Comfort.fromJsonObject(o));
                }
            }

        } catch (JSONException e) {
            Log.d("Flight", "fromJsonObject: No comforts! really?");
        }
        flight.comforts = comforts;

        try {
            flight.imageUrl = object.getString("image");
        } catch (JSONException e) {
            try {
                JSONArray array = object.getJSONArray("images");
                flight.images = new ArrayList<>(array.length());
                for(int i = 0, j = array.length(); i < j; i++)
                    flight.images.add(array.getString(i));

                if (array.length() > 0) {
                    flight.imageUrl = array.getString(0);
                }
            } catch (JSONException e1) {
                Log.d("Flight", "fromJsonObject: No image available");
            }
        }

        flight.type = object.getString("type_flight");
        return flight;
    }

    public static Flight fromJsonObject(JSONObject object) throws JSONException {

        Flight flight = new Flight();
        flight.id = String.valueOf(object.getInt("id"));
        flight.outAirportId = object.optInt("outAirport_id");
        flight.enterAirportId = object.optInt("enterAirport_id");
        flight.modelAirplane = object.getString("modelAirplane");
        flight.seats = object.getString("seats");
        flight.price = object.getString("price");
        flight.outDate = object.getString("outDate");
//
        flight.disponibility = object.optString("disponibility", null);
        flight.year = object.optInt("year", 0);
        flight.classAirplane = object.optString("classAirplane", null);
        flight.observations = object.optString("observations", null);
        flight.terms = object.optString("terms", null);
        flight.disponibilityCode = object.optString("disponibility_code");
        //Pilot
        //Certificate
        String certificateAsString = object.optString("certificate", null);
        if(certificateAsString != null){
            try {
                flight.certificate = Util.getDjangoDateFormatWithOutZ().parse(certificateAsString);
            } catch (ParseException e) {
                Log.e(TAG,"Parse1:fromJsonObject:" + e.getLocalizedMessage());
                try {
                    flight.certificate = Util.getDjangoDateFormatWithMilisenconds().parse(certificateAsString);
                } catch (ParseException e1) {
                    Log.e(TAG,"Parse2:fromJsonObject:" + e1.getLocalizedMessage());
                }
            }
        }
        //Licence
        String licenceAsString = object.optString("licence", null);
        if(licenceAsString != null){
            try {
                flight.licence = Util.getDjangoDateFormatWithOutZ().parse(licenceAsString);
            } catch (ParseException e) {
                Log.e(TAG,"fromJsonObject:" + e.getLocalizedMessage());
                try {
                    flight.licence = Util.getDjangoDateFormatWithMilisenconds().parse(licenceAsString);
                } catch (ParseException e1) {
                    Log.e(TAG,"fromJsonObject:" + e1.getLocalizedMessage());
                }
            }
        }

        flight.age = object.getString("age_pilot");
        flight.flightHours = object.getString("flight_hours");
        //Pilot

        List<Journey> journeys = new ArrayList<>();
        try {
            JSONArray journey = object.getJSONArray("journey");
            if (journey.length() > 0) {
                for (int i = 0, j = journey.length(); i < j; i++) {
                    JSONObject o = journey.getJSONObject(i);
                    journeys.add(Journey.fromJsonObject(o));
                }
            }

        } catch (JSONException e) {
            Log.d("Flight", "fromJsonObject: No journeys! really?");
        }
        flight.journeys = journeys;

        List<Comfort> comforts = new ArrayList<>();
        try {
            JSONArray jsonComforts = object.getJSONArray("comforts");
            if (jsonComforts.length() > 0) {
                for (int i = 0, j = jsonComforts.length(); i < j; i++) {
                    JSONObject o = jsonComforts.getJSONObject(i);
                    comforts.add(Comfort.fromJsonObject(o));
                }
            }

        } catch (JSONException e) {
            Log.d("Flight", "fromJsonObject: No comforts! really?");
        }
        flight.comforts = comforts;

        try {
            flight.imageUrl = object.getString("image");
        } catch (JSONException e) {
            try {
                JSONArray array = object.getJSONArray("images");
                flight.images = new ArrayList<>(array.length());
                for(int i = 0, j = array.length(); i < j; i++)
                    flight.images.add(array.getString(i));

                if (array.length() > 0) {
                    flight.imageUrl = array.getString(0);
                }
            } catch (JSONException e1) {
                Log.d("Flight", "fromJsonObject: No image available");
            }
        }

        flight.type = object.getString("type_flight");
        return flight;
    }

    public static Flight fromJsonObject2(JSONObject object) throws JSONException {
        Flight flight = new Flight();
        flight.id = String.valueOf(object.getInt("id"));
        flight.outAirportId = object.optInt("outAirport_id");
        flight.enterAirportId = object.optInt("enterAirport_id");
        String outDate = object.optString("outDate");
        if (outDate != null) {
            try {
                flight.outDateDate = Util.getDjangoDateFormatWithOutZ().parse(outDate);
            } catch (ParseException e) {
                Log.e("Flight", "fromJsonObject2:ParseException:" + e.getLocalizedMessage());
            }
        }
        flight.seats = object.getString("seats");
        flight.price = object.getString("price");
        return flight;
    }

    public static List<Flight> fromJsonArray(JSONArray array) throws JSONException {
        List<Flight> list = new ArrayList<>();
        for (int i = 0, j = array.length(); i < j; i++)
            list.add(fromJsonObject(array.getJSONObject(i)));
        return list;
    }

    public static List<Flight> fromJsonArrayCOMPRADO(JSONArray array) throws JSONException {
        List<Flight> list = new ArrayList<>();
        for (int i = 0, j = array.length(); i < j; i++)
            list.add(fromJsonObjectCOMPRADO(array.getJSONObject(i)));
        return list;
    }

}