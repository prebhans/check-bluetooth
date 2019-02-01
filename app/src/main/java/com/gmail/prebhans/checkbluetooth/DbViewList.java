package com.gmail.prebhans.checkbluetooth;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

//import android.content.res.Resources;

public class DbViewList extends ArrayAdapter<String[]> {
	private static final String TAG = "DbViewList";
	private final Context context;
	private final ArrayList<String[]> allDbDevices;
	private boolean[] checkedItems;
	ArrayList<Boolean> positionArray;

	public DbViewList(Context context, ArrayList<String[]> allDbDevices) {
		super(context, R.layout.list_one_device_b, allDbDevices);
		Log.i(TAG, "DbViewList start");
		this.context = context;
		this.allDbDevices = allDbDevices;
		//
		checkedItems = new boolean[allDbDevices.size()];
	}

	static class ViewContainer {
		// Log.i(TAG, "ViewContainer start");
		public TextView numDbView; // R.id.no_digits
		public TextView nameDbView; // R.id.name
		public CheckBox checkBox; // R.id.checkBox1
		public TextView macaddrDbView; // R.id.mac_addr
		public TextView powerDbView; // R.id.rssi
        public TextView companyDbView; // R.id.company
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		Log.i(TAG, "getView start");
		ViewContainer viewContainer;
		View rowView = view;
		//int infoPosition = position + 1;
		//String cardNoInfo = " " + infoPosition;
		String[] localDevice;
		if (rowView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_one_device_b,
					(ViewGroup) null, false);
			viewContainer = new ViewContainer();
			viewContainer.numDbView = (TextView) rowView
					.findViewById(R.id.db_no_digits);
			viewContainer.nameDbView = (TextView) rowView
					.findViewById(R.id.db_name);
			viewContainer.checkBox = (CheckBox) rowView
					.findViewById(R.id.checkBox1);
			viewContainer.macaddrDbView = (TextView) rowView
					.findViewById(R.id.db_mac_addr);
			viewContainer.powerDbView = (TextView) rowView
					.findViewById(R.id.db_rssi);
            viewContainer.companyDbView = (TextView) rowView
                    .findViewById(R.id.db_company_name);
			rowView.setTag(viewContainer);

		} else {
			viewContainer = (ViewContainer) rowView.getTag();
			viewContainer.checkBox.setOnCheckedChangeListener(null);
			viewContainer.checkBox.setChecked(checkedItems[position]);

		}
		localDevice = allDbDevices.get(position);
		viewContainer.numDbView.setText(localDevice[0]);
		viewContainer.nameDbView.setText(localDevice[1]);
		viewContainer.checkBox.setId(position);
		viewContainer.macaddrDbView.setText(localDevice[2]);
		viewContainer.powerDbView.setText(localDevice[3]);
        viewContainer.companyDbView.setText(localDevice[4]);
		viewContainer.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						//CompoundButton localButtonView;
						//localButtonView = buttonView;
						int locPosition;
						// int position = localButtonView.getTag();
						// int position = (Integer) buttonView
						// .getTag(R.id.checkBox1);
						// MenuItemObject menuItem = allDbDevices.get(position);
						locPosition = position;
						if (isChecked) {
							checkedItems[position] = true;
							Log.i(TAG, "onCheckedChanged TRUE" + " "
									+ checkedItems[position] + " "
									+ locPosition);
						} else {
							checkedItems[position] = false;
							Log.i(TAG, "onCheckedChanged FALSE" + " "
									+ checkedItems[position] + " "
									+ locPosition);
						}
						/*
						 * for (int i = 0; i < allDbDevices.size(); i++) {
						 * Log.i(TAG, "onCheckedChanged" + " position " + " " +
						 * i + " " + checkedItems[i]); }
						 */
					}
				});

		return rowView;
	}

	public boolean[] getCheckedItems() {
		Log.i(TAG, "getCheckedItems() start");
		return checkedItems;
	}

} // end

