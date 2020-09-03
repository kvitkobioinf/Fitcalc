package pl.fitcalc.fitcalc;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class DBAdapter {

    private static final String databaseName = "fitCalc";
    private static final int databaseVersion = 13;

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
                    " manufactor_id INTEGER, " + //(producent)
                    " weight DOUBLE, " +
                    " unit VARCHAR, " +
                    " kcal DOUBLE, " +
                    " proteins DOUBLE, " +
                    " carbohydrates DOUBLE, " +
                    " fat DOUBLE, " +
                    " added_by_user INT, " +
                    " barcode VARCHAR);");

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
                    " activity_level INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS meals (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name VARCHAR, " +
                    " kcal_share INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS user_meals (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " user_id INTEGER, " +
                    " meal_id INTEGER, " +
                    " date VARCHAR, " +
                    " time VARCHAR);");

            db.execSQL("CREATE TABLE IF NOT EXISTS meal_food (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " food_id INTEGER, " +
                    " serving DOUBLE, " + // krotność wagi jedzenia
                    " meal_id INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS owner (" +
                    " user_id INTEGER);");

            db.execSQL("INSERT INTO owner (user_id) VALUES (0)");

            db.execSQL("INSERT INTO food (name, weight, unit, kcal, proteins, carbohydrates, fat) VALUES('Snickers', '100', 'g', '483', '8.6', '60.7', '22.6')");
            db.execSQL("INSERT INTO food (name, weight, unit, kcal, proteins, carbohydrates, fat) VALUES('Lody czekoladowe', '100', 'g', '261', '5.3', '31.1', '12.6')");
            db.execSQL("INSERT INTO food (name, weight, unit, kcal, proteins, carbohydrates, fat) VALUES('Herbatniki', '100', 'g', '428', '8', '72', '12')");
            db.execSQL("INSERT INTO food (name, weight, unit, kcal, proteins, carbohydrates, fat) VALUES('Chałwa', '100', 'g', '525', '14.1', '48.3', '30.6')");
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

    public long insert(String table, ContentValues values) {
        return db.insert(table, null, values);
    }

    public int update(String table, ContentValues values, String where) {
        return db.update(table, values, where, null);
    }

    public int count(String table) {
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + table + "", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    public ArrayList<Food> findFoods(String name) {
        Cursor food_cursor = db.rawQuery("SELECT id, name, weight, unit FROM food WHERE name LIKE '%" + name + "%'", null);
        ArrayList<Food> foods = new ArrayList<>();

        try {
            while (food_cursor.moveToNext()) {
                Food food = new Food();
                food.id = food_cursor.getInt(0);
                food.name = food_cursor.getString(1);
                food.weight = food_cursor.getFloat(2);
                food.unit = food_cursor.getString(3);

                foods.add(food);
            }
        } finally {
            food_cursor.close();
        }

        return foods;
    }

    public long logUserIn(String email, String password) {
        Cursor mCount = db.rawQuery("SELECT id FROM users WHERE email = '" + email + "' AND password = '" + password + "'", null);
        long id = -1;
        try {
            mCount.moveToFirst();
            id = mCount.getLong(0);
            mCount.close();
        } catch (Exception e) {
        }

        return id;
    }

    public int getUserId(String email) {
        Cursor mCount = db.rawQuery("SELECT id FROM users WHERE email = '" + email + "'", null);
        int id = -1;
        try {
            mCount.moveToFirst();
            id = mCount.getInt(0);
            mCount.close();
        } catch (Exception e) {
        }

        return id;
    }

    public String[] getUserMealIdTime(long user_id, String date) {
        Cursor user_meal_cursor = db.rawQuery("SELECT id, time FROM user_meals WHERE user_id = '" + user_id + "' AND date = '" + date + "'", null);
        String[] id_time = null;
        try {
            user_meal_cursor.moveToFirst();
            id_time = new String[2];
            id_time[0] = user_meal_cursor.getString(0); // id
            id_time[1] = user_meal_cursor.getString(1); // time
            user_meal_cursor.close();
        } catch (Exception ignored) {
        }

        return id_time;
    }
}
