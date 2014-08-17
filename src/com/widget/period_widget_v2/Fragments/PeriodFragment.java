package com.widget.period_widget_v2.Fragments;

import java.util.ArrayList;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;
import com.widget.period_widget_v2.PeriodConfig;
import com.widget.period_widget_v2.R;
import com.widget.period_widget_v2.period_widget;
import com.widget.period_widget_v2.Items.period;
import com.widget.period_widget_v2.ListView.periodListAdaptor;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.TabSpec;

public class PeriodFragment extends Fragment{

	 private DragSortListView mList;
    private DragSortListView.DropListener mDropListener;
	private RemoveListener mRemoveListener;
    private static PeriodConfig parent;
	
    public static PeriodFragment newInstance(PeriodConfig parentObject){
    	parent = parentObject;
    	if(period_widget.periods == null)
    		period_widget.periods = new ArrayList<period>();
		return new PeriodFragment();
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.config, container, false);
    }
    
    @Override
    public void onStart(){
    	super.onStart();
        configure();
        iniButtons();
    }
    
    public void iniButtons(){
    	((Button)getView().findViewById(R.id.gen)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(getActivity());
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.new_item);
				d.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				TabHost tabs = (TabHost)d.findViewById(R.id.tabhost);
				 tabs.setup();
                 tabs.setCurrentTab(0);
                 TabSpec tspec11 = tabs.newTabSpec("Tab1");
                 tspec11.setIndicator("New Period");

                 tspec11.setContent(R.id.newPeriod);
                 tabs.addTab(tspec11);
                 
                 TabSpec tspec2 = tabs.newTabSpec("Tab2");
                 tspec2.setIndicator("Auto-Create");

                 tspec2.setContent(R.id.autoCreate);
                 tabs.addTab(tspec2);
                 
                 ((Button)d.findViewById(R.id.done)).setOnClickListener(new OnClickListener(){



					@Override
					public void onClick(View v){
						d.dismiss();
						TimePicker schoolStart = (TimePicker)d.findViewById(R.id.schoolStart);
						TimePicker periodStart = (TimePicker)d.findViewById(R.id.startTime);
						TimePicker periodEnd = (TimePicker)d.findViewById(R.id.endTime);
						EditText passingTime = (EditText)d.findViewById(R.id.passing);
						EditText periodLength = (EditText)d.findViewById(R.id.periodLength);
						EditText periodName = (EditText)d.findViewById(R.id.name);
						EditText periodNum = (EditText)d.findViewById(R.id.numPeriods);
						if(periodName.getText().toString().length() > 0){
							period_widget.periods.add(new period(periodName.getText().toString(), periodStart.getCurrentHour(), periodStart.getCurrentMinute(), periodEnd.getCurrentHour(), periodEnd.getCurrentMinute()));
						}
						if(periodLength.getText().toString().length() > 0 && periodLength.getText().toString().length() > 0 && periodNum.getText().toString().length() > 0){
							int startTimeH = schoolStart.getCurrentHour();
							int startTimeM = schoolStart.getCurrentMinute();
							int currMinute = startTimeM;
							int currHour = startTimeH; 
							int periodLen = 0;
							int passing = 0;
							periodLen = Integer.parseInt(periodLength.getText().toString());
							int count = 1;
							passing = Integer.parseInt(passingTime.getText().toString());
							if(currMinute + periodLen >= 60){
								currMinute += (periodLen - 60);
								currHour++;
							}else
								currMinute += periodLen;
							
							period_widget.periods.add(new period(Integer.toString(count), startTimeH, startTimeM, currHour, currMinute));
							while(Integer.parseInt(periodNum.getText().toString()) > count){
								count++;
								
								if(currMinute + passing >= 60){
									currMinute+= (passing - 60);
									currHour++;
								}else
									currMinute += passing;
								
								int startMin = currMinute;
								int startH = currHour;
								
								if(currMinute + periodLen >= 60){
									currMinute += (periodLen - 60);
									currHour++;
								}else
									currMinute += periodLen;
								
								period_widget.periods.add(new period(Integer.toString(count), startH, startMin, currHour, currMinute));
							}
						}
						update();		
					}
                 });
				d.show();
			}
        });
        
        Button b =  ((Button)getView().findViewById(R.id.done));
        b.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		parent.finishConfig();
        	}
        });
    }
    public void configure(){
    	getActivity();
		periodListAdaptor adaptor = new periodListAdaptor((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), period_widget.periods);
    	ListView lv = (ListView)getView().findViewById(R.id.periodList);
    	lv.setAdapter(adaptor);
	    mList = (DragSortListView)lv;
    	lv.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				period_widget.periods.remove(arg2);
				Vibrator v = (Vibrator)parent.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(100);
				update();
				return false;
			}
    	});
    	lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final period selected = period_widget.periods.get(arg2);
				final Dialog d = new Dialog(PeriodFragment.this.getActivity());
				d.setTitle("Edit Period" + " \"" +  selected.name + "\"");
				d.setContentView(R.layout.edit_dialog);
				final TimePicker start = (TimePicker) d.findViewById(R.id.periodStart);
				final TimePicker end = (TimePicker) d.findViewById(R.id.periodEnd);
				final EditText period = (EditText)d.findViewById(R.id.period);
				Button done = (Button)d.findViewById(R.id.done);
				start.setCurrentHour(selected.startHour);
				start.setCurrentMinute(selected.startMin);
				end.setCurrentHour(selected.endHour);
				end.setCurrentMinute(selected.endMin);
				period.setText(selected.name);
				done.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						selected.startHour = start.getCurrentHour();
						selected.startMin = start.getCurrentMinute();
						selected.endHour = end.getCurrentHour();
						selected.endMin = end.getCurrentMinute();
						selected.name = period.getText().toString();
						d.dismiss();
						((BaseAdapter)mList.getAdapter()).notifyDataSetChanged();
					}
				});
				d.show();
			}
    	});
    	mRemoveListener = new DragSortListView.RemoveListener() {
			@Override
			public void remove(int which) {
				period_widget.periods.remove(which);
				((BaseAdapter)mList.getAdapter()).notifyDataSetChanged();
			}
		};
    	mDropListener = new DragSortListView.DropListener(){
	        public void drop(int from, int to) {
	            //Assuming that item is moved up the list
	            int direction = -1;
	            int loop_start = from;
	            int loop_end = to;

	            //For instance where the item is dragged down the list
	            if(from < to) {
	            	direction = 1;
	            }

	            period target = period_widget.periods.get(from);

	            for(int i = loop_start; i != loop_end; i = i+direction){
	            	period_widget.periods.set(i, period_widget.periods.get(i+direction));
	            }

	            period_widget.periods.set(to, target);

	            ((BaseAdapter)mList.getAdapter()).notifyDataSetChanged();
	        }
	    };
	    
	    mList = (DragSortListView)lv;
    	mList.setDropListener(mDropListener);
    	mList.setRemoveListener(mRemoveListener);
    	registerForContextMenu(mList);
    
    }
    
	public void update(){
        ((BaseAdapter)mList.getAdapter()).notifyDataSetChanged();
	}
}
