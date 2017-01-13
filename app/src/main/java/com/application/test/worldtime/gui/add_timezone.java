package com.application.test.worldtime.gui;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.application.test.worldtime.R;

import java.util.ArrayList;
import java.util.List;

import database.TimezoneDbHelper;
import database.TimezonesReaderContract;
import models.CountryTimezone;

public class add_timezone extends AppCompatActivity
{

    TimezoneDbHelper mTimezoneDbHelper = null;

    boolean is_intial_display = true;
    Spinner mSpinnerCountry = null;
    Spinner mSpinnerCity = null;

    List<String> mListCoutryIds = null;
    List<String> mListCoutryNames = null;

    ArrayAdapter<String> mCountryArrayAdapter = null;

    List<String> mListCityNames = null;
    List<String> mListCityTimezones = null;
    ArrayAdapter<String> mCityArrayAdapter = null;

    CountryTimezone mReturnCountryTimezone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timezone);

        mTimezoneDbHelper = TimezoneDbHelper.getInstance(this);

        mSpinnerCountry = (Spinner) findViewById(R.id.countriesListSpinner);
        mSpinnerCity = (Spinner) findViewById(R.id.citiesListSpinner);

        getCountryList();

        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(mReturnCountryTimezone == null) {
                    mReturnCountryTimezone = new CountryTimezone(
                            Integer.parseInt(mListCoutryIds.get(position)),
                            "", // city/country name
                            null); // flag picture
                }
                else
                {
                    mReturnCountryTimezone.setId(Integer.parseInt(mListCoutryIds.get(position)));
                }

                ((TextView) findViewById(R.id.textviewSelectedCountry)).setText(mListCoutryNames.get(position));

                getCityList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) findViewById(R.id.textviewSelectedCity)).setText(mListCityNames.get(position));

                mReturnCountryTimezone.setName(mListCityNames.get(position));
                mReturnCountryTimezone.setTimezone(mListCityTimezones.get(position));

                int a = 10;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        is_intial_display = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.m_IsClosingApp = true;
    }

    private void getCountryList()
    {
        mListCoutryIds = new ArrayList<>();
        mListCoutryNames = new ArrayList<>();

        String countQuery = "SELECT  * FROM " + TimezonesReaderContract.TimezoneEntry.TABLE_NAME;
        SQLiteDatabase db = mTimezoneDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor.getCount() <= 0) return;

        while(cursor.moveToNext())
        {
            int id_index = cursor.getColumnIndex(TimezonesReaderContract.TimezoneEntry._ID);
            int name_index = cursor.getColumnIndex(TimezonesReaderContract.TimezoneEntry.COLUMN_NAME_COUNTRY_NAME);

            int id = cursor.getInt(id_index);
            String name = cursor.getString(name_index);

            mListCoutryIds.add(String.valueOf(id));
            mListCoutryNames.add(name);
        }

        mCountryArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mListCoutryNames);
        mSpinnerCountry.setAdapter(mCountryArrayAdapter);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mListCoutryNames);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.countriesListAutocomplete);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2 /*position*/, long arg3)
            {
                Object country_name = arg0.getItemAtPosition(arg2);

                int country_pos = mListCoutryNames.indexOf(country_name);
                mSpinnerCountry.setSelection(country_pos);

                // 1: we use "country_pos" because "mListCoutryIds & mListCoutryNames" are synchronized.
                // 2: Lists are ZERO-BASED & DB entries start from 1...
                // that's why using "country_pos+1" coz country_pos can be ZERO ( from any of the above mentioned lists)
                mReturnCountryTimezone.setId(country_pos+1);
                getCityList(country_pos);
            }
        });
    }

    private void getCityList(int position)
    {
        // NOTE: we use (position + ic_flag_1) bcoz, internally in db, COUNTRY_IDs start from ic_flag_1.
        // Since we do not add new countries at runtime, we can safely assume that these COUNTRY_IDs won't change
        String countQuery = "SELECT  * FROM " + TimezonesReaderContract.CityEntry.TABLE_NAME +
                            " WHERE id = " + (position + 1) + ";";

        SQLiteDatabase db = mTimezoneDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor.getCount() <= 0) return;

        mListCityNames = new ArrayList<String>();
        mListCityTimezones = new ArrayList<String>();

        while(cursor.moveToNext())
        {
            int id_index = cursor.getColumnIndex(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID);
            int name_index = cursor.getColumnIndex(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME);
            int timezone_index = cursor.getColumnIndex(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE);

            String city_name = cursor.getString(name_index);
            String city_timezone = cursor.getString(timezone_index);

            mListCityNames.add(city_name);
            mListCityTimezones.add(city_timezone);
        }

        mCityArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, mListCityNames);
        mSpinnerCity.setAdapter(mCityArrayAdapter);
        mSpinnerCity.setSelection(0);
    }

    public void OnClick_AddTimezone(View view)
    {
        Intent intent = new Intent();

        intent.putExtra("id", mReturnCountryTimezone.getId());
        intent.putExtra("name", mReturnCountryTimezone.getName());
        intent.putExtra("timezone", mReturnCountryTimezone.getTimezone());


        /*
        * finish() method only sends back the result if there is a mParent property set to null.
        * Otherwise the result is lost.
        * */

        if(getParent() == null)
            setResult(RESULT_OK,intent);
        else
            getParent().setResult(RESULT_OK, intent);

        //close this Activity...
        finish();
    }
}
