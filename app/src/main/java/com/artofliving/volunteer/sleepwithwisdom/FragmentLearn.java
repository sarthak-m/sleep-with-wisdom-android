package com.artofliving.volunteer.sleepwithwisdom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.artofliving.volunteer.sleepwithwisdom.includes.Utils;
import com.artofliving.volunteer.sleepwithwisdom.includes.Validation;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FragmentLearn extends Fragment {

	AutoCompleteTextView autoCompView;
	String[] countries;
	AlertDialog uploadingDialog;

	// FORM ELEMENTS
	private EditText etName;
	private EditText etEmail;
	private EditText etPhone;
	private EditText etCity;
	private Button bSubmit, fillAnother;
	private CheckBox cbKriya;

	SharedPreferences setPrefs;
	SharedPreferences.Editor editor;

	private static final String KEY_URL = "https://docs.google.com/forms/d/1grQ8YhzRd-ayUnmssYS-1yP4DRYFQcXMT3fU7ey1278/formResponse";
	private static final String LEAD_SQUARE_URL = "http://iloveseva.org/leadsquare/sleepwithwisdomleads.php";

	private static final String KEY_NAME = "entry.521537101";
	private static final String KEY_EMAIL = "entry.1828714625";
	private static final String KEY_KRIYA = "entry.685687865";
	private static final String KEY_PHONE = "entry.2103594098";
	private static final String KEY_CITY = "entry.1296685923";
	private static final String KEY_COUNTRY = "entry.1895271340";

	public FragmentLearn() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// init preferences
		setPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getBaseContext());
		editor = setPrefs.edit();

		View view;

		if (!setPrefs.getBoolean("learn", false)) {
			view = inflater.inflate(R.layout.fragment_learn, container, false);
			autoCompView = (AutoCompleteTextView) view
					.findViewById(R.id.acTvCountryLearn);

			countries = getActivity().getResources().getStringArray(
					R.array.countries_array);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1,
					countries);
			autoCompView.setAdapter(adapter);

			initFormFields(view);
			registerViews(view);

		} else {
			view = inflater
					.inflate(R.layout.fragment_learned, container, false);
			fillAnother = (Button) view.findViewById(R.id.bFillAnother);
			fillAnother.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					editor.putBoolean("learn", false);
					editor.commit();
					Fragment fragment = new FragmentLearn();
					FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment)
							.commit();
					getActivity().setTitle(HomeActivity.TITLE_LEARN);
				}
			});
		}

		Tracker mTracker;
		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("Learn Fragment");
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

		etCity.addTextChangedListener(new TextWatcher() {
			// after every change has been made to this editText, we would like
			// to check validity
			@Override
			public void afterTextChanged(Editable s) {
				if (checkValidation())
					bSubmit.setTextColor(Color.parseColor("#FFFFFF"));
				else
					bSubmit.setTextColor(Color.parseColor("#666666"));
				Validation.isNormalText(etCity, true);
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
					Toast.makeText(
							getActivity(),
							"Form contains some error. Please check your details.",
							Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void submitForm() {

		if (Utils.isInternetAvailable(getActivity())) {

			try {

				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				String lead_date = df.format(c.getTime());

				String kriya = ((cbKriya.isChecked()) ? "Yes" : "No");
				String getParameters = "?";
				// ?number=8147753491&name=Sarthak%20Majithia&email=sarthakmajithia%40gmail.com";
				getParameters = getParameters + "leaddate=";
				getParameters = getParameters
						+ URLEncoder.encode(lead_date, "UTF-8");
				getParameters = getParameters + "&kriya=";
				getParameters = getParameters + kriya;
				getParameters = getParameters + "&name=";
				getParameters = getParameters + etName.getText().toString();
				getParameters = getParameters + "&phone=";
				getParameters = getParameters + etPhone.getText().toString();
				getParameters = getParameters + "&email=";
				getParameters = getParameters
						+ URLEncoder.encode(etEmail.getText().toString(),
								"UTF-8");
				getParameters = getParameters + "&city=";
				getParameters = getParameters + etCity.getText().toString();
				getParameters = getParameters + "&state=";
				getParameters = getParameters + "";
				getParameters = getParameters + "&country=";
				getParameters = getParameters
						+ autoCompView.getEditableText().toString();
				getParameters = getParameters + "&coursetype=";
				getParameters = getParameters + "";
				getParameters = getParameters + "&notes=";
				getParameters = getParameters + "";
				getParameters = getParameters + "&area=";
				getParameters = getParameters + "";
				getParameters = getParameters + "&enquiry=";
				getParameters = getParameters + "";

				new SubmitToLeadSquare().execute(getParameters);
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

	private class SubmitToLeadSquare extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			uploadingDialog = Utils.showLoadingDialog(getActivity());
		}

		@Override
		protected String doInBackground(String... params) {
			String url = LEAD_SQUARE_URL + params[0];

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(url);

				@SuppressWarnings("unused")
				HttpResponse response = client.execute(request);

			} catch (UnsupportedEncodingException e) {
				return e.toString();
			} catch (ClientProtocolException e) {
				return e.toString();
			} catch (IOException e) {
				return e.toString();
			}

			return "Sample string so that onPostExecute does not crash";
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {

			String kriya = ((cbKriya.isChecked()) ? "Yes" : "No");

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair(KEY_NAME, etName
					.getText().toString()));
			postParameters.add(new BasicNameValuePair(KEY_EMAIL, etEmail
					.getText().toString()));
			postParameters.add(new BasicNameValuePair(KEY_CITY, etCity
					.getText().toString()));
			postParameters.add(new BasicNameValuePair(KEY_PHONE, etPhone
					.getText().toString()));
			postParameters.add(new BasicNameValuePair(KEY_KRIYA, kriya));
			postParameters.add(new BasicNameValuePair(KEY_COUNTRY, autoCompView
					.getEditableText().toString()));

			try {
				new SubmitForm().execute(postParameters);
			} catch (Exception e) {
				Toast.makeText(getActivity(),
						"Some error occured. Please try again later!",
						Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}

	}

	private class SubmitForm extends
			AsyncTask<ArrayList<NameValuePair>, Void, String> {

		@Override
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			ArrayList<NameValuePair> postParameters = params[0];

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(KEY_URL);

				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);

				@SuppressWarnings("unused")
				HttpResponse response = client.execute(request);

			} catch (UnsupportedEncodingException e) {
				return e.toString();
			} catch (ClientProtocolException e) {
				return e.toString();
			} catch (IOException e) {
				return e.toString();
			}

			return "Sample string so that onPostExecute does not crash";
		}

		@Override
		protected void onPostExecute(String result) {
			uploadingDialog.dismiss();

			// save locally that form has been filled already
			editor.putBoolean("learn", true);
			editor.commit();

			Fragment fragment = new FragmentLearn();
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame, fragment).commit();
			getActivity().setTitle(HomeActivity.TITLE_LEARN);
			super.onPostExecute(result);
		}
	}

	private boolean checkValidation() {
		boolean ret = true;

		if (!Validation.isNormalText(etCity, true))
			ret = false;
		if (!Validation.hasText(etName))
			ret = false;
		if (!Validation.isEmailAddress(etEmail, true))
			ret = false;

		return ret;
	}

	private void initFormFields(View view) {
		etName = (EditText) view.findViewById(R.id.etNameLearn);
		etEmail = (EditText) view.findViewById(R.id.etEmailLearn);
		etPhone = (EditText) view.findViewById(R.id.etPhoneLearn);
		etCity = (EditText) view.findViewById(R.id.etCityLearn);
		bSubmit = (Button) view.findViewById(R.id.bSubmitLearn);
		cbKriya = (CheckBox) view.findViewById(R.id.cbKriyaLearn);
	}

}