package com.widget.period_widget_v2.Items;

import java.io.Serializable;

import android.view.View;

public class period implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name = "";
	public int startMin = 0;
	public int startHour = 0;
	public int endMin = 0;
	public int endHour = 0;
	public View view;
	
	public period(String name, int startH, int startM, int endH, int endM){
		this.name = name;
		startMin = startM;
		startHour = startH;
		endMin = endM;
		endHour = endH;
	}
	
	
	public String getFormattedStartMin(){
		if(Integer.toString(startMin).length() == 1)
			return "0" + startMin;
		else
			return Integer.toString(startMin);
	}
	
	public int getLength(){
		return ((endHour*60) + endMin)-((startHour*60) + startMin);
	}
	
	public String getFormattedEndMin(){
		if(Integer.toString(endMin).length() == 1)
			return "0" + endMin;
		else
			return Integer.toString(endMin);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setStartMin(int min){
		startMin = min;
	}
	
	public void setEndMin(int min){
		endMin = min;
	}
	
	public void setStartHour(int hour){
		startHour = hour;
	}
	
	public void setEndHour(int hour){
		endHour = hour;
	}
	
	public int getActualEnd(){
		if(endHour > 12){
			return endHour-12;
		}else
			return endHour;
	}
	
	public int getActualStart(){
		if(startHour > 12){
			return startHour-12;
		}else
			return startHour;
	}
	
}
