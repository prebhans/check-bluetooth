package com.gmail.prebhans.checkbluetooth;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//import android.content.res.Resources;

public class ScanViewList extends ArrayAdapter<ScanDevice> {
    private static final String TAG = "ScanViewList";
    private final Context context;
    private final List<ScanDevice> allScannedDevices;

    public ScanViewList(Context context, List<ScanDevice> allScannedDevices) {
        super(context, R.layout.list_one_device_a, allScannedDevices);
        Log.i(TAG, "ScanViewList start");
        this.context = context;
        this.allScannedDevices = allScannedDevices;
    }

    static class ViewContainer {

        private TextView numView; // R.id.no_digits
        private TextView nameView; // R.id.name
        private TextView macaddrView; // R.id.mac_addr
        private TextView powerView; // R.id.rssi
        private TextView companyView; // R.id.company_name
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.i(TAG, "getView start");
        ViewContainer viewContainer;
        View rowView = view;
        ScanDevice localDevice;
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_one_device_a,
                    null, false);
            viewContainer = new ViewContainer();
            viewContainer.numView = (TextView) rowView
                    .findViewById(R.id.no_digits);
            viewContainer.nameView = (TextView) rowView.findViewById(R.id.name);
            viewContainer.macaddrView = (TextView) rowView
                    .findViewById(R.id.mac_addr);
            viewContainer.powerView = (TextView) rowView
                    .findViewById(R.id.rssi);
            viewContainer.companyView = (TextView) rowView
                    .findViewById(R.id.company_name);
            rowView.setTag(viewContainer);
        } else {
            viewContainer = (ViewContainer) rowView.getTag();
        }
        localDevice = allScannedDevices.get(position);
        viewContainer.numView.setText(localDevice.getmNum());
        viewContainer.nameView.setText(localDevice.getmName());
        viewContainer.macaddrView.setText(localDevice.getmMacaddr());
        viewContainer.powerView.setText(localDevice.getmPower());
        viewContainer.companyView.setText(localDevice.getmCompany());
        return rowView;
    }
} // end

