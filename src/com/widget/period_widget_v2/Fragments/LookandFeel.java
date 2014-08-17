package com.widget.period_widget_v2.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.widget.period_widget_v2.PeriodConfig;
import com.widget.period_widget_v2.R;
import com.widget.period_widget_v2.period_widget;
import com.widget.period_widget_v2.color_picker.ColorPicker;
import com.widget.period_widget_v2.color_picker.OpacityBar;
import com.widget.period_widget_v2.color_picker.SVBar;

public class LookandFeel extends Fragment{

	private TextView standerdText1;
	private TextView standerdText2;
	private ImageView periodText;
	private FrameLayout background;
	public static LookandFeel newInstance(PeriodConfig parentObject){
		return new LookandFeel();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        return inflater.inflate(R.layout.lookandfeel, container, false);
	}

	
	public void onViewCreated(View v, Bundle saved){
		super.onViewCreated(v, saved);
		iniViewItems();
		iniButtons();
	}
	
	
	@SuppressLint("ShowToast")
	public void iniButtons(){
		((Button)getView().findViewById(R.id.background)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(getActivity());
				d.setTitle("Select a Background");
				d.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				d.setContentView(R.layout.background_chooser);
				((ImageButton)d.findViewById(R.id.defult)).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						period_widget.backgoundId = R.drawable.background;
						background.setBackgroundResource(period_widget.backgoundId);
						if(period_widget.isTinted)
							doTint();
						d.dismiss();
					}
				});
				
				((ImageButton)d.findViewById(R.id.gray)).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						period_widget.backgoundId = R.drawable.gray_background;
						background.setBackgroundResource(period_widget.backgoundId);
						if(period_widget.isTinted)
							doTint();
						d.dismiss();
					}
				});
				d.show();
			}
		});
		
		((Button)getView().findViewById(R.id.background_tint)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				final Dialog d = new Dialog(getActivity());
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.colorpicker_tint);
				final ColorPicker picker = (ColorPicker)d.findViewById(R.id.picker);
				SVBar svBar = (SVBar)d.findViewById(R.id.svbar);
				OpacityBar opacityBar = (OpacityBar)d.findViewById(R.id.opacitybar);

				picker.addSVBar(svBar);
				picker.addOpacityBar(opacityBar);
				picker.setOldCenterColor(period_widget.tint);
				picker.setColor(period_widget.tint);
				
				d.show();
				((Button)d.findViewById(R.id.done)).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						period_widget.tint = picker.getColor();
						doTint();
						d.dismiss();
					}
				});
			}
		});
		
		((Button)getView().findViewById(R.id.periodTextColor)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(getActivity());
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.colorpicker);
				final ColorPicker picker = (ColorPicker)d.findViewById(R.id.picker);
				SVBar svBar = (SVBar)d.findViewById(R.id.svbar);
				OpacityBar opacityBar = (OpacityBar)d.findViewById(R.id.opacitybar);

				picker.addSVBar(svBar);
				picker.addOpacityBar(opacityBar);
				picker.setOldCenterColor(period_widget.periodTextColor);
				picker.setColor(period_widget.periodTextColor);
				
				Button b = (Button)d.findViewById(R.id.same);
				GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
				sd.setColor(period_widget.standerdTextColor);
				sd.invalidateSelf();
				b.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						picker.setColor(period_widget.standerdTextColor);
					}
				});
				
				d.show();
				((Button)d.findViewById(R.id.done)).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						period_widget.periodTextColor = picker.getColor();
						periodText.setImageBitmap(buildBitmap(period_widget.periodTextColor));
						
						Button b = (Button)getView().findViewById(R.id.periodTextColor);
						GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
						sd.setColor(period_widget.periodTextColor);
						sd.invalidateSelf();
						
						d.dismiss();
					}
				});
			}
		});
		
		((Button)getView().findViewById(R.id.standeredColor)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				final Dialog d = new Dialog(getActivity());
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.colorpicker);
				final ColorPicker picker = (ColorPicker)d.findViewById(R.id.picker);
				SVBar svBar = (SVBar)d.findViewById(R.id.svbar);
				OpacityBar opacityBar = (OpacityBar)d.findViewById(R.id.opacitybar);

				picker.addSVBar(svBar);
				picker.addOpacityBar(opacityBar);
				picker.setOldCenterColor(period_widget.standerdTextColor);
				picker.setColor(period_widget.standerdTextColor);
				
				Button b = (Button)d.findViewById(R.id.same);
				GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
				sd.setColor(period_widget.periodTextColor);
				sd.invalidateSelf();
				
				b.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						picker.setColor(period_widget.periodTextColor);
					}
				});
				
				d.show();
				((Button)d.findViewById(R.id.done)).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						period_widget.standerdTextColor = picker.getColor();
						standerdText1.setTextColor(period_widget.standerdTextColor);
						standerdText2.setTextColor(period_widget.standerdTextColor);
						
						Button b = (Button)getView().findViewById(R.id.standeredColor);
						GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
						sd.setColor(period_widget.standerdTextColor);
						sd.invalidateSelf();
						
						d.dismiss();
					}
				});
			}
		});
		
		((Button)getView().findViewById(R.id.defult)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				period_widget.standerdTextColor = Color.LTGRAY;
				period_widget.periodTextColor = Color.GREEN;
				period_widget.backgoundId = R.drawable.background;
				period_widget.tint = -1;
				standerdText1.setTextColor(period_widget.standerdTextColor);
				standerdText2.setTextColor(period_widget.standerdTextColor);
				periodText.setImageBitmap(buildBitmap(period_widget.periodTextColor));
				
				Button b = (Button)getView().findViewById(R.id.background_tint);
				GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
				sd.setColor(period_widget.tint);
				sd.invalidateSelf();
				
				Button b1 = (Button)getView().findViewById(R.id.standeredColor);
				GradientDrawable sd1 = (GradientDrawable)b1.getBackground().mutate();
				sd1.setColor(period_widget.standerdTextColor);
				sd1.invalidateSelf();
				
				Button b2 = (Button)getView().findViewById(R.id.periodTextColor);
				GradientDrawable sd2 = (GradientDrawable)b2.getBackground().mutate();
				sd2.setColor(period_widget.periodTextColor);
				sd2.invalidateSelf();
				
				background.setBackgroundResource(period_widget.backgoundId);
			}
		});
	}
	
	public void doTint(){
		if(period_widget.backgoundId == R.drawable.background){
			background.setBackgroundDrawable(new BitmapDrawable(getResources(), tintImage(R.drawable.gray_tile, period_widget.tint)));
			period_widget.backgoundId = R.drawable.gray_tile;
		}else if(period_widget.backgoundId == R.drawable.gray_background){
			background.setBackgroundDrawable(new BitmapDrawable(getResources(), tintImage(R.drawable.white_background, period_widget.tint)));
			period_widget.backgoundId = R.drawable.white_background;
		}else
			background.setBackgroundDrawable(new BitmapDrawable(getResources(), tintImage(period_widget.backgoundId, period_widget.tint)));
		Button b = (Button)getView().findViewById(R.id.background_tint);
		GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
		sd.setColor(period_widget.tint);
		sd.invalidateSelf();
	}
	
	public void iniViewItems(){
		standerdText1 = (TextView)getView().findViewById(R.id.nextin);
		standerdText2 = (TextView)getView().findViewById(R.id.mincounter);
		periodText = (ImageView)getView().findViewById(R.id.period_text);
		background = (FrameLayout)getView().findViewById(R.id.mainLayout);
		standerdText1.setTextColor(period_widget.standerdTextColor);
		standerdText2.setTextColor(period_widget.standerdTextColor);
		if(!period_widget.isTinted)
			background.setBackgroundResource(period_widget.backgoundId);
		else
			background.setBackgroundDrawable(new BitmapDrawable(getResources(), tintImage(period_widget.backgoundId, period_widget.tint)));
		periodText.setImageBitmap(buildBitmap(period_widget.periodTextColor));
		
		Switch tint = (Switch)getView().findViewById(R.id.tintswitch);
		tint.setChecked(period_widget.isTinted);
		if(period_widget.isTinted)
			doTint();
		else{
			((Button)getView().findViewById(R.id.background_tint)).setClickable(false);
			((Button)getView().findViewById(R.id.background_tint)).setEnabled(false);
		}
		tint.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				((Button)getView().findViewById(R.id.background_tint)).setClickable(arg1);
				((Button)getView().findViewById(R.id.background_tint)).setEnabled(arg1);
				if(arg1 == false){
					if(period_widget.backgoundId == R.drawable.gray_tile){
						period_widget.backgoundId = R.drawable.background;
					}else if(period_widget.backgoundId == R.drawable.white_background){
						period_widget.backgoundId = R.drawable.gray_background;
					}
					background.setBackgroundResource(period_widget.backgoundId);
				}else if(arg1 == true){
					doTint();
				}
				period_widget.isTinted = arg1;
			}
		});
		
		Button b = (Button)getView().findViewById(R.id.background_tint);
		GradientDrawable sd = (GradientDrawable)b.getBackground().mutate();
		sd.setColor(period_widget.tint);
		sd.invalidateSelf();
		
		Button b1 = (Button)getView().findViewById(R.id.standeredColor);
		GradientDrawable sd1 = (GradientDrawable)b1.getBackground().mutate();
		sd1.setColor(period_widget.standerdTextColor);
		sd1.invalidateSelf();
		
		Button b2 = (Button)getView().findViewById(R.id.periodTextColor);
		GradientDrawable sd2 = (GradientDrawable)b2.getBackground().mutate();
		sd2.setColor(period_widget.periodTextColor);
		sd2.invalidateSelf();
		
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
	
	public Bitmap buildBitmap(int size){
		Bitmap myBitmap = Bitmap.createBitmap(300, 60, Bitmap.Config.ARGB_4444);
		Canvas myCanvas = new Canvas(myBitmap);
		Typeface clock = Typeface.createFromAsset(getActivity().getAssets(),"digital.otf");
		TextPaint mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTypeface(clock);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setColor(period_widget.periodTextColor);
		mTextPaint.setTextAlign(Align.CENTER);
		
		Rect bounds = new Rect();
		mTextPaint.setTextSize(100);
		mTextPaint.setTextScaleX(1.0f);
		mTextPaint.getTextBounds("6", 0, 1, bounds);
		int h = bounds.bottom - bounds.top;
		int w = bounds.right - bounds.left;
		float target = (float) 60 * .8f;
		float targetw = (float)300* .8f;
		float size1 = ((target / h) * 100f);
		float size2 = ((targetw / w) * 100f);
		if(size1 < size2)
			mTextPaint.setTextSize(size1);
		else
			mTextPaint.setTextSize(size2);

		myCanvas.drawText("6", 150, 60, mTextPaint);
		
		return myBitmap;
	}
}
