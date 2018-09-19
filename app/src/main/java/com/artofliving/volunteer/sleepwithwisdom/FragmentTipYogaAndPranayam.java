package com.artofliving.volunteer.sleepwithwisdom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artofliving.volunteer.sleepwithwisdom.includes.Utils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentTipYogaAndPranayam extends Fragment implements
		OnClickListener {

	TextView tv1, tv2, tv3;
	ImageView share1, share2, share3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(
				R.layout.fragment_tip_yoga_pranayam, container, false);
		tv1 = (TextView) myFragmentView.findViewById(R.id.textView1);
		share1 = (ImageView) myFragmentView.findViewById(R.id.shareYoga1);
		tv2 = (TextView) myFragmentView.findViewById(R.id.textView2);
		share2 = (ImageView) myFragmentView.findViewById(R.id.shareYoga2);
		tv3 = (TextView) myFragmentView.findViewById(R.id.textView3);
		share3 = (ImageView) myFragmentView.findViewById(R.id.shareYoga3);

		tv1.setText("• Helps stretch the back muscles, invigorates the nervous system by increasing blood supply, and makes the spine supple.\n\n"
				+ "Cat Stretch (Marjariasana):\n\n"
				+ "• Excellent stretch for spine flexibility. Also helps massage the digestive organs and improve digestion, thereby helping you to sleep well. It even improves blood circulation and relaxes the mind.");
		tv2.setText("• A deeply relaxing stretch for the back which also helps calm the nervous system so that you can sleep at peace.");
		tv3.setText("• This pose can help remove fatigue from long hours of standing or walking. Also a good stretch for inner thighs, groin and knees.");

		Tracker mTracker;
		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("Yoga and Pranayam Fragment");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		share1.setOnClickListener(this);
		share2.setOnClickListener(this);
		share3.setOnClickListener(this);
		return myFragmentView;

	}

	@Override
	public void onClick(View v) {

		Tracker mTracker;
		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("Tip Shared");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		switch (v.getId()) {
		case R.id.shareYoga1:
			Utils.sharetip("Forward Bend OR Hasta-padasana\n\n"
					+ tv1.getText().toString().substring(2), getActivity());
			break;

		case R.id.shareYoga2:
			Utils.sharetip("Child Pose OR Shishu-asana\n\n"
					+ tv2.getText().toString().substring(2), getActivity());
			break;

		case R.id.shareYoga3:
			Utils.sharetip("Butterfly Pose OR Baddha-Konasana\n\n"
					+ tv3.getText().toString().substring(2), getActivity());
			break;
		}
	}
}
