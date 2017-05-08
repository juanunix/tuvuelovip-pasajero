package fourgeeks.tuvuelovip.pasajero;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import fourgeeks.tuvuelovip.pasajero.util.Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests SignUpController
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilTest {

    @Test
    public void dateIsSameDay() throws Exception {
        assertEquals(true, Util.isSameDay(new Date(), new Date()));

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 1);
        Date time = instance.getTime();

        assertEquals(false, Util.isSameDay(new Date(), time));
    }

    @Test
    public void dateIsToday() throws Exception {
        assertEquals(true, Util.isToday(new Date()));

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 1);
        Date time = instance.getTime();

        assertEquals(false, Util.isToday(time));
    }

    @Test
    public void isEmailValid() throws Exception {
        assertEquals(true, Util.isEmailValid("alacret@gmail.com"));
        assertEquals(false, Util.isEmailValid("alacret@gmail.@com"));
        assertEquals(false, Util.isEmailValid("alacret@gmail"));
        assertEquals(false, Util.isEmailValid("alacretgmail.com"));
        assertEquals(false, Util.isEmailValid("alacre t@gmail. com"));
    }

    @Test
    public void isAlphaNumeric() throws Exception {
        assertEquals(true, Util.isAlphaNumeric("password123"));
        assertEquals(true, Util.isAlphaNumeric("3password1"));
        assertEquals(true, Util.isAlphaNumeric("3password"));

        assertEquals(false, Util.isAlphaNumeric("321"));
        assertEquals(false, Util.isAlphaNumeric("pass"));
    }
}