package com.artofliving.volunteer.sleepwithwisdom;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentMeditate extends Fragment implements OnClickListener {

	private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=com.meditation.tracker.android&hl=en";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_meditate, container,
				false);

		view.setClickable(true);
		view.setOnClickListener(this);
		
		Tracker mTracker;
		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("Meditation Fragment");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		return view;

	}

	@Override
	public void onClick(View v) {
		try {
			Tracker mTracker;
			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Sattva Link clicked");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			Intent myIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(PLAY_STORE_LINK));
			startActivity(myIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(getActivity(),
					"No Web-browser detected. Please install one first",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Set title
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(HomeActivity.TITLE_MEDITATE);
		}
	}

}
