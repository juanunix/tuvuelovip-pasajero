package fourgeeks.tuvuelovip.pasajero.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.api.APP;
import fourgeeks.tuvuelovip.pasajero.api.URL;
import fourgeeks.tuvuelovip.pasajero.signup.SignUpView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alacret on 12/6/16.
 */

public abstract class Util {

    public static final String TAG = "UTIL";
    public static final String TERMS_NOTIFICATION_TYPE = "te";
    public static final String FLIGHT_NOTIFICATION_TYPE = "fl";
    public static final String FLIGHT_TAKEN_NOTIFICATION_TYPE = "ra";
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
    private static final Pattern ALPHA_PATTERN = Pattern.compile("\\D+");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final String DJANGO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DJANGO_DATE_TIME_FORMAT_WITHOUT_Z = "yyyy-MM-dd'T'HH:mm:ss";
//    public static final String DJANGO_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Check if a string is AlphaNumeric
     *
     * @param s the String to Test
     * @return true if the String is AlphaNumeric, false otherwise
     */
    public static boolean isAlphaNumeric(String s) {
        if (s == null)
            return false;

        if (s.isEmpty())
            return false;

        if (!NUMERIC_PATTERN.matcher(s).find())
            return false;

        if (!ALPHA_PATTERN.matcher(s).find())
            return false;

        return true;
    }

    /**
     * <p>Checks if two dates are on the same day ignoring time.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>Checks if a date is today.</p>
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static SimpleDateFormat getDjangoDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat(DJANGO_DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    public static SimpleDateFormat getDjangoDateFormatWithMilisenconds() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    public static SimpleDateFormat getDjangoDateFormatWithOutZ() {
        SimpleDateFormat sdf = new SimpleDateFormat(DJANGO_DATE_TIME_FORMAT_WITHOUT_Z);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    /**
     * Check if an string is a valid Email
     *
     * @param email the String to test
     * @return true if is a valid email or false otherwise
     */
    public static boolean isEmailValid(String email) {
        if (email == null)
            return false;

        if (email.isEmpty())
            return false;

        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static void addRollbackableTransaction(int place, FragmentManager fm, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.add(place, fragment, tag);
        fragmentTransaction.commit();
    }

    public static void addRollbackableTransaction(int place, FragmentManager fm, Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.add(place, fragment, tag);
        fragmentTransaction.commit();
    }

    public static void addNonRollbackableTransaction(int place, FragmentManager fm, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(place, fragment, tag);
        fragmentTransaction.commit();
    }

    public static void addNonRollbackableTransaction(int place, FragmentManager fm, Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(place, fragment, tag);
        fragmentTransaction.commit();
    }


    public static void replaceNonRollbackableTransaction(int place, FragmentManager fm, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(place, fragment, tag);
        fragmentTransaction.commit();
    }


    public static void replaceRollbackableTransaction(int place, FragmentManager fm, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(place, fragment, tag);
        fragmentTransaction.commit();
    }

    public static void replaceRollbackableTransaction(int place, FragmentManager fm, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        String tag = fragment.getClass().getSimpleName();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(place, fragment, tag);
        fragmentTransaction.commit();
    }

    public static Bitmap decodeFromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;

    }

    /**
     * Support method for translating outsite android context
     *
     * @param f the Fragment
     * @param r the int resource id
     * @return The translate String from the Fragment Context
     */
    private static String getTranslation(Fragment f, String r) {
        try {
            int i = Integer.valueOf(r);
            return f.getString(i);
        } catch (Exception e) {
            return r;
        }
    }

    public static String msgFromRetrofitThrowable(Fragment f, Throwable e) {
        try {
            int i = Integer.valueOf(e.getMessage());
            return getTranslation(f, e.getMessage());
        } catch (Exception ex) {
            if (e instanceof HttpException) {
                String string = null;
                try {
                    string = ((HttpException) e).response().errorBody().string();
                } catch (IOException e1) {
                    Log.e("UTIL", e1.getLocalizedMessage());
                    return e.getLocalizedMessage();
                }
                return string;
            }
        }
        return e.getLocalizedMessage();
    }

    /**
     * Checks if the application is connected to the internet
     *
     * @param context
     * @return true is an connection to the internet is available, false otherwise
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Shortcut for creating Single Errors
     *
     * @param errorMsg the Error code
     * @return a Single that falls on error on Subscribe
     */
    public static Single createOnErrorSingle(String errorMsg) {
        return Single.error(new IllegalArgumentException(errorMsg)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getHeight()*(listAdapter.getCount() - 1));
        gridView.setLayoutParams(params);
    }

    public static final String FLIGHT_CANCEL_STATE = "Cancelado";
    public static final String FLIGHT_FLIGHT_STATE = "vo";
    public static final String FLIGHT_PAY_STATE = "pa";
    public static final String FLIGHT_EXPIRED_STATE = "Expirado";

    public static final String FLIGHT_PASSANGER_NOT_YET_QUALIFIED = "nr";
}

