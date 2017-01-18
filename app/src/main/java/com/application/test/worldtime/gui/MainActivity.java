package com.application.test.worldtime.gui;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
        import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.CheckBox;

        import com.application.test.worldtime.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener
{
    static boolean m_IsClosingApp = true;
    static boolean m_IsHomeTimezoneSet = false;

    CheckBox mChkUseCurrentTime = null;

    private FragmentDrawer mDrawerFragment;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), myToolbar);
        mDrawerFragment.setDrawerListener(this);

        displayView(0);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
//        if(m_IsClosingApp)
//        {
//            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
//            TimezoneDbHelper.getInstance(this).close();
//            TimezoneDbHelper.getInstance(this).close();
//        }
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return false;
    }

    public void displayView(int position)
    {
        android.support.v4.app.Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position)
        {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.drawer_title_home);
                break;
            case 1:
                fragment = new NewTimezoneFragment();
                title = getString(R.string.drawer_title_friends);
                break;
            case 2:
                fragment = new AboutFragment();
                title = getString(R.string.drawer_title_messages);
                break;
            default:
                break;
        }

        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position)
    {
        displayView(position);
    }
}
