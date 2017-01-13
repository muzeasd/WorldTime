package database;

import android.provider.BaseColumns;

/**
 * Created by Muzammil on 10/01/2017.
 */

public class TimezonesReaderContract
{
    public static final String SQL_CREATE_COUNTRIES =
            "CREATE TABLE " + TimezoneEntry.TABLE_NAME + " (" +
            TimezoneEntry._ID + " INTEGER PRIMARY KEY," +
            TimezoneEntry.COLUMN_NAME_COUNTRY_NAME+ " TEXT);";

    public static final String SQL_CREATE_CITIES =
            "CREATE TABLE " + CityEntry.TABLE_NAME + " (" +
                    CityEntry.COLUMN_NAME_COUNTRY_ID + " INTEGER," +
                    CityEntry.COLUMN_NAME_CITY_NAME+ " TEXT," +
                    CityEntry.COLUMN_NAME_CITY_TIMEZONE + " TEXT);";

    public static final String SQL_CREATE_SAVED_COUNTRIES =
            "CREATE TABLE " + SavedTimezoneEntry.TABLE_NAME + " (" +
                    SavedTimezoneEntry.COLUMN_NAME_COUNTRY_ID + " INTEGER," +
                    SavedTimezoneEntry.COLUMN_NAME_CITY_NAME+ " TEXT ," +
                    SavedTimezoneEntry.COLUMN_NAME_TIME+ " TEXT," +
                    SavedTimezoneEntry.COLUMN_NAME_CITY_TIMEZONE + " TEXT);";

    public static final String SQL_DELETE_COUNTRIES = "DROP TABLE IF EXISTS " + TimezoneEntry.TABLE_NAME;
    public static final String SQL_DELETE_CITIES = "DROP TABLE IF EXISTS " + CityEntry.TABLE_NAME;
    public static final String SQL_DELETE_SAVED_COUNTRIES = "DROP TABLE IF EXISTS " + SavedTimezoneEntry.TABLE_NAME;

    private TimezonesReaderContract()
    {}

    public static class TimezoneEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "countries";
        public static final String COLUMN_NAME_COUNTRY_NAME = "country_name";
//        public static final String COLUMN_NAME_COUNTRY_FLAG = "country_flag";
    }

    public static class CityEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "states";
        public static final String COLUMN_NAME_COUNTRY_ID = "id";
        public static final String COLUMN_NAME_CITY_NAME = "city_name";
        public static final String COLUMN_NAME_CITY_TIMEZONE = "city_timezone";
    }

    public static class SavedTimezoneEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "saved_countries";
        public static final String COLUMN_NAME_COUNTRY_ID = "id";
        public static final String COLUMN_NAME_CITY_NAME = "city_name";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CITY_TIMEZONE = "city_timezone";
    }
}
