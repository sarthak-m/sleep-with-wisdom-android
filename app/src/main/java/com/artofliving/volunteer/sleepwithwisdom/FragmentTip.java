package com.artofliving.volunteer.sleepwithwisdom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentTip extends Fragment {

	TextView tv;
	ImageView iv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.fragment_tip,
				container, false);
		tv = (TextView) myFragmentView.findViewById(R.id.textView1);
		iv = (ImageView) myFragmentView.findViewById(R.id.imageView1);

		Tracker mTracker;

		switch (getArguments().getInt("number")) {
		case 1:
			tv.setText("• Set a regular bedtime \n\n"
					+ "• Wake up at the same time every day \n\n"
					+ "• Nap during the day - only if you must, to make up for lost sleep\n\n"
					+ "• Fight after-dinner drowsiness. Do something mildly stimulating to avoid falling asleep early such as washing the dishes, getting clothes ready for next day or reading a book.");
			iv.setImageResource(R.drawable.tips_ic_habit);

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Habits Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		case 2:
			tv.setText("• Turn off your television and computer. Instead, listen to some music, read a book or practice relaxation exercises.\n\n"
					+ "• Don’t read from a backlit device (Mobiles, iPad) at night\n\n"
					+ "• When it’s time to sleep, make sure the room is dark.\n\n"
					+ "• Spend more time outside during daylight.\n\n"
					+ "• Let as much light into your home/workspace as possible\n\n"
					+ "• Keep noise or any other disturbances down to minimum.");
			iv.setImageResource(R.drawable.tips_ic_general);

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("General Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		case 3:
			tv.setText("• Eat right and have somehting light for dinner\n\n"
					+ "• Avoid heavy foods and big meals late in the day\n\n"
					+ "• Finish any snack at least an hour before bed\n\n"
					+ "• If you must eat late in the night or wake up from sleep due to hunger, have low-fat milk, yogurt or a banana\n\n"
					+ "• Rethink your drink - Warm milk is a better beverage choice in the evening.\n\n"
					+ "• Avoid alcohol and Cigarettes- after the initial effects wear off, alcohol and cigarettes actually cause more frequent awakenings at night and less restful sleep.\n\n"
					+ "• Stop Sipping After 8 pm - Nothing to drink within two hours of bedtime. This will help prevent the mid-night bathroom trips.");
			iv.setImageResource(R.drawable.tips_ic_food);

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Food Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		case 4:
			tv.setText("• Deep breathing for few minutes helps a lot for a good sleep.\n\n"
					+ "• Include physical activity (but not within four hours of bed time) in your daily routine. This promotes relaxing sleep at night.\n\n"
					+ "• Lower the lights 2-3 hours prior to bed time - It signals your brain to produce melatonin, the hormone that brings on sleep.\n\n"
					+ "• Free your mind at bedtime - read something calm, meditate, listen to music, or take a warm bath.");
			iv.setImageResource(R.drawable.tips_ic_relaxation);

			mTracker = ((SleepWithWisdom) getActivity().getApplication())
					.getDefaultTracker();
			mTracker.setScreenName("Relaxation Tips Fragment");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

			break;
		}

		return myFragmentView;
	}

}
