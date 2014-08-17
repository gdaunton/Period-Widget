package com.widget.period_widget_v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.widget.RemoteViews;

import com.widget.period_widget_v2.Items.period;

public class period_widget extends AppWidgetProvider{
	public static String WIDGET_UPDATE = "com.widget.period_widget_v2.WIDGET_UPDATE";
	public static String CONFIGURE = "com.widget.period_widget_v2.PeriodConfig";
	RemoteViews remoteViews = null;
	ComponentName widget = null;
	private SharedPreferences sharedPreferences;
	public static boolean isTinted = false;
	public static int tint;
	public static File cacheDir;
	public static ArrayList<period> periods;
	public static int periodTextColor = Color.GREEN;
	public static int standerdTextColor = Color.LTGRAY;
	public static int backgoundId = R.drawable.background;
	public static Context context;
	public static boolean isPro = true;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		period_widget.context = context;
		
		if (WIDGET_UPDATE.equals(intent.getAction())) {
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
		    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		    int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
		    for (int appWidgetID: ids) {
				Update(context, appWidgetManager, appWidgetID);
		    }
		}
	}
		
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override 
	public void onEnabled(Context context) {
		super.onEnabled(context);		
		
		this.context = context;
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		isPro = true;
		cacheDir = new File(context.getCacheDir().getAbsoluteFile() + File.separator + "periodArrayCache");
		try {
			FileInputStream f = new FileInputStream(cacheDir);
			ObjectInputStream is = new ObjectInputStream(f);
			periods = (ArrayList<period>)is.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sharedPreferences = context.getSharedPreferences("period_widget", context.MODE_PRIVATE);
        if(sharedPreferences != null){
        	standerdTextColor = sharedPreferences.getInt("standerd_color", period_widget.standerdTextColor);
            periodTextColor = sharedPreferences.getInt("period_color", period_widget.periodTextColor);
            backgoundId = sharedPreferences.getInt("background", R.drawable.background);
            tint = sharedPreferences.getInt("tint", -1);
            isTinted = sharedPreferences.getBoolean("isTinted", false);
        }
        		
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000, createClockTickIntent(context));

	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		period_widget.context = context;
    	AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));	
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		final int N = appWidgetIds.length;
		
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			Update(context, appWidgetManager, appWidgetId);
		}
    }
	
	private PendingIntent createClockTickIntent(Context context) {
		    Intent intent = new Intent(WIDGET_UPDATE);
		    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    return pendingIntent;
	}
	
	public void Update(Context context, AppWidgetManager manager, int appWidgetId){
		period_widget.context = context;
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		widget = new ComponentName(context, period_widget.class);
		String period = "";
		Date date = new Date();
		int hour = date.getHours();
		int min = date.getMinutes();
		int minUntilNext = 0;
		period currperiod = null;
    	if(periods != null && periods.size() != 0){
    		for(period p : periods){
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
    			if(hour >= periods.get(periods.size()-1).endHour && hour <= 24){
    				period = "After School";
    				totalMin = (((periods.get(0).startHour + 24) * 60) + periods.get(0).startMin) - (( hour * 60) + min);
    				calcAfterSchoolTime(totalMin);
    			}else if(hour <= periods.get(periods.size()-1).endHour){
    				period = "Before School";
    				totalMin = ((periods.get(0).startHour * 60) + periods.get(0).startMin) - (( hour * 60) + min);
    				calcAfterSchoolTime(totalMin);
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
    			remoteViews.setTextViewText(R.id.nextin, "Period " + periods.get(periods.indexOf(currperiod)+1).name + " in:");
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
        remoteViews.setTextColor(R.id.nextin, standerdTextColor);
        remoteViews.setTextColor(R.id.minuitesUntil, standerdTextColor);
        
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        ComponentName cn = new ComponentName("com.widget.period_widget_v2","com.widget.period_widget_v2.PeriodConfig");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);
		if(!isTinted){
			remoteViews.setImageViewResource(R.id.background, period_widget.backgoundId);
		}else
			remoteViews.setImageViewBitmap(R.id.background, tintImage(period_widget.backgoundId, period_widget.tint));
        
		manager.updateAppWidget(appWidgetId, remoteViews);
	}
	
	public void calcAfterSchoolTime(int totalMin){
		int remHour = totalMin/60;
		int remMin = totalMin - (remHour * 60);
		remoteViews.setTextViewText(R.id.minuitesUntil, remHour + " hours " + remMin + " min");
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
		int h = bounds.bottom - bounds.top;
		int w = bounds.right - bounds.left;
		float target = (float) 60 * .8f;
		float targetw = (float)300 * .8f;
		float size1 = ((target / h) * 100f);
		float size2 = ((targetw / w) * 100f);
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
		Bitmap b = BitmapFactory.decodeResource(period_widget.context.getResources(), resId);
		Bitmap post = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
		Canvas c = new Canvas(post);
        Matrix matrix = new Matrix();
        c.drawBitmap(b, matrix, p);
        return post;
	}
}
