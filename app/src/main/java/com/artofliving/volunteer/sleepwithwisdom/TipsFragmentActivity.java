package com.artofliving.volunteer.sleepwithwisdom;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TipsFragmentActivity extends AppCompatActivity {

    private Tracker mTracker;

    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

    String TabFragmentB;

    public void setTabFragmentB(String t) {
        TabFragmentB = t;
    }

    public String getTabFragmentB() {
        return TabFragmentB;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        setContentView(mViewPager);

        final ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_TITLE);
        // back arrow button
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        // title
        bar.setDisplayShowTitleEnabled(true);
        bar.setTitle("Tips for Better Sleep");
        // action bar background
        Drawable d = getResources().getDrawable(R.drawable.actionbar_bg);
        bar.setBackgroundDrawable(d);

        mTabsAdapter = new TabsAdapter(this, mViewPager);

        Bundle args1 = new Bundle();
        Bundle args2 = new Bundle();
        Bundle args3 = new Bundle();
        Bundle args4 = new Bundle();
        args1.putInt("number", 1);
        args2.putInt("number", 2);
        args3.putInt("number", 3);
        args4.putInt("number", 4);

        mTabsAdapter.addTab(bar.newTab().setText("General"),
                FragmentTipNew.class, args2);
        mTabsAdapter.addTab(bar.newTab().setText("Habits"),
                FragmentTipNew.class, args1);
        mTabsAdapter.addTab(bar.newTab().setText("Food"), FragmentTipNew.class,
                args3);
        mTabsAdapter.addTab(bar.newTab().setText("Relaxation Techniques"),
                FragmentTipNew.class, args4);
        mTabsAdapter.addTab(bar.newTab().setText("Yoga & Pranayam"),
                FragmentTipYogaAndPranayam.class, null);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

        // Get a Tracker (should auto-report)
        ((SleepWithWisdom) getApplication())
                .getTracker(SleepWithWisdom.TrackerName.APP_TRACKER);
        mTracker = ((SleepWithWisdom) getApplication()).getDefaultTracker();
        mTracker.setScreenName("Tips Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar()
                .getSelectedNavigationIndex());
    }

    public static class TabsAdapter extends FragmentPagerAdapter implements
            ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final Context mContext;
        private final android.support.v7.app.ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(AppCompatActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(),
                    info.args);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab,
                                    android.support.v4.app.FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab,
                                  android.support.v4.app.FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i = 0; i < mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab,
                                    android.support.v4.app.FragmentTransaction ft) {
        }

    }

}