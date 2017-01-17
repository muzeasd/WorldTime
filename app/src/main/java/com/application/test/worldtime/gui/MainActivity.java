package com.application.test.worldtime.gui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.icu.util.TimeUnit;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.application.test.worldtime.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.TimerTask;

import adapters.TimezoneAdapter;
import database.TimezoneDbHelper;
import database.TimezonesReaderContract;
import interfaces.OnItemClickListener;
import models.CountryTimezone;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener
{
    public RecyclerView mRecyclerView = null;
    List<CountryTimezone> mList = null;
    TimezoneAdapter mAdapter = null;

    TimezoneDbHelper mTimezoneDbHelper = null;

    static boolean m_IsClosingApp = true;
    static boolean m_IsHomeTimezoneSet = false;

    CheckBox mChkUseCurrentTime = null;

    List<String> list_australia = null;
    List<String> list_brazil = null;
    List<String> list_canada = null;
    List<String> list_chile = null;
    List<String> list_denmark = null;
    List<String> list_ecuador = null;
    List<String> list_france = null;
    List<String> list_indonesia = null;
    List<String> list_kazakhstan = null;
    List<String> list_kiribati = null;
    List<String> list_mexico = null;
    List<String> list_micronesia = null;
    List<String> list_mongolia = null;
    List<String> list_netherlands = null;
    List<String> list_newZealand = null;
    List<String> list_papuaNewGuinea = null;
    List<String> list_portugal = null;
    List<String> list_russia = null;
    List<String> list_spain = null;
    List<String> list_southAfrica = null;
    List<String> list_unitedKingdom = null;
    List<String> list_unitedStates = null;

    private FragmentDrawer mDrawerFragment;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), myToolbar);
        mDrawerFragment.setDrawerListener(this);

        mChkUseCurrentTime = ((CheckBox) findViewById(R.id.chkUseCurrentTime));
        mChkUseCurrentTime.setChecked(true);
        mChkUseCurrentTime.setEnabled(false);

//        mChkUseCurrentTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            private void OnChangeTime(int progress, TextView textView)
//            {
//                try
//                {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(new Date());
//                    calendar.add(Calendar.MINUTE, progress);
//
//                    String newDateInString = "";
//                    String dateInString = new java.text.SimpleDateFormat("MMM d, kk:mm").format(new Date(calendar.getTimeInMillis()));
//                    StringBuilder builder = new StringBuilder(dateInString);
//                    if(builder.charAt(8) == '2' && dateInString.charAt(9) == '4')
//                    {
//                        builder.replace(8, 10, "00");// = dateInString.replaceFirst("24", "00");
//                        newDateInString = builder.toString();
//                    }
//
//                    textView.setText(newDateInString.trim().length() == 0 ? dateInString : newDateInString);
//
//                    TimeZone timeZone = TimeZone.getDefault();
//                    String homeTimezoneName = timeZone.getDisplayName(); //e.g. Pakistan Standard Time
//                    String homeTimezoneId = timeZone.getDisplayName(false, TimeZone.SHORT); //e.g. UTC+05:00
//                    homeTimezoneId = homeTimezoneId.replace("GMT", "UTC");
//                    SimpleDateFormat formatter = new java.text.SimpleDateFormat("EEE, MMM d, ''yy kk:mm");
//
////                    for(int index=0; index<mList.size(); index++)
////                    {
////                        String itemTimezone = mList.get(index).getTimezone();
////                        Date date;
////                        if(homeTimezoneId.trim().equals(itemTimezone.trim()))
////                        {
////                            date = new Date(calendar.getTimeInMillis());
////                        }
////                        else
////                        {
////                            date = calcDateTime(homeTimezoneId, itemTimezone);
////                        }
////
////                        //dateInString = formatter.format(date);
////                        mList.get(index).setDateTime(date);
////                    }
////
////                    mAdapter.notifyDataSetChanged();
//
//                    newDateInString = "";
//                    for (int index = 0; index < mAdapter.getItemCount(); index++)
//                    {
//
//                        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(index);
//                        TextView tvDateTime = (TextView) holder.itemView.findViewById(R.id.datetime);
//                        TextView tvTimezone = (TextView) holder.itemView.findViewById(R.id.timezone);
//                        String itemTimezone = tvTimezone.getText().toString();
//
//                        if(homeTimezoneId.trim().equals(itemTimezone.trim()))
//                        {
//                            dateInString = formatter.format(new Date(calendar.getTimeInMillis()));
//                            builder = new StringBuilder(dateInString);
//                            if(builder.charAt(17) == '2' && dateInString.charAt(18) == '4')
//                            {
//                                builder.replace(17, 19, "00");// = dateInString.replaceFirst("24", "00");
//                                newDateInString = builder.toString();
//                            }
//
//                            tvDateTime.setText(newDateInString.trim().length() == 0 ? dateInString : newDateInString);
//                        }
//                        else
//                        {
//                            Date date = calcDateTime(homeTimezoneId, itemTimezone);
//                            dateInString = formatter.format(date);
//                            builder = new StringBuilder(dateInString);
//                            if(builder.charAt(17) == '2' && dateInString.charAt(18) == '4')
//                            {
//                                builder.replace(17, 19, "00");// = dateInString.replaceFirst("24", "00");
//                                newDateInString = builder.toString();
//                            }
//
//                            tvDateTime.setText(newDateInString.trim().length() == 0 ? dateInString : newDateInString);
//                        }
//                    }
//                }
//                catch (Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
////                // Finally we add newly created tableLayout to MainActivity TableLayout
////                TableLayout mainTableLayout =(TableLayout)findViewById(R.id.activity_main);
////
////                if(isChecked)
////                {
////                    View myView = findViewById(R.id.fragmentCustomTime);
////                    mainTableLayout.removeViewAt(1);
////                }
////                else
////                {
////                    TableRow seekbar_row = new TableRow(MainActivity.this);
////
////                    RelativeLayout fragmentCustomTime = (RelativeLayout) findViewById(R.id.fragmentCustomTime);
////                    if(fragmentCustomTime == null)
////                    {
////                        View view = getLayoutInflater().inflate(R.layout.fragment_custom_time, null);
////                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
////                        seekbar_row.addView(view, params);
////                        mainTableLayout.addView(seekbar_row, 1);
////                    }
////                }
//
//                int step = 1;
//                final int max = 1440;//24*60
//                final int half = max/2; // max/2 = 720
//                int min = 0;//this is 12 hours back from us
//
//                if(isChecked)
//                {
//                    TableLayout mainTableLayout =(TableLayout)findViewById(R.id.activity_main);
//                    mainTableLayout.removeViewAt(1);
//                }
//                else
//                {
//
//                    TableLayout custom_timezone_tablelayout = new TableLayout(getBaseContext());
//
//                    TableRow textView_row = new TableRow(MainActivity.this);
//
//                    String dateInString = new java.text.SimpleDateFormat("MMM d, kk:mm").format(new Date());
//
//                    // In first row, we add textview for showing current time the row
//                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
//                    TextView view = new TextView(MainActivity.this);
//                    view.setId(R.id.display_seekbar_time);
//                    view.setVisibility(View.VISIBLE);
//                    view.setText(dateInString);
//                    view.setLayoutParams(params);
//                    textView_row.addView(view);
//                    custom_timezone_tablelayout.addView(textView_row);
//
//                    // in second row, we first add MINUS button then SEEKBAR then PLUS button
//                    TableRow seekbar_row = new TableRow(MainActivity.this);
//
//                    Button minus_button = new Button(MainActivity.this);
//                    minus_button.setText("-");
//                    minus_button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
//                    minus_button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            SeekBar seekbar = (SeekBar) findViewById(R.id.timeSeekBar);
//                            seekbar.setProgress(seekbar.getProgress() - 1);
//                        }
//                    });
//
//                    seekbar_row.addView(minus_button);
//                    minus_button.getLayoutParams().width = 10;
//
//
//                    TableRow.LayoutParams layoutParams =  new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 9.0f);
//                    SeekBar timeSeekBar = new SeekBar(MainActivity.this);
//                    timeSeekBar.setLayoutParams(layoutParams);
//                    timeSeekBar.setId(R.id.timeSeekBar);
//                    timeSeekBar.setMax(max);
//                    timeSeekBar.setProgress(half);
//                    seekbar_row.addView(timeSeekBar/*, layoutParams*/);
//
//                    Button plus_button = new Button(MainActivity.this);
//                    plus_button.setText("+");
////                    plus_button.setMinimumWidth(30);
////                    plus_button.setMaxWidth(30);
////                    plus_button.setMaxHeight(30);
////                    plus_button.setMinimumHeight(30);
////                    plus_button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
//                    plus_button.setLayoutParams(new LinearLayout.LayoutParams(30, 20));
//                    plus_button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            SeekBar seekbar = (SeekBar) findViewById(R.id.timeSeekBar);
//                            seekbar.setProgress(seekbar.getProgress() + 1);
//                        }
//                    });
//
//                    seekbar_row.addView(plus_button);
//
//                    // now we add second row to the newly created layout
//                    custom_timezone_tablelayout.addView(seekbar_row);
//
//
//                    // Finally we add newly created tableLayout to MainActivity TableLayout
//                    TableLayout mainTableLayout =(TableLayout)findViewById(R.id.activity_main);
//                    mainTableLayout.addView(custom_timezone_tablelayout, 1);
//
//                    // dont forget to add the progress_change_listener to the seekbar
//
//                    timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                        @Override
//                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
//                        {
//                            int minutes_to_add = 0;
//                            TextView textView = (TextView) MainActivity.this.findViewById(R.id.display_seekbar_time);
//
//                            if(progress > half)
//                                minutes_to_add = progress - half;
//                            else minutes_to_add = -1 * (half - progress);
//
//                            String msg = "P: " + progress + ", M: " + minutes_to_add;
//
////                            textView.setText(msg);
//
//                            OnChangeTime(minutes_to_add, textView);
//                        }
//
//                        @Override
//                        public void onStartTrackingTouch(SeekBar seekBar) { }
//
//                        @Override
//                        public void onStopTrackingTouch(SeekBar seekBar)
//                        {
//                            //Toast.makeText(getBaseContext(), "Value = " + seekBar.getProgress(), LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//            }
//        });

        prepareCitiesWithMultipleTimezones();

        mList = new ArrayList<>();
        mAdapter = new TimezoneAdapter(mList, new OnItemClickListener()
        {
            @Override
            public void onItemClick(final CountryTimezone item)
            {
                final TimezoneAdapter adapter = mAdapter;
                final int timezone_count_b4_dlg = mList.size();

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Caution");
                alert.setMessage("Remove this timezone?");
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void RemoveTimezoneFromDB(String city_name /*int country_id*/)
                    {
                        SQLiteDatabase db = mTimezoneDbHelper.getWritableDatabase();

//                        String countQuery = "DELETE FROM " + TimezonesReaderContract.SavedTimezoneEntry.TABLE_NAME +
//                                " WHERE "+ TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_COUNTRY_ID +
//                                " = " + country_id + ";";
//                        Cursor cursor = db.rawQuery(countQuery, null);
//                        cursor.close();

                        // Define 'where' part of query.
                        String selection = TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_CITY_NAME + " LIKE ?";

                        // Specify arguments in placeholder order.
                        String[] selectionArgs = { city_name };

                        // Issue SQL statement.
                        db.delete(TimezonesReaderContract.SavedTimezoneEntry.TABLE_NAME, selection, selectionArgs);

                        String countQuery = "SELECT * FROM " + TimezonesReaderContract.SavedTimezoneEntry.TABLE_NAME;
                        Cursor cursor = db.rawQuery(countQuery, null);
                        int count = cursor.getCount();
                        cursor.close();
                    }

                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        for(int index=0; index<mList.size(); index++)
                        {
                            int country_id = mList.get(index).getId();
                            if(country_id == item.getId())
                            {
                                RemoveTimezoneFromDB(item.getName() /*country_id*/);
                                if(mList.get(index).getIsHomeTimezone() == true)
                                    m_IsHomeTimezoneSet = false;

                                mList.remove(index);

                                if(mList.size() <= 0) break;

                                RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(index);
                                holder.itemView.setBackgroundColor(Color.rgb(238, 238, 238));

                                break;
                            }
                        }

                        int timezone_count_after_dlg = mList.size();

                        // this check makes sure that we have changed out list
                        if(timezone_count_after_dlg != timezone_count_b4_dlg)
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                alert.setNegativeButton(android.R.string.no, null);
                alert.show();
            }
        });

//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mAdapter);
//
//        if(mTimezoneDbHelper == null)
//            mTimezoneDbHelper = TimezoneDbHelper.getInstance(this);
//
//        PrepareDatabase_Async asyncTask = new PrepareDatabase_Async(this);
//        asyncTask.execute();
    }

    Runnable updater;

    private void setupAlarmManager()
    {
        final Context context = this.getApplicationContext();
        final Handler timerHandler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int index = 0; index < mAdapter.getItemCount(); index++)
                    {

                        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(index);
                        TextView tvDateTime = (TextView) holder.itemView.findViewById(R.id.datetime);

                        String dateInString = tvDateTime.getText().toString(); //new java.text.SimpleDateFormat("EEE, MMM d, ''yy hh:mm").format(new Date());

                        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy kk:mm"); //"EEE, MMM d, ''yy"
                        Date parsedDate = formatter.parse(dateInString);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(parsedDate);
                        calendar.add(Calendar.MINUTE, 1);

                        dateInString = new java.text.SimpleDateFormat("EEE, MMM d, ''yy kk:mm")
                                .format(new Date(calendar.getTimeInMillis()));

                        String newDateInString = "";
                        StringBuilder builder = new StringBuilder(dateInString);
                        if(builder.charAt(17) == '2' && dateInString.charAt(18) == '4')
                        {
                            builder.replace(17, 19, "00");// = dateInString.replaceFirst("24", "00");
                            newDateInString = builder.toString();
                        }

                        tvDateTime.setText(newDateInString.trim().length() > 0 ? newDateInString : dateInString);
                    }

                    //Toast.makeText(context, "Alarm Set", Toast.LENGTH_LONG).show();
                    timerHandler.postDelayed(updater, 1000*60);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        };

        timerHandler.postDelayed(updater, 1000*60);
    }

    private void prepareCitiesWithMultipleTimezones()
    {

        list_australia = new ArrayList<String>();
        list_brazil = new ArrayList<>();
        list_canada = new ArrayList<>();
        list_chile = new ArrayList<>();
        list_denmark = new ArrayList<>();
        list_ecuador = new ArrayList<>();
        list_france = new ArrayList<>();
        list_indonesia = new ArrayList<>();
        list_kazakhstan = new ArrayList<>();
        list_kiribati = new ArrayList<>();
        list_mexico = new ArrayList<>();
        list_micronesia = new ArrayList<>();
        list_mongolia = new ArrayList<>();
        list_netherlands = new ArrayList<>();
        list_newZealand = new ArrayList<>();
        list_papuaNewGuinea = new ArrayList<>();
        list_portugal = new ArrayList<>();
        list_russia = new ArrayList<>();
        list_spain = new ArrayList<>();
        list_southAfrica = new ArrayList<>();
        list_unitedKingdom = new ArrayList<>();
        list_unitedStates = new ArrayList<>();

        list_australia.add("UTC+05:00, McDonald Islands");
        list_australia.add("UTC+06:30, Cocos Islands");
        list_australia.add("UTC+07:00, Christmas Island");
        list_australia.add("UTC+08:00, Perth");
        list_australia.add("UTC+09:30, Darwin");
        list_australia.add("UTC+10:00, Brisbane");
        list_australia.add("UTC+10:30, Adelaide");
        list_australia.add("UTC+11:00, Sydney");


        list_brazil.add("UTC-05:00, Rio Branco");
        list_brazil.add("UTC-04:00, Manaus");
        list_brazil.add("UTC-03:00, Belem");
        list_brazil.add("UTC-02:00, Brasilia");

        list_canada.add("UTC-08:00, Vancouver");
        list_canada.add("UTC-07:00, Edmonton");
        list_canada.add("UTC-06:00, Winnipeg");
        list_canada.add("UTC-05:00, Toronto");
        list_canada.add("UTC-04:00, Halifax");
        list_canada.add("UTC-03:30, St. John's");

        list_chile.add("UTC-05:00, Easter Island");
        list_chile.add("UTC-03:00, Santiago");

        list_denmark.add("UTC-04:00, Thule Air Base");
        list_denmark.add("UTC-03:00, Nuuk");
        list_denmark.add("UTC-01:00, Ittoqqortoormiit");
        list_denmark.add("UTC+00:00, Danmarkshavn");
        list_denmark.add("UTC+01:00, Copenhagen");

        list_ecuador.add("UTC-06:00, Galapagos Islands");
        list_ecuador.add("UTC-05:00, Quito");

        list_france.add("UTC-10:00, Pape'ete");
        list_france.add("UTC-09:30, Marquesas Islands");
        list_france.add("UTC-09:00, Gambier Islands");
        list_france.add("UTC-08:00, Clipperton Island");
        list_france.add("UTC-04:00, Guadeloupe");
        list_france.add("UTC-03:00, French Guiana");
        list_france.add("UTC+01:00, Paris");
        list_france.add("UTC+03:00, Mayotte");
        list_france.add("UTC+04:00, Réunion");
        list_france.add("UTC+05:00, Kerguelen Islands, Crozet Islands");
        list_france.add("UTC+11:00, New Caledonia");
        list_france.add("UTC+12:00, Wallis and Futuna");

        list_indonesia.add("UTC+07:00, Jakarta");
        list_indonesia.add("UTC+08:00, Makassar");
        list_indonesia.add("UTC+09:00, Manokwari");

        list_kazakhstan.add("UTC+05:00, Oral");
        list_kazakhstan.add("UTC+06:00, Almaty");

        list_kiribati.add("UTC+12:00, Tarawa");
        list_kiribati.add("UTC+13:00, Kanton Island");
        list_kiribati.add("UTC+14:00, Kiritimati");

        list_mexico.add("UTC-08:00, Tijuana");
        list_mexico.add("UTC-07:00, Hermosillo");
        list_mexico.add("UTC-06:00, Mexico City");
        list_mexico.add("UTC-05:00, Cancun");

        list_micronesia.add("UTC+10:00, Weno");
        list_micronesia.add("UTC+11:00, Tofol");

        list_mongolia.add("UTC+07:00, Hovd");
        list_mongolia.add("UTC+08:00, Ulaanbaatar");

        list_netherlands.add("UTC-04:00, Kralendijk");
        list_netherlands.add("UTC+01:00, Amsterdam");

        list_newZealand.add("UTC-11:00, Niue");
        list_newZealand.add("UTC-10:00, Cook Islands");
        list_newZealand.add("UTC+12:00, Auckland");
        list_newZealand.add("UTC+12:45, Chatham Islands");
        list_newZealand.add("UTC+13:00, Tokelau");

        list_papuaNewGuinea.add("UTC+10:00, Port Moresby");
        list_papuaNewGuinea.add("UTC+11:00, Arawa");

        list_portugal.add("UTC-01:00, Ponta Delgada");
        list_portugal.add("UTC+00:00, Lisbon");

        list_russia.add("UTC+02:00, Kaliningrad");
        list_russia.add("UTC+03:00, Moscow");
        list_russia.add("UTC+04:00, Samara");
        list_russia.add("UTC+05:00, Yekaterinburg");
        list_russia.add("UTC+06:00, Omsk");
        list_russia.add("UTC+07:00, Krasnoyarsk");
        list_russia.add("UTC+08:00, Irkutsk");
        list_russia.add("UTC+09:00, Chita");
        list_russia.add("UTC+10:00, Vladivostok");
        list_russia.add("UTC+11:00, Magadan");
        list_russia.add("UTC+12:00, Anadyr");

        list_spain.add("UTC+00:00, Las Palmas");
        list_spain.add("UTC+01:00, Madrid");

        list_southAfrica.add("UTC+02:00, Cape Town");
        list_southAfrica.add("UTC+03:00, Prince Edward Islands");

        list_unitedKingdom.add("UTC-08:00, Pitcairn Islands");
        list_unitedKingdom.add("UTC-05:00, Cayman Islands");
        list_unitedKingdom.add("UTC-04:00, Anguilla");
        list_unitedKingdom.add("UTC-03:00, Falkland Islands");
        list_unitedKingdom.add("UTC-02:00, South Georgia");
        list_unitedKingdom.add("UTC+00:00, London");
        list_unitedKingdom.add("UTC+01:00, Gibraltar");
        list_unitedKingdom.add("UTC+02:00, Akrotiri");
        list_unitedKingdom.add("UTC+06:00, British Indian Ocean Territory");

        list_unitedStates.add("UTC-10:00, Honolulu");
        list_unitedStates.add("UTC-09:00, Anchorage");
        list_unitedStates.add("UTC-08:00, Los Angeles");
        list_unitedStates.add("UTC-07:00, Arizona");
        list_unitedStates.add("UTC-06:00, Chicago");
        list_unitedStates.add("UTC-05:00, New York");
        list_unitedStates.add("UTC-04:00, Puerto Rico");
        list_unitedStates.add("UTC+10:00, Guam");

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        if(m_IsClosingApp)
            mTimezoneDbHelper.close();

        super.onStop();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.overridePendingTransition(R.anim.slide_in,
                R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = getMenuInflater();//.inflate(R.menu.main_menu, menu);
        inflator.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) return;

        if(requestCode == 10)
        {

            int id = data.getIntExtra("id", -1);
            String name = data.getStringExtra("name");
            String timezone = data.getStringExtra("timezone");
            boolean isHomeTimezone = data.getBooleanExtra("isHomeTimezone", false);

            if(mList.size() > 0)
            {
                for (int index = 0; index < mList.size(); index++)
                {
                    if (mList.get(index).getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
                    {
                        Toast.makeText(this, "Timezone Already Added !!", LENGTH_SHORT);
                        return;
                    }
                }
            }

            Bitmap bitmap = getFlag(id);

            CountryTimezone country = new CountryTimezone(id, name, bitmap);
            country.setTimezone(timezone);
            country.setIsHomeTimezone(isHomeTimezone);
            country.setFlag(bitmap);

            // If this is first item...then it is Home timezone... secondary otherwise
            Date date = null;
            if(country.getIsHomeTimezone())
            {
                date = new Date();
                m_IsHomeTimezoneSet = country.getIsHomeTimezone();
            }
            else
            {
                TimeZone timeZone = TimeZone.getDefault();

                // home timezone_name (e.g Pakistan Standard Time)
                String homeTimezoneName = timeZone.getDisplayName();

                // home timezone_id (e.g UTC+05:00 or GMT+05:00)
                String homeTimezoneId = timeZone.getDisplayName(false, TimeZone.SHORT);

                date = calcDateTime(homeTimezoneId, country.getTimezone());
            }

            country.setDateTime(date);
            mList.add(country);

            // Persist it to retrieve later during app startup/shutdown
            AddTimezoneToDB(country);

            mAdapter.notifyDataSetChanged();

            if(mList.size() == 1)
            {
                setupAlarmManager();
            }

            m_IsClosingApp = true;
        }
        else
        {

        }
    }

    private void AddTimezoneToDB(CountryTimezone country)
    {
        SQLiteDatabase db = mTimezoneDbHelper.getWritableDatabase();

        ContentValues content_value = new ContentValues();
        content_value.put(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_COUNTRY_ID, country.getId());
        content_value.put(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_CITY_NAME, country.getName());
        content_value.put(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_ISHOMETIMEZONE, country.getIsHomeTimezone() == true ? 1 : 0);
        content_value.put(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_CITY_TIMEZONE, country.getTimezone());

        long countryId = db.insert(TimezonesReaderContract.SavedTimezoneEntry.TABLE_NAME, null, content_value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case R.id.add_menu:
                Intent intent = new Intent(this, add_timezone.class);
                startActivityForResult(intent, 10/*, ActivityOptions.(this).toBundle()*/); // requestCode = 10
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                m_IsClosingApp = false;
                return super.onOptionsItemSelected(item);
            case R.id.action_settings:
                return true;
            default:
                Toast.makeText(this, "Unrecognized Action", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayView(int position)
    {
        android.support.v4.app.Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.drawer_title_home);
                break;
            case 1:
                fragment = new FriendsFragment();
                title = getString(R.string.drawer_title_friends);
                break;
            case 2:
                fragment = new MessagesFragment();
                title = getString(R.string.drawer_title_messages);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private Bitmap getFlag(int country_id)
    {
        switch (country_id)
        {
            case 1:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_1);
            case 2:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_2);
            case 3:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_3);
            case 4:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_4);
            case 5:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_5);
            case 6:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_6);
            case 7:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_7);
            case 8:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_8);
            case 9:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_9);
            case 10:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_10);
            case 11:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_11);
            case 12:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_12);
            case 13:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_13);
            case 14:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_14);
            case 15:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_15);
            case 16:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_16);
            case 17:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_17);
            case 18:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_18);
            case 19:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_19);
            case 20:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_20);
            case 21:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_21);
            case 22:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_22);
            case 23:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_23);
            case 24:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_24);
            case 25:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_25);
            case 26:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_26);
            case 27:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_27);
            case 28:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_28);
            case 29:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_29);
            case 30:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_30);
            case 31:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_31);
            case 32:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_32);
            case 33:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_33);
            case 34:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_34);
            case 35:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_35);
            case 36:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_36);
            case 37:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_37);
            case 38:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_38);
            case 39:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_39);
            case 40:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_40);
            case 41:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_41);
            case 42:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_42);
            case 43:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_43);
            case 44:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_44);
            case 45:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_45);
            case 46:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_46);
            case 47:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_47);
            case 48:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_48);
            case 49:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_49);
            case 50:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_50);
            case 51:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_51);
            case 52:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_52);
            case 53:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_53);
            case 54:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_54);
            case 55:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_55);
            case 56:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_56);
            case 57:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_57);
            case 58:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_58);
            case 59:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_59);
            case 60:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_60);
            case 61:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_61);
            case 62:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_62);
            case 63:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_63);
            case 64:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_64);
            case 65:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_65);
            case 66:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_66);
            case 67:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_67);
            case 68:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_68);
            case 69:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_69);
            case 70:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_70);
            case 71:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_71);
            case 72:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_72);
            case 73:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_73);
            case 74:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_74);
            case 75:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_75);
            case 76:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_76);
            case 77:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_77);
            case 78:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_78);
            case 79:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_79);
            case 80:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_80);
            case 81:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_81);
            case 82:
                return null;
            case 83:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_83);
            case 84:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_84);
            case 85:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_85);
            case 86:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_86);
            case 87:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_87);
            case 88:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_88);
            case 89:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_89);
            case 90:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_90);
            case 91:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_91);
            case 92:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_92);
            case 93:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_93);
            case 94:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_94);
            case 95:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_95);
            case 96:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_96);
            case 97:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_97);
            case 98:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_98);
            case 99:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_99);
            case 100:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_100);
            case 101:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_101);
            case 102:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_102);
            case 103:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_103);
            case 104:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_104);
            case 105:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_105);
            case 106:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_106);
            case 107:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_107);
            case 108:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_108);
            case 109:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_109);
            case 110:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_110);
            case 111:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_111);
            case 112:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_112);
            case 113:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_113);
            case 114:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_114);
            case 115:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_115);
            case 116:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_116);
            case 117:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_117);
            case 118:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_118);
            case 119:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_119);
            case 120:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_120);
            case 121:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_121);
            case 122:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_122);
            case 123:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_123);
            case 124:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_124);
            case 125:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_125);
            case 126:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_126);
            case 127:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_127);
            case 128:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_128);
            case 129:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_129);
            case 130:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_130);
            case 131:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_131);
            case 132:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_132);
            case 133:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_133);
            case 134:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_134);
            case 135:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_135);
            case 136:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_136);
            case 137:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_137);
            case 138:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_138);
            case 139:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_139);
            case 140:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_140);
            case 141:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_141);
            case 142:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_142);
            case 143:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_143);
            case 144:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_144);
            case 145:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_145);
            case 146:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_146);
            case 147:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_147);
            case 148:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_148);
            case 149:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_149);
            case 150:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_150);
            case 151:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_151);
            case 152:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_152);
            case 153:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_153);
            case 154:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_154);
            case 155:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_155);
            case 156:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_156);
            case 157:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_157);
            case 158:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_158);
            case 159:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_159);
            case 160:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_160);
            case 161:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_161);
            case 162:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_162);
            case 163:
                return null;
            case 164:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_164);
            case 165:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_165);
            case 166:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_166);
            case 167:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_167);
            case 168:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_168);
            case 169:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_169);
            case 170:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_170);
            case 171:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_171);
            case 172:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_172);
            case 173:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_173);
            case 174:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_174);
            case 175:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_175);
            case 176:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_176);
            case 177:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_177);
            case 178:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_178);
            case 179:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_179);
            case 180:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_180);
            case 181:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_181);
            case 182:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_182);
            case 183:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_183);
            case 184:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_184);
            case 185:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_185);
            case 186:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_186);
            case 187:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_187);
            case 188:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_188);
            case 189:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_189);
            case 190:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_190);
            case 191:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_191);
            case 192:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_192);
            case 193:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_193);
            case 194:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_194);
            case 195:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_195);
            case 196:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_196);
            case 197:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_197);
            case 198:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_198);
        }

        return null;
    }

    private Date calcDateTime(String homeTimezone, String secondaryTimezone)
    {

        if(homeTimezone.trim().length() <= 0) return null;

        /*
        *   Manipulate HOME_TIME_FIRST
        * */

        String home_time_operator = homeTimezone.substring(3, 4);
        String str_home_hours = homeTimezone.substring(4, 6);
        String str_home_minutes = homeTimezone.substring(homeTimezone.length() - 2); // last two characters

        // Is home timezone negative ?
        int total_time_in_mins = (Integer.parseInt(str_home_hours) * 60) + Integer.parseInt(str_home_minutes);
        if(home_time_operator.contains("-"))
            total_time_in_mins = total_time_in_mins * (-1);

        // Home minutes to milliseconds
        long home_time_in_millis = total_time_in_mins * 60 * 1000;

        // In order to get UTC+00:00
        // If home timezone is positive (i.e. UTC+05:00) ....then we subtract these milliseconds from current time
        // If home timezone is negative (i.e. UTC-02:00) ....then we add these milliseconds in current time

        Calendar UTC_ZERO = Calendar.getInstance();
        long utcZERO = -1;
        if(home_time_operator.contains("-"))
            utcZERO = UTC_ZERO.getTimeInMillis() + home_time_in_millis;
        else
            utcZERO = UTC_ZERO.getTimeInMillis() - home_time_in_millis;

        // We have UTC+0 time now
        UTC_ZERO.setTimeInMillis(utcZERO);


        /*
        *   Manipulate DEST_TIME
        * */


        String dest_time_operator = secondaryTimezone.substring(3, 4);
        String str_dest_hours = secondaryTimezone.substring(4, 6);
        String str_dest_minutes = secondaryTimezone.substring(secondaryTimezone.length() - 2);

        // Is dest timezone negative ?
        total_time_in_mins = 0;
        total_time_in_mins = (Integer.parseInt(str_dest_hours) * 60) + Integer.parseInt(str_dest_minutes);
        if(dest_time_operator.contains("-"))
            total_time_in_mins = total_time_in_mins * (-1);

        // Dest minutes to milliseconds
        long dest_time_in_millis = total_time_in_mins * 60 * 1000;

        // this is the destination time
        dest_time_in_millis = UTC_ZERO.getTimeInMillis() + dest_time_in_millis;

        // Prepare to format and return destination time
        Calendar TARGET_UTC = Calendar.getInstance();
        TARGET_UTC.setTimeInMillis(dest_time_in_millis);

        DateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy hh:mm"); //"MM/dd/yyyy HH:mm:ss"
        String dest_date_time_str = formatter.format(TARGET_UTC.getTime());
        Date dest_date_time = new Date(TARGET_UTC.getTimeInMillis());

        return dest_date_time;
    }

    @Override
    public void onDrawerItemSelected(View view, int position)
    {
        displayView(position);
    }

    private class PrepareDatabase_Async extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alt;
        Context m_Context;

        AlertDialog alert;
        Dialog dlg;

        public PrepareDatabase_Async(Context context)
        {
            this.m_Context = context;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            PrepareDatabase_AddCountries();
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Preparing.");
            alert.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            alert.setMessage("Progress: " + values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            loadSavedTimezones();
            alert.dismiss();
        }


        private int getCountryCount()
        {
            String countQuery = "SELECT  * FROM " + TimezonesReaderContract.TimezoneEntry.TABLE_NAME;
            SQLiteDatabase db = mTimezoneDbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        }

        private int getCityCount()
        {
            String countQuery = "SELECT  * FROM " + TimezonesReaderContract.CityEntry.TABLE_NAME;
            SQLiteDatabase db = mTimezoneDbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        }

        private void PrepareDatabase_AddCountries()
        {

            // we first check if both "Cities & Countries" tables are created along with their data
            // we handling 198 countries at the moment

            if(getCountryCount() >= 198)
                return;

            SQLiteDatabase db = mTimezoneDbHelper.getWritableDatabase();

            List<String> list = getCountries();

//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_flag_empty);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();

            for (int index=0; index<list.size(); index++)
            {
                // timezoneinfo[0] = country_name
                // timezoneinfo[ic_flag_1] = time_zone
                // timezoneinfo[2] = capital

                String[] timezone_info = list.get(index).split(",");
                String country_name = timezone_info[0].trim();//

                // first insert the country
                ContentValues country = new ContentValues();
                country.put(TimezonesReaderContract.TimezoneEntry.COLUMN_NAME_COUNTRY_NAME, country_name);

                long countryId = db.insert(TimezonesReaderContract.TimezoneEntry.TABLE_NAME, null, country);

                if(timezone_info.length < 3)
                {
                    // this country has more than one time_zones ( i.e. Russia)....we handle them seperately
                    PrepareDatabase_AddCities((int)countryId, country_name.toLowerCase(), db);
                }
                else
                {
                    ContentValues city = new ContentValues();
                    city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, countryId);
                    city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, timezone_info[1].trim());
                    city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, timezone_info[2].trim());

                    // this country has only one time_zone (i.e. Pakistan )....we add them here
                    db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                }

                float add_countries_to_db_percent = (float)(((index + 1) * 100 ) / 198);
                float aValue = (float) (((index+ + 1) / 198) * 100);
                publishProgress((int)add_countries_to_db_percent);
            }
        }

        private void PrepareDatabase_AddCities(int country_id, String country_name, SQLiteDatabase db)
        {
            switch(country_name)
            {
                case "australia":
                    for(int index=0; index<list_australia.size(); index++)
                    {
                        String[] city_info = list_australia.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "brazil":
                    for(int index=0; index<list_brazil.size(); index++)
                    {
                        String[] city_info = list_brazil.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "canada":
                    for(int index=0; index<list_canada.size(); index++)
                    {
                        String[] city_info = list_canada.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "chile":
                    for(int index=0; index<list_chile.size(); index++)
                    {
                        String[] city_info = list_chile.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "denmark":
                    for(int index=0; index<list_denmark.size(); index++)
                    {
                        String[] city_info = list_denmark.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "ecuador":
                    for(int index=0; index<list_ecuador.size(); index++)
                    {
                        String[] city_info = list_ecuador.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "france":
                    for(int index=0; index<list_france.size(); index++)
                    {
                        String[] city_info = list_france.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "indonesia":
                    for(int index=0; index<list_indonesia.size(); index++)
                    {
                        String[] city_info = list_indonesia.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "kazakhstan":
                    for(int index=0; index<list_kazakhstan.size(); index++)
                    {
                        String[] city_info = list_kazakhstan.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "kiribati":
                    for(int index=0; index<list_kiribati.size(); index++)
                    {
                        String[] city_info = list_kiribati.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "mexico":
                    for(int index=0; index<list_mexico.size(); index++)
                    {
                        String[] city_info = list_mexico.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "micronesia":
                    for(int index=0; index<list_micronesia.size(); index++)
                    {
                        String[] city_info = list_micronesia.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "mongolia":
                    for(int index=0; index<list_mongolia.size(); index++)
                    {
                        String[] city_info = list_mongolia.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "netherlands":
                    for(int index=0; index<list_netherlands.size(); index++)
                    {
                        String[] city_info = list_netherlands.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "new zealand":
                    for(int index=0; index<list_newZealand.size(); index++)
                    {
                        String[] city_info = list_newZealand.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "papua New Guinea":
                    for(int index=0; index<list_papuaNewGuinea.size(); index++)
                    {
                        String[] city_info = list_papuaNewGuinea.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "portugal":
                    for(int index=0; index<list_portugal.size(); index++)
                    {
                        String[] city_info = list_portugal.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "russia":
                    for(int index=0; index<list_portugal.size(); index++)
                    {
                        String[] city_info = list_portugal.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "spain":
                    for(int index=0; index<list_spain.size(); index++)
                    {
                        String[] city_info = list_spain.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "south africa":
                    for(int index=0; index<list_southAfrica.size(); index++)
                    {
                        String[] city_info = list_southAfrica.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "united kingdom":
                    for(int index=0; index<list_unitedKingdom.size(); index++)
                    {
                        String[] city_info = list_unitedKingdom.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;

                case "united states":
                    for(int index=0; index<list_unitedStates.size(); index++)
                    {
                        String[] city_info = list_unitedStates.get(index).split(",");
                        ContentValues city = new ContentValues();
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_COUNTRY_ID, country_id);
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_TIMEZONE, city_info[0].trim());
                        city.put(TimezonesReaderContract.CityEntry.COLUMN_NAME_CITY_NAME, city_info[1].trim());
                        db.insert(TimezonesReaderContract.CityEntry.TABLE_NAME, null, city);
                    }
                    return;
            }
        }


        private void loadSavedTimezones()
        {
            try {


                if (mList.size() == 0) {
                    SQLiteDatabase db = mTimezoneDbHelper.getReadableDatabase();
                    String countQuery = "SELECT  * FROM " + TimezonesReaderContract.SavedTimezoneEntry.TABLE_NAME;

                    Cursor cursor = db.rawQuery(countQuery, null);

                    int saved_timezone_count =cursor.getCount();
                    if (saved_timezone_count <= 0) return;

                    int index = 0;
                    while (cursor.moveToNext()) {
                        index++;

                        int id_index = cursor.getColumnIndex(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_COUNTRY_ID);
                        int city_name_index = cursor.getColumnIndex(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_CITY_NAME);
                        int is_home_timezone_index = cursor.getColumnIndex(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_ISHOMETIMEZONE);
                        int timezone_index = cursor.getColumnIndex(TimezonesReaderContract.SavedTimezoneEntry.COLUMN_NAME_CITY_TIMEZONE);

                        int id = cursor.getInt(id_index);
                        String city_name = cursor.getString(city_name_index);
                        int is_home_timezone = cursor.getInt(is_home_timezone_index);
                        String timezone = cursor.getString(timezone_index);

                        Bitmap bmp = getFlag(id);

                        CountryTimezone country = new CountryTimezone(id, city_name, bmp);
                        country.setTimezone(timezone);
                        country.setFlag(bmp);
                        country.setIsHomeTimezone(is_home_timezone == 1 ? true : false);

                        // if this is first item....then we take current system time.....
                        // else we calculate other timezone's time relative to first one
                        Date currentDateTime = null;
                        if(index == 1)
                        {
//                            country.setIsHomeTimezone(true);
                            currentDateTime = new Date();
                            MainActivity.m_IsHomeTimezoneSet = true;
                        }
                        else {
//                            country.setIsHomeTimezone(false);
                            currentDateTime = calcDateTime(mList.get(0).getTimezone(), timezone);
                        }

                        country.setDateTime(currentDateTime);

                        mList.add(country);
                    }

                    cursor.close();

                    mAdapter.notifyDataSetChanged();

                    setupAlarmManager();
//
//                    countQuery = "DELETE FROM " + TimezonesReaderContract.SavedTimezoneEntry.TABLE_NAME;
//                    cursor = db.rawQuery(countQuery, null);
//                    cursor.close();
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        private ArrayList<String> getCountries()
        {
            ArrayList<String> list = new ArrayList<>();
            list.add("Afghanistan, UTC+04:30, Kabul");
            list.add("Albania, UTC+01:00, Tirana");
            list.add("Algeria, UTC+01:00, Algiers");
            list.add("Andorra, UTC+01:00, AlgiersAndorra la Vella");
            list.add("Angola, UTC+01:00, Luanda");
            list.add("Antigua, UTC-04:00, Saint John's");
            list.add("Argentina, UTC-03:00, Buenos Aires");
            list.add("Armenia, UTC+04:00, Yerevan");
            list.add("Australia");
            list.add("Austria, UTC+01:00, Vienna");
            list.add("Azerbaijan, UTC+04:00, Baku");
            list.add("Bahamas, UTC-05:00, Nassau");
            list.add("Bahrain, UTC+03:00, Manama");
            list.add("Bangladesh, UTC+06:00, Dhaka");
            list.add("Barbados, UTC-04:00, Bridgetown");
            list.add("Belarus, UTC+03:00, Minsk");
            list.add("Belgium, UTC+01:00, Brussels");
            list.add("Belize, UTC-06:00, Belmopan");
            list.add("Benin, UTC+01:00, Porto-Novo");
            list.add("Bhutan, UTC+06:00, Thimphu");
            list.add("Bolivia, UTC-04:00, La Paz");
            list.add("Bosnia & Herzegovina, UTC+01:00, Sarajevo");
            list.add("Botswana, UTC+02:00, Gaborone");
            list.add("Brazil");
            list.add("Brunei, UTC+08:00, Bandar Seri Begawan");
            list.add("Bulgaria, UTC+02:00, Sofia");
            list.add("Burkina Faso, UTC+00:00, \tOuagadougou");
            list.add("Burundi, UTC+02:00, Bujumbura");
            list.add("Cambodia, UTC+07:00, Phnom Penh");
            list.add("Cameroon, UTC+01:00, Yaounde");
            list.add("Canada");
            list.add("Cape Verde, UTC-01:00, Praia");
            list.add("Chad, UTC+01:00, N'Djamena");
            list.add("Chile");
            list.add("China, UTC+08:00, Beijing");
            list.add("Colombia, UTC-05:00, Bogotá");
            list.add("Comoros, UTC+03:00, Moroni");
            list.add("Congo, UTC+01:00, Brazzaville");
            list.add("Costa Rica, UTC-06:00, San Jose");
            list.add("Cote d'Ivoire, UTC+00:00, \tYamoussoukro");
            list.add("Croatia, UTC+01:00, Zagreb");
            list.add("Cuba, UTC-05:00, Havana");
            list.add("Cyprus, UTC+02:00, Nicosia");
            list.add("Czech Republic, UTC+01:00, Prague");
            list.add("Denmark");
            list.add("Djibouti, UTC+03:00, Djibouti");
            list.add("Dominica, UTC-04:00, Roseau");
            list.add("Dominican Republic, UTC-04:00, Santo Domingo");
            list.add("Ecuador");
            list.add("Egypt, UTC+02:00, Cairo");
            list.add("El Salvador, UTC-06:00, San Salvador");
            list.add("Equatorial Guinea, UTC+01:00, Malabo");
            list.add("Eritrea, UTC+03:00, Asmara");
            list.add("Estonia, UTC+02:00, Tallinn");
            list.add("Ethiopia, UTC+03:00, Addis Ababa");
            list.add("Fiji, UTC+12:00, Suva");
            list.add("Finland, UTC+02:00, Helsinki");
            list.add("France");
            list.add("Gabon, UTC+01:00, Libreville");
            list.add("Gambia, UTC+00:00, \tBanjul");
            list.add("Georgia, UTC+04:00, Tbilisi");
            list.add("Germany, UTC+01:00, Berlin");
            list.add("Ghana, UTC+00:00, \tAccra");
            list.add("Greece, UTC+02:00, Athens");
            list.add("Grenada, UTC-04:00, Saint George's");
            list.add("Guatemala, UTC-06:00, Guatemala City");
            list.add("Guinea, UTC+00:00, \tConakry");
            list.add("Guinea-Bissau, UTC+00:00, \tBissau");
            list.add("Guyana, UTC-04:00, Georgetown");
            list.add("Haiti, UTC-05:00, Port-au-Prince");
            list.add("Honduras, UTC-06:00, Tegucigalpa");
            list.add("Hong Kong, UTC+08:00, Victoria City");
            list.add("Hungary, UTC+01:00, Budapest");
            list.add("Iceland, UTC+00:00, \tReykjavik");
            list.add("India, UTC+05:30, New Delhi");
            list.add("Indonesia");
            list.add("Iran, UTC+03:30, Tehran");
            list.add("Iraq, UTC+03:00, Baghdad");
            list.add("Ireland, UTC+00:00, \tDublin");
            list.add("Israel, UTC+02:00, Jerusalem");
            list.add("Italy, UTC+01:00, Rome");
            list.add("Ivory Coast, UTC+00:00, \tYamoussoukro");
            list.add("Jamaica, UTC-05:00, Kingston");
            list.add("Japan, UTC+09:00, Tokyo");
            list.add("Jordan, UTC+02:00, Amman");
            list.add("Kazakhstan");
            list.add("Kiribati");
            list.add("Kenya, UTC+03:00, Nairobi");
            list.add("North Korea, UTC+08:30, Pyongyang");
            list.add("South Korea, UTC+09:00, Seoul");
            list.add("Kuwait, UTC+03:00, Kuwait City");
            list.add("Kosovo, UTC+01:00, Pristina");
            list.add("Kyrgyzstan, UTC+06:00, Bishkek");
            list.add("Laos, UTC+07:00, Vientiane");
            list.add("Latvia, UTC+02:00, Riga");
            list.add("Lebanon, UTC+02:00, Beirut");
            list.add("Lesotho, UTC+02:00, Maseru");
            list.add("Liberia, UTC+00:00, \tMonrovia");
            list.add("Libya, UTC+02:00, Tripoli");
            list.add("Liechtenstein, UTC+01:00, Vaduz");
            list.add("Lithuania, UTC+02:00, Vilnius");
            list.add("Luxembourg, UTC+01:00, Luxembourg");
            list.add("Macau (China), UTC+08:00, Macau");
            list.add("Macedonia, UTC+01:00, Skopje");
            list.add("Madagascar, UTC+03:00, Antananarivo");
            list.add("Malawi, UTC+02:00, Lilongwe");
            list.add("Malaysia, UTC+08:00, Kuala Lumpur");
            list.add("Maldives, UTC+05:00, Male");
            list.add("Mali, UTC+00:00, \tBamako");
            list.add("Malta, UTC+01:00, Valletta");
            list.add("Marshall Islands, UTC+12:00, Majuro");
            list.add("Mauritania, UTC+00:00, \tNouakchott");
            list.add("Mauritius, UTC+04:00, Port Louis");
            list.add("Mexico");
            list.add("Micronesia");
            list.add("Moldova, UTC+02:00, Chisinau");
            list.add("Monaco, UTC+01:00, Monaco");
            list.add("Mongolia");
            list.add("Montenegro, UTC+01:00, Podgorica");
            list.add("Morocco, UTC+00:00, \tRabat");
            list.add("Mozambique, UTC+02:00, Maputo");
            list.add("Myanmar, UTC+06:30, Naypyidaw");
            list.add("Namibia, UTC+01:00, Windhoek");
            list.add("Nauru, UTC+12:00, Yaren District");
            list.add("Nepal, UTC+05:45, Kathmandu");
            list.add("Netherlands");
            list.add("New Zealand");
            list.add("Nicaragua, UTC-06:00, Managua");
            list.add("Niger, UTC+01:00, Niamey");
            list.add("Nigeria, UTC+01:00, Abuja");
            list.add("Norway, UTC+01:00, Oslo");
            list.add("Oman, UTC+04:00, Muscat");
            list.add("Pakistan, UTC+05:00, Islamabad");
            list.add("Palastine, UTC+02:00, Ramallah");
            list.add("Palau, UTC+09:00, Ngerulmud");
            list.add("Panama, UTC-05:00, Panama City");
            list.add("Papua New Guinea");
            list.add("Paraguay, UTC-04:00, Asunción");
            list.add("Peru, UTC-05:00, Lima");
            list.add("Philippines, UTC+08:00, Manila");
            list.add("Poland, UTC+01:00, Warsaw");
            list.add("Portugal");
            list.add("Qatar, UTC+03:00, Doha");
            list.add("Romania, UTC+02:00, Bucharest");
            list.add("Russia");
            list.add("Rwanda, UTC+02:00, Kigali");
            list.add("Saint Kitts and Nevis, UTC-04:00, Basseterre");
            list.add("Saint Lucia, UTC-04:00, Castries");
            list.add("Saint Vincent & Grenadines, UTC-04:00, Kingstown ");
            list.add("Samoa, UTC+13:00, Apia");
            list.add("San Marino, UTC+01:00, San Marino");
            list.add("São Tomé and Príncipe, UTC+00:00, \tSão Tomé");
            list.add("Saudi Arabia, UTC+03:00, Riyadh");
            list.add("Senegal, UTC+00:00, \tDakar");
            list.add("Serbia, UTC+01:00, Belgrade");
            list.add("Seychelles, UTC+04:00, Victoria");
            list.add("Sierra Leone, UTC+00:00, \tFreetown");
            list.add("Singapore, UTC+08:00, Singapore");
            list.add("Slovakia, UTC+01:00, Bratislava");
            list.add("Slovenia, UTC+01:00, Ljubljana");
            list.add("Solomon Islands, UTC+11:00, Honiara");
            list.add("Somalia, UTC+03:00, Mogadishu");
            list.add("South Sudan, UTC+03:00, Juba");
            list.add("Spain");
            list.add("South Africa");
            list.add("Sri Lanka, UTC+05:30, Columbo");
            list.add("Sudan, UTC+03:00, Khartoum");
            list.add("Suriname, UTC-03:00, Paramaribo");
            list.add("Swaziland, UTC+02:00, Mbabane");
            list.add("Sweden, UTC+01:00, Stockholm");
            list.add("Switzerland, UTC+01:00, Bern");
            list.add("Syria, UTC+02:00, Damascus");
            list.add("Taiwan, UTC+08:00, Taipei");
            list.add("Tajikistan, UTC+05:00, Dushanbe");
            list.add("Tanzania, UTC+03:00, Dodoma");
            list.add("Thailand, UTC+07:00, Bangkok");
            list.add("Timor Leste, UTC+09:00, Dili");
            list.add("Togo, UTC+00:00, \tLomé");
            list.add("Tonga, UTC+13:00, Nukuʻalofa");
            list.add("Trinidad and Tobago, UTC-04:00, Port of Spain");
            list.add("Tunisia, UTC+01:00, Tunis");
            list.add("Turkey, UTC+03:00, Ankara");
            list.add("Turkmenistan, UTC+05:00, Ashgabat");
            list.add("Tuvalu, UTC+12:00, Funafuti");
            list.add("Uganda, UTC+03:00, Kampala");
            list.add("Ukraine, UTC+02:00, Kyiv");
            list.add("United Arab Emirates, UTC+04:00, Abu Dhabi");
            list.add("United Kingdom");
            list.add("United States");
            list.add("Uruguay, UTC-03:00, Montevideo");
            list.add("Uzbekistan, UTC+05:00, Tashkent");
            list.add("Vanuatu, UTC+11:00, Port Vila");
            list.add("Vatican City, UTC+01:00, Vatican City");
            list.add("Venezuela, UTC-04:00, Caracas");
            list.add("Vietnam, UTC+07:00, Hanoi");
            list.add("Yemen, UTC+03:00, Sana'a");
            list.add("Zambia, UTC+02:00, Lusaka");
            list.add("Zimbabwe, UTC+02:00, Harare");

             return list;
        }
    }
}
