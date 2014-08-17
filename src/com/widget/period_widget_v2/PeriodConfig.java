package com.widget.period_widget_v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RemoteViews;

import com.widget.period_widget_v2.Fragments.About;
import com.widget.period_widget_v2.Fragments.LookandFeel;
import com.widget.period_widget_v2.Fragments.PeriodFragment;
import com.widget.period_widget_v2.Items.period;

public class PeriodConfig extends FragmentActivity {

	private static int mAppWidgetId;
	private Context context;

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	public SharedPreferences sharedPreferences;
	public static File cache_dir;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		this.context = getApplicationContext();
		sharedPreferences = this.getSharedPreferences("period_widget", MODE_PRIVATE);
		period_widget.standerdTextColor = sharedPreferences.getInt("standerd_color", period_widget.standerdTextColor);
		period_widget.periodTextColor = sharedPreferences.getInt("period_color", period_widget.periodTextColor);
		period_widget.backgoundId = sharedPreferences.getInt("background", R.drawable.background);
		period_widget.tint = sharedPreferences.getInt("tint", -1);
		period_widget.isTinted = sharedPreferences.getBoolean("isTinted", period_widget.isTinted);
		if(period_widget.context != null){
			period_widget.cacheDir = new File(period_widget.context.getCacheDir().getAbsoluteFile() + File.separator + "periodArrayCache");

			try {
				FileInputStream f = new FileInputStream(period_widget.cacheDir);
				ObjectInputStream is = new ObjectInputStream(f);
				period_widget.periods = (ArrayList<period>)is.readObject();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (extras != null) 
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

		mViewPager = (ViewPager)findViewById(R.id.pager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(PeriodConfig.this.getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}



	public Bitmap buildBitmapUpdate(String time){
		Bitmap myBitmap = Bitmap.createBitmap(300, 60, Bitmap.Config.ARGB_4444);
		Canvas myCanvas = new Canvas(myBitmap);
		Typeface clock = Typeface.createFromAsset(context.getAssets(),"digital.otf");
		TextPaint mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTypeface(clock);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setColor(period_widget.periodTextColor);
		mTextPaint.setTextAlign(Align.CENTER);

		Rect bounds = new Rect();
		mTextPaint.setTextSize(100);
		mTextPaint.setTextScaleX(1.0f);
		mTextPaint.getTextBounds(time, 0, time.length(), bounds);
		int hight = bounds.bottom - bounds.top;
		int width = bounds.right - bounds.left;
		float target = (float) 60 * .8f;
		float targetw = (float)300* .8f;
		float size1 = ((target / hight) * 100f);
		float size2 = ((targetw / width) * 100f);
		if(size1 < size2)
			mTextPaint.setTextSize(size1);
		else
			mTextPaint.setTextSize(size2);

		myCanvas.drawText(time, 150, 60, mTextPaint);

		return myBitmap;
	}

	public Bitmap tintImage(int resId, int color){
		Paint p = new Paint();
		ColorFilter filter = new PorterDuffColorFilter(color, Mode.MULTIPLY);
		p.setColorFilter(filter);
		Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
		Bitmap post = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
		Canvas c = new Canvas(post);
		Matrix matrix = new Matrix();
		c.drawBitmap(b, matrix, p);
		return post;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch(i){
			case 0: return PeriodFragment.newInstance(PeriodConfig.this);
			case 1: return LookandFeel.newInstance(PeriodConfig.this);
			case 2: return About.newInstance(PeriodConfig.this);
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return getString(R.string.title_section1).toUpperCase();
			case 1: return getString(R.string.title_section2).toUpperCase();
			case 2: return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	public void calcAfterSchoolTime(int totalMin, RemoteViews rm, String name){
		int remHour = totalMin/60;
		int remMin = totalMin - (remHour * 60);
		rm.setTextViewText(R.id.minuitesUntil, remHour + " hours " + remMin + " min");
	}

	public void finishConfig(){
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

		Update(context, appWidgetManager);

		if(period_widget.periods != null && period_widget.periods.size() != 0){
			sharedPreferences.edit().putInt("period_color", period_widget.periodTextColor).commit();
			sharedPreferences.edit().putInt("standerd_color", period_widget.standerdTextColor).commit();
			sharedPreferences.edit().putInt("background", period_widget.backgoundId).commit();
			sharedPreferences.edit().putInt("tint", period_widget.tint).commit();
			sharedPreferences.edit().putBoolean("isTinted", period_widget.isTinted).commit();

			FileOutputStream fos  = null;
			ObjectOutputStream oos  = null;
			boolean keep = true;

			try {
				fos = new FileOutputStream(period_widget.cacheDir);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(period_widget.periods);
			}
			catch (Exception e) {
				keep = false;
				e.printStackTrace();
			}
			finally {
				try {
					if (oos != null)   oos.close();
					if (fos != null)   fos.close();
					if (keep == false) period_widget.cacheDir.delete();
				}
				catch (Exception e) { /* do nothing */ }
			}

			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			ComponentName cname = new ComponentName("com.widget.period_widget_v2","com.widget.period_widget_v2.PeriodConfig");
			intent.setComponent(cname);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);

			if(!period_widget.isTinted){
				remoteViews.setImageViewResource(R.id.background, period_widget.backgoundId);
			}else
				remoteViews.setImageViewBitmap(R.id.background, tintImage(period_widget.backgoundId, period_widget.tint));

			appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}else{
			finish();
		}
	}

	public void Update(Context context, AppWidgetManager manager){
		period_widget.context = context;
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		String period = "";
		Date date = new Date();
		int hour = date.getHours();
		int min = date.getMinutes();
		int minUntilNext = 0;
		period currperiod = null;
		if(period_widget.periods != null && period_widget.periods.size() != 0){
			for(period p : period_widget.periods){
				if((p.startHour == hour || p.endHour == hour)){
					period = p.name;
					minUntilNext = ((p.endHour * 60) + p.endMin) - ((hour * 60) + min);
					if(minUntilNext >= 0 && minUntilNext <= p.getLength()){
						currperiod = p;
					}
				}
			}
			if(currperiod == null){
				int totalMin = 0;
				if(hour >= period_widget.periods.get(period_widget.periods.size()-1).endHour && hour <= 24){
					period = "After School";
					totalMin = (((period_widget.periods.get(0).startHour + 24) * 60) + period_widget.periods.get(0).startMin) - (( hour * 60) + min);
					remoteViews.setTextViewText(R.id.minuitesUntil, calcAfterSchoolTime(totalMin));
				}else if(hour <= period_widget.periods.get(period_widget.periods.size()-1).endHour){
					period = "Before School";
					totalMin = ((period_widget.periods.get(0).startHour * 60) + period_widget.periods.get(0).startMin) - (( hour * 60) + min);
					remoteViews.setTextViewText(R.id.minuitesUntil, calcAfterSchoolTime(totalMin));
				}if((hour > period_widget.periods.get(0).startHour || (hour == period_widget.periods.get(0).startHour && min > period_widget.periods.get(0).startMin)) && (hour <= period_widget.periods.get(period_widget.periods.size()-1).endHour || (hour == period_widget.periods.get(period_widget.periods.size()-1).endHour && min < period_widget.periods.get(period_widget.periods.size()-1).endMin))){
					period = "Passing";
					remoteViews.setTextViewText(R.id.minuitesUntil, "");
				}
			}else{
				period = currperiod.name;
				minUntilNext = ((currperiod.endHour * 60) + currperiod.endMin) - ((hour * 60) + min);
				remoteViews.setTextViewText(R.id.minuitesUntil, minUntilNext + " min");
			}
		}

		if(period.equals("Passing")){
    		remoteViews.setTextViewText(R.id.nextin, "Next Period in:");
    	}else{
    		try{
    			remoteViews.setTextViewText(R.id.nextin, "Period " + period_widget.periods.get(period_widget.periods.indexOf(currperiod)+1).name + " in:");
    		}catch(Exception E){
    			remoteViews.setTextViewText(R.id.nextin, "Next Period in:");
    		}
    	}
    	 
		Bitmap b = null;
		if(period.equals("")){
			b = buildBitmapUpdate("Error");
			remoteViews.setTextViewText(R.id.minuitesUntil, "Tap To Configure");
			remoteViews.setTextViewText(R.id.nextin, "Next Period in:");
		}else
			b = buildBitmapUpdate(period);

		remoteViews.setImageViewBitmap(R.id.period, b);
		remoteViews.setTextColor(R.id.nextin, period_widget.standerdTextColor);
		remoteViews.setTextColor(R.id.minuitesUntil, period_widget.standerdTextColor);

		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		ComponentName cn = new ComponentName("com.widget.period_widget_v2","com.widget.period_widget_v2.PeriodConfig");
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);
		if(!period_widget.isTinted){
			remoteViews.setImageViewResource(R.id.background, period_widget.backgoundId);
		}else
			remoteViews.setImageViewBitmap(R.id.background, tintImage(period_widget.backgoundId, period_widget.tint));

		manager.updateAppWidget(mAppWidgetId, remoteViews);
	}

	public String calcAfterSchoolTime(int totalMin){
		int remHour = totalMin/60;
		int remMin = totalMin - (remHour * 60);
		return remHour + " hours " + remMin + " min";
	}
}
