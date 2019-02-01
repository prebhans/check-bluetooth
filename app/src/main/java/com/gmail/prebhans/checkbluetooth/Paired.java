package com.gmail.prebhans.checkbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Set;

public class Paired extends Fragment {
    private static final String TAG = "Paired";
    private FirebaseAnalytics mFirebaseAnalytics;
    // TODO Run for emulator true for real devices
    private boolean runEmulator = false;
    public static final String ARG_SECTION_NUMBER = "Paired";
    private CbtHelpMethods btHelp = new CbtHelpMethods();
    private Context context;
    private View pairRootView;
    private Bundle args;
    //
    private int numPairDev; // number of paired devices
    private ArrayList<String[]> allPairDevices = new ArrayList<String[]>(); // string
    private boolean allDevicesReadIn = false;
    private PairViewList adapter;
    private ListView pairDevList;
    private BluetoothAdapter mBtAdapter;
    // DB
    private DBHelper mDeviceSql;
    private Resources res;
    private boolean activityCreated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate start");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Crashlytics.log("Paired created");
    } // onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView start");
        pairRootView = inflater.inflate(R.layout.paired, container, false);
        args = getArguments();
        context = container.getContext();
        res = container.getResources();
        mDeviceSql = new DBHelper(context);
        return pairRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume start");
        // long now = System.currentTimeMillis();
        pairToView();
        // Log.w(TAG, "onResume" + (System.currentTimeMillis() - now) + "ms");
        Log.i(TAG, "onResume end");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart start");
        int versionCode = BuildConfig.VERSION_CODE;
        String versionCodeStr = Integer.toString(versionCode);
        String versionName = BuildConfig.VERSION_NAME;
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, versionCodeStr);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, versionName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Paired");
        bundle.putString(FirebaseAnalytics.Event.APP_OPEN, "onStart");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        // getBtSettings();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause start");
        super.onPause();
    } // onPause

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop start");
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy start");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy start");
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint start");
        if (isVisibleToUser) {
            Log.i(TAG, "Fragment is visible.");
            // pairToView();
        } else
            Log.i(TAG, "Fragment is not visible.");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated start");
        activityCreated = true;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored start");
    }

    // onOverride ends

    private void getPairDev() {
        Log.i(TAG, "sqlToView start");
        String numDev;
        numPairDev = 0;
        String name = "No Name";
        String macaddr = "00:00:00:00:00:00";
        String power = "-99"; // temp to later coding
        String company = "No Company Name";
        // Get the local Bluetooth adapter
        if (!runEmulator)
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        if (!runEmulator) {

            if(mBtAdapter != null) {
                Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

                // If there are paired devices, add each one to the ArrayAdapter
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        // numDev = getDevId(macaddr);
                        // numDev = getNumDev();
                        numPairDev++;
                        numDev = String.valueOf(numPairDev);
                        name = device.getName();
                        macaddr = device.getAddress();
                        power = getPowerFromDb(macaddr);
                        company = getCompanyFromDb(macaddr);
                        // power = "No";
                        String[] oneDbDeviceData = {numDev, name, macaddr, power, company};
                        allPairDevices.add(oneDbDeviceData);
                    }
                } else {
                    btHelp.callToast(context, "No Paired Devices", 0);
                }
            }
        }
    }

    private void pairToView() {
        Log.i(TAG, "pairToView start");
        allPairDevices.clear(); // clear before call
        allDevicesReadIn = false;
        getPairDev();
        allDevicesReadIn = true;
        adapter = new PairViewList(context, allPairDevices);
        pairDevList = (ListView) pairRootView.findViewById(R.id.pair_list);
        pairDevList.setAdapter(adapter);
        Log.i(TAG, "pairToView End");
    }

    private String getPowerFromDb(String macaddr) {
        String dbPower = "";
        boolean deviceInDb;
        deviceInDb = mDeviceSql.isDeviceInDb(macaddr);
        if (deviceInDb)
            dbPower = mDeviceSql.getDevicePower(macaddr);
        else
            dbPower = "0";
        return dbPower;
    }

    private String getCompanyFromDb(String macaddr) {
        String dbCompany = "";
        boolean deviceInDb;
        deviceInDb = mDeviceSql.isDeviceInDb(macaddr);
        if (deviceInDb)
            dbCompany = mDeviceSql.getCompanyName(macaddr);
        else
            dbCompany = "0";
        return dbCompany;
    }

    // private String getNumDev() {
    // String numDev = "";
    // numDev = String.valueOf(numPairDev + 1);
    // return numDev;
    // }

    /*
     * private String getDevId(String macaddr) { String devId = ""; boolean
     * deviceInDb; deviceInDb = mDeviceSql.isDeviceInDb(macaddr); if
     * (deviceInDb) devId = String.valueOf(mDeviceSql.getDeviceId(macaddr));
     * return devId; }
     */

}
