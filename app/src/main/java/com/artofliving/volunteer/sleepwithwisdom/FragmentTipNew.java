package com.artofliving.volunteer.sleepwithwisdom;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentTipNew extends Fragment {

	ImageView iv;
	ListView list;

	public static final String KEY_TIP_TEXT = "tip";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.fragment_tip_new,
				container, false);
		iv = (ImageView) myFragmentView.findViewById(R.id.image);
		list = (ListView) myFragmentView.findViewById(R.id.list);

		Tracker mTracker;

		String[] tips;

		ArrayList<HashMap<String, String>> tipsList = new ArrayList<HashMap<String, String>>();

		switch (getArguments().getInt("number")) {
		case 1:
			// Habit
			iv.setImageResource(R.drawable.tips_ic_habit);

			tips = getResources().getStringArray(R.array.tips_habits);

			for (int i = 0; i < tips.length; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tip", tips[i]);
				tipsList.add(map);
			}

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Habits Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		case 2:
			// General
			iv.setImageResource(R.drawable.tips_ic_general);

			tips = getResources().getStringArray(R.array.tips_general);

			for (int i = 0; i < tips.length; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tip", tips[i]);
				tipsList.add(map);
			}

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("General Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		case 3:
			// food

			iv.setImageResource(R.drawable.tips_ic_food);

			tips = getResources().getStringArray(R.array.tips_food);

			for (int i = 0; i < tips.length; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tip", tips[i]);
				tipsList.add(map);
			}

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Food Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		case 4:
			// relax

			iv.setImageResource(R.drawable.tips_ic_relaxation);

			tips = getResources().getStringArray(R.array.tips_relax);

			for (int i = 0; i < tips.length; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(KEY_TIP_TEXT, tips[i]);
				tipsList.add(map);
			}

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Relaxation Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		}

		TipsListAdapter adapter = new TipsListAdapter(getActivity(), tipsList);
		list.setAdapter(adapter);

		return myFragmentView;
	}

}
