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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fourgeeks.tuvuelovip.pasajero.util.Util;

/**
 * Created by Carlos_Lopez on 1/9/17.
 */

public class FlightRequest implements Parcelable, Serializable {


    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("outAirport_id")
    @Expose
    public int outAirport_id;

    @SerializedName("enterAirport_id")
    @Expose
    public int enterAirport_id;

    @SerializedName("outDate")
    @Expose
    public Date outDate;

    @SerializedName("seats")
    @Expose
    public int seats;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("num_responses")
    @Expose
    public int num_responses;


    @SerializedName("journey")
    @Expose
    public List<Journey> journeys;

    @SerializedName("responses")
    @Expose
    public List<Flight> responses;

    @Deprecated
    public String inAirportName;
    @Deprecated
    public String outAirportName;

    public FlightRequest() {

    }

    protected FlightRequest(Parcel in) {
        outAirport_id = in.readInt();
        description = in.readString();
        seats = in.readInt();
        enterAirport_id = in.readInt();
        inAirportName = in.readString();
        outAirportName = in.readString();
        id = in.readInt();
    }

    public static final Creator<FlightRequest> CREATOR = new Creator<FlightRequest>() {
        @Override
        public FlightRequest createFromParcel(Parcel in) {
            return new FlightRequest(in);
        }

        @Override
        public FlightRequest[] newArray(int size) {
            return new FlightRequest[size];
        }
    };

    public int getOutAirport_id() {
        return outAirport_id;
    }

    public void setOutAirport_id(int outAirport_id) {
        this.outAirport_id = outAirport_id;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getEnterAirport_id() {
        return enterAirport_id;
    }

    public void setEnterAirport_id(int enterAirport_id) {
        this.enterAirport_id = enterAirport_id;
    }

    @Override
    public String toString() {
        return "FlightRequest: [outAirport_id = " + outAirport_id + ", description = " + description + ", Journey = " + journeys + ", outDate = " + outDate + ", seats = " + seats + ", enterAirport_id = " + enterAirport_id + "]";
    }

    public static FlightRequest fromJsonObject(JSONObject object) throws JSONException, ParseException {
        FlightRequest request = new FlightRequest();
        request.id = object.getInt("id");
        request.outAirport_id = object.optInt("outAirport_id");
        request.enterAirport_id = object.optInt("enterAirport_id");
        String outDate = object.optString("outDate", null);
        if (outDate != null)
            if (!outDate.equals("null"))
                request.outDate = Util.getDjangoDateFormatWithOutZ().parse(outDate);
        request.seats = object.getInt("seats");
        JSONArray journeys = object.optJSONArray("journey");
        if (journeys != null) {
            for (int i = 0, j = journeys.length(); i < j; i++) {
                Journey journey = Journey.fromJsonObject(journeys.getJSONObject(i));
                request.addJourney(journey);
            }
        }
        request.num_responses = object.getInt("num_responses");

        JSONArray responses = object.optJSONArray("responses");
        if (responses != null) {
            for (int i = 0, j = responses.length(); i < j; i++) {
                Flight flight = Flight.fromJsonObject2(responses.getJSONObject(i));
                request.addResponse(flight);
            }
        }

        return request;
    }

    public static List<FlightRequest> fromJsonArray(JSONArray array) throws JSONException, ParseException {
        List<FlightRequest> list = new ArrayList<>();
        for (int i = 0, j = array.length(); i < j; i++)
            list.add(fromJsonObject(array.getJSONObject(i)));
        return list;
    }

    public void addJourney(Journey journey) {
        if(journeys == null)
            journeys = new ArrayList<>();
        journeys.add(journey);
    }

    public void addResponse(Flight flight) {
        responses.add(flight);
    }

//    public Journey toJourney() {
//        Journey journey = new Journey();
//        if (this.enterAirport_id != null)
//            journey.inAirportId = Integer.valueOf(this.enterAirport_id);
//        if (this.outAirport_id != null)
//            journey.outAirportId = Integer.valueOf(this.outAirport_id);
//        journey.outDate = this.outDate;
//
//        return journey;
//    }

    public JSONObject toJSON() {
        JSONObject requestAsJson = new JSONObject();

        if (id != 0)
            try {
                requestAsJson.put("id", id);
            } catch (JSONException e) {
                Log.e("FLIGHTREQUEST", "toJSON:id:" + e.getLocalizedMessage());
                return null;
            }

        try {
            requestAsJson.put("outAirport_id", getOutAirport_id());
        } catch (JSONException e) {
            Log.e("FLIGHTREQUEST", "toJSON:outAirport_id:" + e.getLocalizedMessage());
            return null;
        }
        try {
            requestAsJson.put("enterAirport_id", getEnterAirport_id());
        } catch (JSONException e) {
            Log.e("FLIGHTREQUEST", "toJSON:enterAirport_id:" + e.getLocalizedMessage());
            return null;
        }
        try {
            requestAsJson.put("seats", getSeats());
        } catch (JSONException e) {
            Log.e("FLIGHTREQUEST", "toJSON:seats" + e.getLocalizedMessage());
            return null;
        }
        SimpleDateFormat djangoDateFormat = Util.getDjangoDateFormatWithOutZ();
        try {
            requestAsJson.put("outDate", djangoDateFormat.format(outDate));
        } catch (JSONException e) {
            Log.e("FLIGHTREQUEST", "toJSON:outDate:JSONException:" + e.getLocalizedMessage());
            return null;
        }
        // Journeys
        JSONArray jsonJourneys = new JSONArray();
        if (journeys != null)
            for (Journey journey : journeys)
                jsonJourneys.put(journey.toJson());

        if (jsonJourneys.length() > 0)
            try {
                requestAsJson.put("journey", jsonJourneys);
            } catch (JSONException e) {
                Log.e("FLIGHTREQUEST", "toJSON:journeys:" + e.getLocalizedMessage());
            }

        return requestAsJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(outAirport_id);
        parcel.writeString(description);
        parcel.writeInt(seats);
        parcel.writeInt(enterAirport_id);
        parcel.writeString(inAirportName);
        parcel.writeString(outAirportName);
        parcel.writeInt(id);
    }

    public Journey toJourney() {
        Journey journey = new Journey();
        journey.outAirportId = outAirport_id;
        journey.inAirportId = enterAirport_id;
        journey.outDate = outDate;
        journey.seats = seats;
        return journey;
    }
}
