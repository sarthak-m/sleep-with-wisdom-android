package com.artofliving.volunteer.sleepwithwisdom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentAboutUs extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_about_us, container,
				false);

		TextView about_us;
		about_us = (TextView) view.findViewById(R.id.about_us);
		Linkify.addLinks(about_us, Linkify.WEB_URLS);

		Tracker mTracker;
		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("About Us Fragment");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		return view;

	}

	@Override
	public void onResume() {
		super.onResume();
		// Set title
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(HomeActivity.TITLE_ABOUT_US);
		}
	}

}
