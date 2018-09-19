package com.artofliving.volunteer.sleepwithwisdom.includes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.artofliving.volunteer.sleepwithwisdom.R;

public class Utils {

	@SuppressLint("InflateParams")
	public static AlertDialog showLoadingDialog(Activity activity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

		LayoutInflater inflater = activity.getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.dialog_loading, null);
		dialog.setView(dialogView);

		AlertDialog loadingDialog;

		loadingDialog = dialog.create();
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(false);
		loadingDialog.show();

		return loadingDialog;
	}

	public static void sharetip(String tip_text, Activity activity) {
		tip_text = activity.getResources().getString(R.string.share_extra_text)
				+ "\n\n" + tip_text;
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT, tip_text);
		try {
			activity.startActivity(Intent.createChooser(i, "Share via"));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity, "No sharing app found on your phone!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void shareapp(String string, Activity activity) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT, string);
		try {
			activity.startActivity(Intent.createChooser(i, "Share via"));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity, "No sharing app found on your phone!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static boolean isInternetAvailable(Activity activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
