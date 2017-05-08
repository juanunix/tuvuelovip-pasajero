package fourgeeks.tuvuelovip.pasajero.login;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leosantana on 26/04/17.
 */

public class RetrofitFacebookClient {

    private static Retrofit retro = null;

    public static Retrofit sendData(String base){
         if(retro==null){
             retro = new Retrofit.Builder()
                     .baseUrl(base)
                     .addConverterFactory(GsonConverterFactory.create())
                     .build();
         }
         return retro;
    }
}
