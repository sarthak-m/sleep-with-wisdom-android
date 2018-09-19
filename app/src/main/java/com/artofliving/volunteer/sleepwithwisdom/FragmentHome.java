package com.artofliving.volunteer.sleepwithwisdom;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentHome extends Fragment {

    LinearLayout letsBegin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        letsBegin = (LinearLayout) view.findViewById(R.id.llLetsBegin);
        letsBegin.setClickable(true);

        letsBegin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Tracker mTracker;
                mTracker = ((SleepWithWisdom) getActivity().getApplication())
                        .getDefaultTracker();
                mTracker.setScreenName("Begin clicked from Home Fragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());

                Fragment fragment = new FragmentSleep();
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment)
                        .commit();
                getActivity().setTitle(HomeActivity.TITLE_SLEEP);
            }

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("Home");
        }
    }

}
