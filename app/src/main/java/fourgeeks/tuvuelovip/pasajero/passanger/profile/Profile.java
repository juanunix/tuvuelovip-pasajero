package fourgeeks.tuvuelovip.pasajero.passanger.profile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlos_Lopez on 12/4/16.
 */

public class Profile {
    public int id;
    public String firstName;
    public String lastName;
    public String userName;

    public String image;
    public String email;
    public String passport;

    public String userType;
    public String contactPhone;
    public String contactPhone2;

    public static Profile fromJsonObject(JSONObject object) throws JSONException {
        Profile profile = new Profile();
        profile.id = object.getInt("id");
        profile.firstName = object.getString("first_name");
        profile.lastName = object.getString("last_name");
        profile.userName = object.getString("username");
        profile.image = object.getString("image64");
        profile.email = object.getString("email");
        profile.userType = object.getString("userType");
        profile.contactPhone = object.optString("contactPhone", "");
        profile.passport = object.optString("passport", "");
        return profile;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject profileAsJson = new JSONObject();
        profileAsJson.put("first_name", firstName);
        profileAsJson.put("last_name", lastName);
        profileAsJson.put("email", email);
        profileAsJson.put("contactPhone", contactPhone);
        profileAsJson.put("passport", passport);
        profileAsJson.put("image64", image);
        return profileAsJson;
    }

    public static Profile contactFromJsonObject(JSONObject object) throws JSONException {
        Profile profile = new Profile();
        profile.id = object.getInt("id");
        profile.firstName = object.getString("name");
        //profile.email = object.getString("email");
        profile.contactPhone = object.optString("phone", "");
        return profile;
    }

    public JSONObject contactToJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", firstName);
        json.put("phone", contactPhone);
        //json.put("phone2", contactPhone2);
        return json;
    }

    public void pasteProfile(Profile profile) {
        this.id = profile.id;
        this.image = profile.image;
        this.firstName = profile.firstName;
        this.lastName = profile.lastName;
        this.email = profile.email;
        this.contactPhone = profile.contactPhone;
        this.passport = profile.passport;
    }

}