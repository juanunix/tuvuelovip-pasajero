package fourgeeks.tuvuelovip.pasajero.api.retrofit;

import fourgeeks.tuvuelovip.pasajero.api.URL;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by antonio on 02/02/17.
 */

public class CharterApi
{
    protected Retrofit retrofit;
    protected RestRequest service;

    public RestRequest getInstance()
    {
        if(service == null)
        {
            retrofit = new Retrofit.Builder().
                    baseUrl(URL.MAIN_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();

            service = retrofit.create(RestRequest.class);
            return service;
        }

        return service;

    }
}
