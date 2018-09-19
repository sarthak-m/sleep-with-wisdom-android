package com.artofliving.volunteer.sleepwithwisdom;

import java.util.HashMap;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class SleepWithWisdom extends Application {

	private Tracker mTracker;

	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-63890170-1";

	// Logging TAG
	// private static final String TAG = "DonorApp";

	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
						// roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company.
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public SleepWithWisdom() {
		super();
	}

	synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
					.newTracker(R.xml.app_tracker)
					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
							.newTracker(PROPERTY_ID) : analytics
							.newTracker(R.xml.app_tracker);
			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "U8PngHEo8pUNjOEPdBA7BEKEdDq9eKF4Q6wDI8bb",
				"sXpUIdCDqtL5J0xjEmsYYaJBkeX5zqKaCIPgrm3G");

		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("com.parse.push",
							"successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});
	}

	/**
	 * Gets the default {@link Tracker} for this {@link Application}.
	 * 
	 * @return tracker
	 */
	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
			mTracker = analytics.newTracker(R.xml.app_tracker);
		}
		return mTracker;
	}

}
