package com.artofliving.volunteer.sleepwithwisdom.includes;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.artofliving.volunteer.sleepwithwisdom.R;

public class CustomPagerAdapter extends PagerAdapter {

	Context mContext;
	LayoutInflater mLayoutInflater;
	int[] _appImages;
	String[] _links;
	
	public CustomPagerAdapter(Context context, int[] appImages, String[] links) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_appImages = appImages;
		_links = links;
	}

	@Override
	public int getCount() {
		return _appImages.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View itemView = mLayoutInflater.inflate(R.layout.imagepager_item,
				container, false);

		ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
		imageView.setImageResource(_appImages[position]);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Toast.makeText(mContext, _links[position], Toast.LENGTH_LONG).show();
					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(_links[position]));
					mContext.startActivity(myIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(mContext,
							"No Web-browser detected. Please install one!",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		container.addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout) object);
	}
}
