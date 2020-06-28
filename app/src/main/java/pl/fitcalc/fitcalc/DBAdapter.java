package pl.fitcalc.fitcalc;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    private static final String databaseName = "fitCalc";
    private static  final int databaseVersion = 7;

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper (context);
    }


    //DatabaseHelper
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super (context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate (SQLiteDatabase db) {
            try {
                   //Create tables
                db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " first_name VARCHAR, " +
                        " last_name VARCHAR, " +
                        " email VARCHAR, " +
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

                db.execSQL("CREATE TABLE IF NOT EXISTS user_masurments (" +
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

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
          //All tables that are going to be dropped need to be lested here
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS food");
            onCreate(db);
            String TAG = "Tag";
            Log.w(TAG, "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old data" );
        }

    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " first_name VARCHAR, " +
                        " last_name VARCHAR, " +
                        " email VARCHAR, " +
                        " password VARCHAR, " +
                        " birthday VARCHAR, " +
                        " sex VARCHAR, " +
                        " body_type VARCHAR);");
        return this;
    }

    public void close() {DBHelper.close(); }

    /* Insert data to the database----------*/
    public  void  insert(String table, String fields, String values) {
        db.execSQL("INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")");
    }

    //Count
     public int count(String table)
     {
         Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + table + "", null);
         mCount.moveToFirst();
         int count = mCount.getInt(0);
         mCount.close();
         return count;
     }
}
