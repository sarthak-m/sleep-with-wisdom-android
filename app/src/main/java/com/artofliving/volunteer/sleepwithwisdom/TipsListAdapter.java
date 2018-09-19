package com.artofliving.volunteer.sleepwithwisdom;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.artofliving.volunteer.sleepwithwisdom.includes.Utils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TipsListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public TipsListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.fragment_tip_list_row, null);

		TextView tip = (TextView) vi.findViewById(R.id.tiptext); // title
		ImageView share = (ImageView) vi.findViewById(R.id.share);

		HashMap<String, String> tipList = new HashMap<String, String>();
		tipList = data.get(position);

		final String tip_text = tipList.get(FragmentTipNew.KEY_TIP_TEXT);

		// Setting all values in listview
		tip.setText(tip_text);

		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Tracker mTracker;
				mTracker = ((SleepWithWisdom) activity.getApplication())
						.getDefaultTracker();
				mTracker.setScreenName("Tip Shared");
				mTracker.send(new HitBuilders.ScreenViewBuilder().build());
				Utils.sharetip(tip_text.substring(2), activity);
			}
		});

		// Animation animation = AnimationUtils.loadAnimation(activity,
		// R.anim.fade_in);
		// animation.setDuration(750);
		// vi.setVisibility(View.VISIBLE);
		// vi.startAnimation(animation);
		// animation = null;

		return vi;
	}

}
