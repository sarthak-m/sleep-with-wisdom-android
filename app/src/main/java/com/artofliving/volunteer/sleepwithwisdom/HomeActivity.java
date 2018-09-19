package com.artofliving.volunteer.sleepwithwisdom;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.artofliving.volunteer.sleepwithwisdom.includes.CustomDrawerAdapter;
import com.artofliving.volunteer.sleepwithwisdom.includes.DrawerItem;
import com.artofliving.volunteer.sleepwithwisdom.includes.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    public static final String TITLE_HOME = "Sleep with Wisdom";
    public static final String TITLE_SLEEP = "Deep Meditative Sleep";
    public static final String TITLE_TIPS = "Tips for Better Sleep";
    public static final String TITLE_LEARN = "Learn Yoga / Meditation";
    public static final String TITLE_MEDITATE = "Meditate Now";
    public static final String TITLE_CONNECT = "Connect with Sri Sri";
    public static final String TITLE_ABOUT_US = "About Us";
    public static final String TITLE_FEEDBACK = "Feedback";
    public static final String TITLE_SHARE = "Share the happiness!";
    public static final String PLAY_STORE_LINK_SRI_SRI = "https://play.google.com/store/apps/details?id=com.artofliving.srisrinews";
    public static final String PLAY_STORE_LINK_SLEEP_WITH_WISDOM = "https://play.google.com/store/apps/details?id=com.artofliving.volunteer.sleepwithwisdom";

    SharedPreferences setPrefs;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        setPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        int counter = Integer.parseInt(setPrefs.getString("counter", "0"));
        SharedPreferences.Editor edit = setPrefs.edit();

        if (!setPrefs.getBoolean("dnd", false)) {
            if (setPrefs.getString("counter", "0").contains("2")) {
                edit.putString("counter", "0");
                edit.commit();
                rateUs();
            } else {
                edit.putString("counter", String.valueOf(++counter));
                edit.commit();
            }
        }

        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        dataList.add(new DrawerItem("Home", R.drawable.ic_launcher));
        dataList.add(new DrawerItem(TITLE_SLEEP, R.drawable.icon_sleep));
        dataList.add(new DrawerItem(TITLE_TIPS, R.drawable.icon_tips));
        dataList.add(new DrawerItem(TITLE_LEARN, R.drawable.icon_learn));
        dataList.add(new DrawerItem(TITLE_MEDITATE, R.drawable.icon_meditate));
        dataList.add(new DrawerItem(TITLE_CONNECT, R.drawable.ic_srisri));
        dataList.add(new DrawerItem(TITLE_ABOUT_US, R.drawable.icon_about_us));
        dataList.add(new DrawerItem(TITLE_FEEDBACK, R.drawable.icon_feedback));
        dataList.add(new DrawerItem(TITLE_SHARE, R.drawable.icon_share));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        Drawable d = getResources().getDrawable(R.drawable.actionbar_bg);
        getSupportActionBar().setBackgroundDrawable(d);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(0);
        }

        // Get a Tracker (should auto-report)
        ((SleepWithWisdom) getApplication())
                .getTracker(SleepWithWisdom.TrackerName.APP_TRACKER);

    }

    private void rateUs() {
        View checkBoxView = View.inflate(this, R.layout.dialog_rate_app, null);
        final CheckBox checkBox = (CheckBox) checkBoxView
                .findViewById(R.id.cbDND);

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Like the App?");
        build.setMessage("Please take few seconds to share your valuable feedback with us");
        build.setView(checkBoxView);
        build.setPositiveButton("RATE US", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "https://play.google.com/store/apps/details?id=com.artofliving.volunteer.sleepwithwisdom";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse(url));
                startActivity(browserIntent);
                // SharedPreferences.Editor edit = setPrefs.edit();
                // edit.putBoolean("dnd", true);
                // edit.commit();
            }
        });

        build.setNegativeButton("CANCEL", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // will cancel the dialog automatically
                if (checkBox.isChecked()) {
                    SharedPreferences.Editor edit = setPrefs.edit();
                    edit.putBoolean("dnd", true);
                    edit.commit();
                }
            }
        });
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        // Get an Analytics tracker to report app starts & uncaught exceptions
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        // Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    public void SelectItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentHome();
                setTitle(TITLE_HOME);
                break;

            case 1:
                fragment = new FragmentSleep();
                setTitle(TITLE_SLEEP);
                break;

            case 2:
                Intent i = new Intent(HomeActivity.this, TipsFragmentActivity.class);
                startActivityForResult(i, 0);
                break;

            case 3:
                fragment = new FragmentLearn();
                setTitle(TITLE_LEARN);
                break;

            case 4:
                fragment = new FragmentMeditate();
                setTitle(TITLE_MEDITATE);
                break;

            case 5:
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(PLAY_STORE_LINK_SRI_SRI));
                startActivity(myIntent);
                break;

            case 6:
                fragment = new FragmentAboutUs();
                setTitle(TITLE_ABOUT_US);
                break;

            case 7:
                fragment = new FragmentFeedback();
                setTitle(TITLE_FEEDBACK);
                break;
            case 8:
                Utils.shareapp(getResources().getString(R.string.share_app_text),
                        HomeActivity.this);
                break;
        }
        if (fragment != null) {
            FragmentManager frgManager = getSupportFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

        }
    }

    // override the methods for defined purpose
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        }
    }

    // override the methods for defined purpose
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    // override the methods for defined purpose
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        return mDrawerToggle.onOptionsItemSelected(item);

    }

    // override the methods for defined purpose
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // override the methods for defined purpose
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            SelectItem(0);
        } else if (requestCode == 1) {
            SelectItem(1);
        }
    }

}
