package com.widget.period_widget_v2.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.widget.period_widget_v2.PeriodConfig;
import com.widget.period_widget_v2.R;

public class About extends Fragment{
	
	public static About newInstance(PeriodConfig parentObject){
		return new About();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about, container, false);
	}

	
	public void onViewCreated(View v, Bundle saved){
		super.onViewCreated(v, saved);
		String mystring=new String("leave a rating");
		SpannableString content = new SpannableString(mystring);
		content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
		Button b = (Button) v.findViewById(R.id.rating);
		b.setText(content);
		b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
			}	
		});
	}
}
