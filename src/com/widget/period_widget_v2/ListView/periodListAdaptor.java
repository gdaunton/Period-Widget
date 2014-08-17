package com.widget.period_widget_v2.ListView;

import java.util.ArrayList;

import com.widget.period_widget_v2.R;
import com.widget.period_widget_v2.Items.period;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class periodListAdaptor extends BaseAdapter{
	private ArrayList<period> objects;
	private LayoutInflater inflater;
	private ArrayList<View> views;
	public periodListAdaptor(LayoutInflater inflater, ArrayList<period> objects){
		this.objects = objects;
		this.inflater = inflater;;
		views = new ArrayList<View>();
	}
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position){
		return views.get(position);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		
        if(convertView == null)
            vi = inflater.inflate(R.layout.period_object, null);
        
        TextView title = (TextView)vi.findViewById(R.id.name); // title
        TextView detail = (TextView)vi.findViewById(R.id.time);
        String name = objects.get(position).name;
        if(name.length()> 25){
        	name = name.substring(0, 25) + "...";
        }
        period p = objects.get(position);
        title.setText(name);
        detail.setText(p.getActualStart() + ":" + p.getFormattedStartMin() + " to " + p.getActualEnd() + ":" + p.getFormattedEndMin());
		return vi;
	}
}
