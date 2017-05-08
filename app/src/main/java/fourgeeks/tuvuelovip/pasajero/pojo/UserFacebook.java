package fourgeeks.tuvuelovip.pasajero.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by leosantana on 05/05/17.
 */




public class UserFacebook {
        /**
         * Creates an empty UserFacebook
         */
        public UserFacebook(){}
        /**
         * Create a new UserFacebook
         *
         * @param countryId
         * @param last_name
         * @param firstName
         * @param username
         * @param email
         * @param dni
         * @param facebook_token
         * @param userId
         */
        public UserFacebook(int countryId, String last_name, String firstName, String username, String email, String dni, String passport,
                            String facebook_token, String userId) {
            this.countryId = countryId;
            this.last_name = last_name;
            this.firstName = firstName;
            this.username = username;
            this.email = email;
            this.dni = dni;
            this.passport = passport;
            this.facebookAccessToken=facebook_token;
            this.facebookId=userId;
        }

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("username")
        @Expose
        public String username;

        @SerializedName("first_name")
        @Expose
        public String firstName;

        @SerializedName("last_name")
        @Expose
        public String last_name;

        @SerializedName("country_id")
        @Expose
        public int countryId;

        @SerializedName("passport")
        @Expose
        public String passport;

        @SerializedName("dni")
        @Expose
        public String dni;

        @SerializedName("os")
        @Expose
        public String os;

        @SerializedName("device_id")
        @Expose
        public String deviceId;

        @SerializedName("facebook_access_token")
        @Expose
        public String facebookAccessToken;

        @SerializedName("facebook_id")
        @Expose
        public String facebookId;

    }


