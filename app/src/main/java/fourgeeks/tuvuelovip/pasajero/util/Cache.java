package fourgeeks.tuvuelovip.pasajero.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.BuildConfig;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.pojo.Airport;
import fourgeeks.tuvuelovip.pasajero.pojo.Country;
import fourgeeks.tuvuelovip.pasajero.pojo.Flight;
import fourgeeks.tuvuelovip.pasajero.pojo.FlightRequest;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 1/28/17.
 */

public final class Cache {

    public static final String TAG = "CACHE";
    public static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String SESSION_ACTIVE = "SESSION_ACTIVE";
    public static final String INSTALL = "INSTALL";
    public static final String TERMS = "TERMS";
    private static SharedPreferences preferences;

    /**
     * For initialization
     *
     * @param context
     */

    public static void init(Context context) {
        Log.d(TAG, "init:");
        preferences = context.getSharedPreferences("CHARTER", Context.MODE_PRIVATE);
        try {
            Reservoir.init(context, 10000000); //in bytes
        } catch (Exception e) {
            Log.e(TAG, "init:Exception:" + e.getLocalizedMessage());
        }
    }

    public static void preloadCache(){
        try {
            getAirports();
            getCountries();
            buildRetrofit();
        } catch (IOException e) {
            Log.e(TAG, "init:Exception on preloading:" + e.getLocalizedMessage());
        }
    }

    public static void clearCache(){
        try {
            Reservoir.clear();
        } catch (IOException e) {
            Log.e(TAG, "clearCache:" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        preferences.edit().clear().commit();
        retrofit = null;
        authRetrofit = null;
    }

    private Cache() {
        throw new IllegalAccessError("Don't");
    }

    private static final String AIRPORTS = "AIRPORTS";
    private static final String COUNTRIES = "COUNTRIES";
    private static final String MAX_SEATS = "MAX-SEATS";
    private static final String MY_FLIGHTS = "MY-FLIGHTS";
    private static final String FLIGHTS = "FLIGHTS";
    private static final String MY_FLIGHT_REQUESTS = "MY-FLIGHT-REQUESTS";

    //    public static void getAirportsAsync(ReservoirGetCallback<List<Airport>> callback) {
//        Type resultType = new TypeToken<List<Airport>>() {
//        }.getType();
//        Reservoir.getAsync(AIRPORTS, resultType, callback);
//    }
    private static List<Airport> airportList;

    /**
     * Get the aiports on Cache
     *
     * @return
     * @throws IOException
     */
    public static synchronized List<Airport> getAirports() throws IOException {
        if (airportList == null) {
            Type resultType = new TypeToken<List<Airport>>() {
            }.getType();
            airportList = Reservoir.get(AIRPORTS, resultType);
        }
        return airportList;
    }

    //    public static void getCountriesAsync(ReservoirGetCallback<List<Country>> callback) {
//        Type resultType = new TypeToken<List<Country>>() {
//        }.getType();
//        Reservoir.getAsync(COUNTRIES, resultType, callback);
//    }
    private static List<Country> countriesList;

    /**
     * Returns the countries in cache
     *
     * @return
     * @throws IOException
     */
    public static synchronized List<Country> getCountries() throws IOException {
        if (countriesList == null) {
            Type resultType = new TypeToken<List<Country>>() {
            }.getType();
            countriesList = Reservoir.get(COUNTRIES, resultType);
        }
        return countriesList;
    }

    public static int getMaxSeats() throws IOException {
        Type resultType = new TypeToken<Integer>() {
        }.getType();
        return Reservoir.get(MAX_SEATS, resultType);
    }

    public static void setNeedToReloadFlightRequest(boolean value) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("NeedToReloadFlightRequest", value).commit();
    }

    public static void setNeedToReloadMyFlights(boolean value) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("NeedToReloadMyFlights", value).commit();
    }

    public static Set<String> getListOfFlighRequestViewed() {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return null;
        }
        return preferences.getStringSet("ListOfFlighRequestViewed", new HashSet<String>());
    }

    public static void addFlighRequestViewed(int id) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        Set<String> listOfFlighRequestViewed = preferences.getStringSet("ListOfFlighRequestViewed", null);
        if (listOfFlighRequestViewed == null) {
            listOfFlighRequestViewed = new HashSet<>();
        }
        listOfFlighRequestViewed.add(String.valueOf(id));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("ListOfFlighRequestViewed", listOfFlighRequestViewed).commit();
    }

    /**
     * store my Flights in Cache
     * @param myFlights my flights
     */
    public static void setMyFlights(List<Flight> myFlights) {
        Log.d(TAG, "setMyFlights");
        Reservoir.putAsync(MY_FLIGHTS, myFlights, new ReservoirPutCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "setMyFlights:putAsync:onSuccess");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "setMyFlights:putAsync:onFailure:" + e.getLocalizedMessage());
            }
        });
    }

    /**
     * store the Flights in Cache
     * @param flights flights
     */
    public static void setFlights(List<Flight> flights) {
        Log.d(TAG, "setFlights");
        Reservoir.putAsync(FLIGHTS, flights, new ReservoirPutCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "setFlights:putAsync:onSuccess");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "setFlights:putAsync:onFailure:" + e.getLocalizedMessage());
            }
        });
    }

    /**
     * get My Flights from Cache
     * @return my Flights
     */
    public static Single<List<Flight>> getMyFlights() {
        Single<List<Flight>> single = Single.create(new Single.OnSubscribe<List<Flight>>() {

            @Override
            public void call(final SingleSubscriber<? super List<Flight>> singleSubscriber) {
                Type resultType = new TypeToken<List<Flight>>() {
                }.getType();
                ReservoirGetCallback<List<Flight>> callback = new ReservoirGetCallback<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> flights) {
                        singleSubscriber.onSuccess(flights);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        singleSubscriber.onError(e);
                    }
                };

                Reservoir.getAsync(MY_FLIGHTS, resultType, callback);
            }
        });
        return single.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * get the Flights from Cache
     * @return the Flights
     */
    public static Single<List<Flight>> getFlights() {
        Single<List<Flight>> single = Single.create(new Single.OnSubscribe<List<Flight>>() {

            @Override
            public void call(final SingleSubscriber<? super List<Flight>> singleSubscriber) {
                Type resultType = new TypeToken<List<Flight>>() {
                }.getType();
                ReservoirGetCallback<List<Flight>> callback = new ReservoirGetCallback<List<Flight>>() {
                    @Override
                    public void onSuccess(List<Flight> flights) {
                        singleSubscriber.onSuccess(flights);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        singleSubscriber.onError(e);
                    }
                };

                Reservoir.getAsync(FLIGHTS, resultType, callback);
            }
        });
        return single.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static void setMyFlightRequests(List<FlightRequest> myFlightRequests) {
        Log.d(TAG, "setMyFlightRequests");
        Reservoir.putAsync(MY_FLIGHT_REQUESTS, myFlightRequests, new ReservoirPutCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "setMyFlightRequests:putAsync:onSuccess");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "setMyFlightRequests:putAsync:onFailure:" + e.getLocalizedMessage());
            }
        });
    }

    public static Single<List<FlightRequest>> getMyFlightRequests() {
        Single<List<FlightRequest>> single = Single.create(new Single.OnSubscribe<List<FlightRequest>>() {

            @Override
            public void call(final SingleSubscriber<? super List<FlightRequest>> singleSubscriber) {
                Type resultType = new TypeToken<List<FlightRequest>>() {
                }.getType();
                ReservoirGetCallback<List<FlightRequest>> callback = new ReservoirGetCallback<List<FlightRequest>>() {
                    @Override
                    public void onSuccess(List<FlightRequest> flights) {
                        singleSubscriber.onSuccess(flights);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        singleSubscriber.onError(e);
                    }
                };

                Reservoir.getAsync(MY_FLIGHT_REQUESTS, resultType, callback);
            }
        });
        return single.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static void setFirebaseToken(String token) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIREBASE_TOKEN, token).commit();
    }

    public static void setUserToken(String token) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN, token).commit();
        editor.putBoolean(SESSION_ACTIVE, true).commit();
        buildAuthRetrofit();
    }

    public static void setTermsAndConditionsWereAccepted(boolean b) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        preferences.edit().putBoolean(TERMS, b).commit();
    }

    public static boolean termsAndConditionsWereAccepted() {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return false;
        }
        return preferences.getBoolean(TERMS, false);
    }

    public static void setAppInstalled(boolean b) {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return;
        }
        preferences.edit().putBoolean(INSTALL, true).commit();
    }

    public static boolean isAppInstalled() {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return false;
        }
        return preferences.getBoolean(INSTALL, false);
    }

    public static String getUserToken() {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return null;
        }
        return preferences.getString(USER_TOKEN, null);
    }

    public static String getFirebaseToken() {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return null;
        }
        return preferences.getString(FIREBASE_TOKEN, null);
    }

    public static boolean isSesionActive() {
        if (preferences == null) {
            Log.e(TAG, "Need to initialize Cache first");
            return false;
        }
        return preferences.getBoolean(SESSION_ACTIVE, false);
    }

    private static Retrofit retrofit;

    private static void buildRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG || URL.MAIN_URL.contains("dev") || URL.MAIN_URL.contains("192.168")) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json").build();
                return chain.proceed(build);
            }
        });

        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat(Util.DJANGO_DATE_TIME_FORMAT_WITHOUT_Z)
                .create();

        retrofit = new Retrofit.Builder().baseUrl(URL.MAIN_URL)
                .client(client).addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

    }

    /**
     * Build a Retrofit Instance with the RxJava Adapter and the Gson Converter
     *
     * @return a retrofit instance
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null)
            buildRetrofit();
        return retrofit;
    }

    private static Retrofit authRetrofit;

    private static void buildAuthRetrofit() {
        final String token = Cache.getUserToken();

        if (token == null)
            throw new IllegalStateException("You can't ask for a Auth Retrofit instance without a USER_TOKEN");

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG || URL.MAIN_URL.contains("dev") || URL.MAIN_URL.contains("192.168")) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", String.format("token %s", token));
                return chain.proceed(builder.build());
            }
        });

        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat(Util.DJANGO_DATE_TIME_FORMAT_WITHOUT_Z)
                .create();

        authRetrofit = new Retrofit.Builder().baseUrl(URL.MAIN_URL)
                .client(client).addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * Build a Retrofit Instance with the Authorization Header, RxJava Adapter and the Gson Converter
     *
     * @return a retrofit instance
     * @throws IllegalStateException if no token is in Shared Preferences
     */
    public static Retrofit getAuthRetrofitInstance() {
        if(getUserToken() == null)
            throw new IllegalArgumentException("Impossible to create a Auth Retrofit instance without a Token");
        if (authRetrofit == null)
            buildAuthRetrofit();
        return authRetrofit;
    }

}
