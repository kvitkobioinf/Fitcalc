package pl.fitcalc.fitcalc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Ustawienia {
    public static long getUserId(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        long nieZnalezionoUzytkownika = -1;
        long uzytkownik_id = sharedPref.getLong(activity.getString(R.string.uzytkownik_id_klucz), nieZnalezionoUzytkownika);

        return uzytkownik_id;
    }

    public static boolean setUserId(Activity activity, long user_id) {
        try {
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(activity.getString(R.string.uzytkownik_id_klucz), user_id);
            editor.apply();

            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

}
