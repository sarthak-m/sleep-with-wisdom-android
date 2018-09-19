package com.artofliving.volunteer.sleepwithwisdom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artofliving.volunteer.sleepwithwisdom.includes.Utils;
import com.artofliving.volunteer.sleepwithwisdom.includes.Validation;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentFeedback extends Fragment {

	// FORM ELEMENTS
	private EditText etName;
	private EditText etEmail;
	private EditText etFeedback;
	private Button bSubmit, fillAnother;

	SharedPreferences setPrefs;
	SharedPreferences.Editor editor;

	private static final String KEY_URL = "https://docs.google.com/forms/d/16AI5rruRaXNuESHcynGX3FpNxOCNw_y4zZi17iJsREk/formResponse";

	private static final String KEY_NAME = "entry.521537101";
	private static final String KEY_EMAIL = "entry.1828714625";
	private static final String KEY_FEEDBACK = "entry.1296685923";

	public FragmentFeedback() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// init preferences
		setPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getBaseContext());
		editor = setPrefs.edit();

		View view;

		if (!setPrefs.getBoolean("feedback", false)) {
			view = inflater.inflate(R.layout.fragment_feedback, container,
					false);

			initFormFields(view);
			registerViews(view);

		} else {
			view = inflater.inflate(R.layout.fragment_feedback_given,
					container, false);
			fillAnother = (Button) view.findViewById(R.id.bFillAnother);
			fillAnother.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					editor.putBoolean("feedback", false);
					editor.commit();
					Fragment fragment = new FragmentFeedback();
					FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment)
							.commit();
					getActivity().setTitle(HomeActivity.TITLE_FEEDBACK);
				}
			});
		}

		Tracker mTracker;
		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("Feedback Fragment");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		return view;
	}

	private void registerViews(View view) {

		// TextWatcher would let us check validation error on the fly
		etName.addTextChangedListener(new TextWatcher() {
			// after every change has been made to this editText, we would like
			// to check validity
			@Override
			public void afterTextChanged(Editable s) {
				if (checkValidation())
					bSubmit.setTextColor(Color.parseColor("#FFFFFF"));
				else
					bSubmit.setTextColor(Color.parseColor("#666666"));
				Validation.isNormalText(etName, true);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		etEmail.addTextChangedListener(new TextWatcher() {
			// after every change has been made to this editText, we would like
			// to check validity
			@Override
			public void afterTextChanged(Editable s) {
				if (checkValidation())
					bSubmit.setTextColor(Color.parseColor("#FFFFFF"));
				else
					bSubmit.setTextColor(Color.parseColor("#666666"));
				Validation.isEmailAddress(etEmail, true);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		etFeedback.addTextChangedListener(new TextWatcher() {
			// after every change has been made to this editText, we would like
			// to check validity
			@Override
			public void afterTextChanged(Editable s) {
				if (checkValidation())
					bSubmit.setTextColor(Color.parseColor("#FFFFFF"));
				else
					bSubmit.setTextColor(Color.parseColor("#666666"));
				Validation.hasText(etFeedback);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		bSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkValidation())
					submitForm();
				else
					Toast.makeText(getActivity(),
							"Please don't leave any fields blank.",
							Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void submitForm() {

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(KEY_NAME, etName.getText()
				.toString()));
		postParameters.add(new BasicNameValuePair(KEY_EMAIL, etEmail.getText()
				.toString()));
		postParameters.add(new BasicNameValuePair(KEY_FEEDBACK, etFeedback
				.getText().toString()));

		if (Utils.isInternetAvailable(getActivity())) {
			try {
				new SubmitForm().execute(postParameters);
			} catch (Exception e) {
				Toast.makeText(getActivity(),
						"Some error occured. Please try again later!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getActivity(), "Couldn't connect to Internet",
					Toast.LENGTH_LONG).show();
		}
	}

	private class SubmitForm extends
			AsyncTask<ArrayList<NameValuePair>, Void, String> {

		AlertDialog uploadingDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.uploadingDialog = Utils.showLoadingDialog(getActivity());
		}

		@Override
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			ArrayList<NameValuePair> postParameters = params[0];

			BufferedReader in = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(KEY_URL);

				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);
				HttpResponse response = client.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.seperator");

				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String result = sb.toString().trim();
				return result;

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			this.uploadingDialog.dismiss();

			// save locally that form has been filled already
			editor.putBoolean("feedback", true);
			editor.commit();

			Fragment fragment = new FragmentFeedback();
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame, fragment).commit();
			getActivity().setTitle(HomeActivity.TITLE_FEEDBACK);
			super.onPostExecute(result);
		}
	}

	private boolean checkValidation() {
		boolean ret = true;

		if (!Validation.hasText(etFeedback))
			ret = false;
		if (!Validation.isNormalText(etName, true))
			ret = false;
		if (!Validation.isEmailAddress(etEmail, true))
			ret = false;

		return ret;
	}

	private void initFormFields(View view) {
		etName = (EditText) view.findViewById(R.id.etFeedbackName);
		etEmail = (EditText) view.findViewById(R.id.etFeedbackEmail);
		etFeedback = (EditText) view.findViewById(R.id.etFeedback);
		bSubmit = (Button) view.findViewById(R.id.bSubmit);
	}

}