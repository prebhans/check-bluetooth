package com.gmail.prebhans.checkbluetooth;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//import android.content.res.Resources;

public class PairViewList extends ArrayAdapter<String[]> {
	// private static final String TAG = "ListCardScroll";
	private final Context context;
	private final ArrayList<String[]> allPairDevices;

	public PairViewList(Context context, ArrayList<String[]> allPairDevices) {
		super(context, R.layout.list_one_device_c, allPairDevices);
		this.context = context;
		this.allPairDevices = allPairDevices;
	}

	static class ViewContainer {
		public TextView no_digitsPairView; // R.id.no_digits
		public TextView namePairView; // R.id.name
		public TextView macaddrPairView; // R.id.mac_addr
		public TextView powerPairView; // R.id.rssi
        public TextView companyPairView; // R.id.rssi
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewContainer viewContainer;
		View rowView = view;
		//int infoPosition = position + 1;
		//String cardNoInfo = " " + infoPosition;
		String[] localDevice;
		if (rowView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_one_device_c,
					null, false);
			viewContainer = new ViewContainer();
			viewContainer.no_digitsPairView = (TextView) rowView
					.findViewById(R.id.pair_no_digits);
			viewContainer.namePairView = (TextView) rowView
					.findViewById(R.id.pair_name);
			viewContainer.macaddrPairView = (TextView) rowView
					.findViewById(R.id.pair_mac_addr);
			viewContainer.powerPairView = (TextView) rowView
					.findViewById(R.id.pair_rssi);
            viewContainer.companyPairView = (TextView) rowView
                    .findViewById(R.id.pair_company_name);
			rowView.setTag(viewContainer);
		} else {
			viewContainer = (ViewContainer) rowView.getTag();
		}
		localDevice = allPairDevices.get(position);

		// delText.
		viewContainer.no_digitsPairView.setText(localDevice[0]);
		viewContainer.namePairView.setText(localDevice[1]);
		viewContainer.macaddrPairView.setText(localDevice[2]);
		viewContainer.powerPairView.setText(localDevice[3]);
        viewContainer.companyPairView.setText(localDevice[4]);
		return rowView;
	}

} // end

