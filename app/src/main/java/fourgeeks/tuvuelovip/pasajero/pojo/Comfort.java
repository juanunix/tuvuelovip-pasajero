package fourgeeks.tuvuelovip.pasajero.pojo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlos_Lopez on 12/7/16.
 */

public class Comfort {

    public int id;
    public String name;
    public String image;

    public static Comfort fromJsonObject(JSONObject object) throws JSONException {
        Comfort comfort = new Comfort();
        comfort.id = object.getInt("id");
        comfort.name = object.getString("name");
        comfort.image = object.getString("image");
        return comfort;
    }
}
