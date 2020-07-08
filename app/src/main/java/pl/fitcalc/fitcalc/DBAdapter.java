package pl.fitcalc.fitcalc;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DBAdapter {

    private static final String databaseName = "fitCalc";
    private static final int databaseVersion = 10;

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " first_name VARCHAR, " +
                    " last_name VARCHAR, " +
                    " email VARCHAR UNIQUE, " +
                    " password VARCHAR, " +
                    " birthday VARCHAR, " +
                    " sex VARCHAR, " +
                    " body_type VARCHAR);");

            db.execSQL("CREATE TABLE IF NOT EXISTS food (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name VARCHAR, " +
                    " manufactor_id VARCHAR, " + //(producent)
                    " weight DOUBLE, " +
                    " unit VARCHAR, " +
                    " kcal DOUBLE, " +
                    " proteins DOUBLE, " +
                    " carbohydrates DOUBLE, " +
                    " fat DOUBLE, " +
                    " added_by_user INT, " +
                    " barcode VARCHAR);");


            db.execSQL("CREATE TABLE IF NOT EXISTS user_food (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " food_id INTEGER, " +
                    " user_id INTEGER, " +
                    " date VARCHAR, " +
                    " serving DOUBLE, " +
                    " meail_id INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS user_measurements (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " user_id INTEGER, " +
                    " date VARCHAR, " +
                    " weight DOUBLE, " +
                    " desired_weight DOUBLE, " +
                    " goal VARCHAR, " +
                    " height DOUBLE, " +
                    " BMI DOUBLE, " +
                    " BMR DOUBLE, " +
                    " activity_level DOUBLE);");

            db.execSQL("CREATE TABLE IF NOT EXISTS user_meals (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " user_id INTEGER, " +
                    " meal_id INTEGER, " +
                    " date VARCHAR, " +
                    " time VARCHAR);");

            db.execSQL("CREATE TABLE IF NOT EXISTS meals (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name VARCHAR, " +
                    " kcal_share INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS owner (" +
                    " user_id INTEGER);");

            db.execSQL("INSERT INTO owner(user_id) VALUES(0)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS food");
            db.execSQL("DROP TABLE IF EXISTS user_food");
            db.execSQL("DROP TABLE IF EXISTS user_measurements");
            db.execSQL("DROP TABLE IF EXISTS user_meals");
            db.execSQL("DROP TABLE IF EXISTS meals");
            db.execSQL("DROP TABLE IF EXISTS owner");

            onCreate(db);

            Log.w("FitCalcLog", "Aktualizacja bazy danych z wersji " + oldVersion + "do " + newVersion + ", spowoduje usunięcie wszystkich dotychczasowych danych.");
        }

    }

    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }

    public void close() {
        DBHelper.close();
    }

    public void insert(String table, String fields, String values) {
        db.execSQL("INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")");
    }

    public int count(String table) {
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + table + "", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    public long logUserIn(String email, String password) {
        password = encodePassword(password); // poszukiwanie w bazie hasła zaszyfrowanego
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = '" + email + "' AND password = '" + password + "'", null);
        long user_id = -1;
        try {
            cursor.moveToFirst();
            user_id = cursor.getLong(-1);
            cursor.close();

            if (user_id != -1) // znaleziono użytkownika
                db.execSQL("UPDATE owner SET user_id = " + user_id);
        } catch (Exception ignored) {
        }

        return user_id;
    }

    public long getUserByEmail(String email) {
        Cursor mCount = db.rawQuery("SELECT id FROM users WHERE email = '" + email + "'", null);
        long id = -1;
        try {
            mCount.moveToFirst();
            id = mCount.getLong(-1);
            mCount.close();
        } catch (Exception ignored) {
        }

        return id;
    }

    public long[] registerUser(String imie, String nazwisko, String data_urodzenia, String email, String password) {
        ContentValues values = new ContentValues();
        values.put("first_name", imie);
        values.put("last_name", nazwisko);
        values.put("email", email);
        values.put("birthday", data_urodzenia);
        values.put("password", encodePassword(password));

        long user_id = db.insert("users", null, values);

        if (user_id == -1) // nie dodano nowego użytkownika
        {
            user_id = getUserByEmail(email);
            if (user_id == -1) // nie znaleziono użytkownika
                return new long[]{-1, 1}; // nie dodano i nie znaleziono użytkownika
            else
                return new long[]{user_id, 0}; // użytkownik o podanym adresie istnieje
        } else
            return new long[]{user_id, 1}; // dodano nowego użytkownika o user_id
    }

    private String encodePassword(String password) {
        // zaszyfrowanie hasła przed zapisaniem w bazie danych
        try {
            MessageDigest digest = null;
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            password = hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return password;
    }
}
