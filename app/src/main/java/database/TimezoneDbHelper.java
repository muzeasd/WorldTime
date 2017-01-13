package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Muzammil on 10/01/2017.
 */

public class TimezoneDbHelper extends SQLiteOpenHelper
{

    // Thread-safe Singleton Implementation

    private static TimezoneDbHelper instance = null;
    public static TimezoneDbHelper getInstance(Context context)
    {
        if(instance == null){
            synchronized (TimezoneDbHelper.class) {
                if(instance == null){
                    instance = new TimezoneDbHelper(context);
                }
            }
        }
        return instance;
    }

    // class variables & functions

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Timezones.db";

    private TimezoneDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TimezonesReaderContract.SQL_CREATE_COUNTRIES);
        db.execSQL(TimezonesReaderContract.SQL_CREATE_CITIES);
        db.execSQL(TimezonesReaderContract.SQL_CREATE_SAVED_COUNTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(TimezonesReaderContract.SQL_DELETE_CITIES);
        db.execSQL(TimezonesReaderContract.SQL_DELETE_COUNTRIES);
        db.execSQL(TimezonesReaderContract.SQL_DELETE_SAVED_COUNTRIES);
        onCreate(db);
    }
}
