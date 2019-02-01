package com.gmail.prebhans.checkbluetooth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class Database extends Fragment {
    private static final String TAG = "Database";
    private FirebaseAnalytics mFirebaseAnalytics;
    public static final String ARG_SECTION_NUMBER = "Database";
    private Context context;
    private View dbRootView;
    private Bundle args;
    private Button downloadButton;
    private Button deleteButton;
    private CbtHelpMethods btHelp = new CbtHelpMethods();
    private DBHelper mDeviceSql;
    // private ArrayAdapter<String> mDbDevicesArrayAdapter;
    //
    private int noDbDev = 0;
    private ArrayList<String[]> allDbDevices = new ArrayList<String[]>();
    private boolean allDevicesReadIn = false;
    private DbViewList adapter;
    private ListView dbDevList;
    private Resources res;
    private boolean activityCreated = false;
    private boolean[] dbCheckedItems;
    // --Commented out by Inspection (02-03-15 11:29):long[] dbCheckedItemsIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate start");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Crashlytics.log("Database created");
    } // onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView start");
        dbRootView = inflater.inflate(R.layout.database, container, false);
        args = getArguments();
        context = container.getContext();
        setupDbButtonLister();
        res = container.getResources();
        mDeviceSql = new DBHelper(context);
        return dbRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume start");
        // long now = System.currentTimeMillis();
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
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Database");
        bundle.putString(FirebaseAnalytics.Event.APP_OPEN, "onStart");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

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
            sqlToView();
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

    // two button db-button and delete-button
    private void setupDbButtonLister() {
        Log.i(TAG, "setupButtonLister start");
        //
        downloadButton = (Button) dbRootView
                .findViewById(R.id.delete_some_devices);
        downloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick startButton");
                // do something
                // alert to delete some devices
                // getDeleteSomeDevices();
                deleteSomeDevicesAlert(view);
            }
        });
        //
        deleteButton = (Button) dbRootView
                .findViewById(R.id.delete_all_devices);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick stopButton");
                // do something
                // alert to delete all devices
                deleteAllDevicesAlert(view);
            }
        });
    }

    private void deleteSomeDevicesAlert(View view) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                view.getContext());

        // Setting Dialog Title
        // "Delete some ..."
        alertDialog2.setTitle(res.getString(R.string.delete_some));

        // Setting Dialog Message
        // "Are you sure you want delete this devices?"
        alertDialog2.setMessage(res.getString(R.string.delete_some_text));

        // Setting Icon to Dialog
        alertDialog2.setIcon(android.R.drawable.ic_dialog_alert);

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton(res.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        getDeleteSomeDevices();
                        // Toast.makeText(context.getApplicationContext(),
                        // "You clicked on YES", Toast.LENGTH_SHORT)
                        // .show();
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton(res.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        // Toast.makeText(context.getApplicationContext(),
                        // "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();

    }

    private void deleteAllDevicesAlert(View view) {

        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
                view.getContext());

        // Setting Dialog Title
        alertDialog1.setTitle(res.getString(R.string.delete_all));

        // Setting Dialog Message
        alertDialog1.setMessage(res.getString(R.string.delete_all_text));

        // Setting Icon to Dialog
        alertDialog1.setIcon(android.R.drawable.ic_dialog_alert);

        // Setting Positive "Yes" Btn
        alertDialog1.setPositiveButton(res.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        getDeleteAllDevices();
                        // Toast.makeText(context.getApplicationContext(),
                        // "You clicked on YES", Toast.LENGTH_SHORT)
                        // .show();
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog1.setNegativeButton(res.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        // Toast.makeText(context.getApplicationContext(),
                        // "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog1.show();

    }

    private void getDeleteSomeDevices() {
        // TODO get info from DbViewList
        Log.i(TAG, "getDeleteSomeDevices start");
        dbCheckedItems = null;
        boolean devToDel = false;

        //
        if (!adapter.isEmpty()) {
            dbCheckedItems = adapter.getCheckedItems();

            for (int i = 0; dbCheckedItems.length > i; i++) {
                if (dbCheckedItems[i])
                    devToDel = true;

            }
            if (devToDel) {
                deleteSomeDevices(dbCheckedItems);
                // "OK devices to delete"
                btHelp.callToast(context, res.getString(R.string.ok_dev_to_del), 0);
            } else {
                // "No devices selected"
                btHelp.callToast(context, res.getString(R.string.no_dev_select), 0);
            }
        } else {
            // "No devices in database"
            btHelp.callToast(context, res.getString(R.string.no_dev_in_base), 0);
        }
        Log.i(TAG, "getDeleteSomeDevices start");

    }

    private void deleteSomeDevices(boolean[] dbCheckedItems) {
        Log.i(TAG, "deleteSomeDevices start");
        boolean[] locDbCheckedItems;
        int lengthDbCheckedItems;
        locDbCheckedItems = dbCheckedItems;
        lengthDbCheckedItems = locDbCheckedItems.length;
        String num = "";
        String name = "";
        String macaddr = "";
        String power = "";
        String company = "";
        String[] oneDbDeviceData = {num, name, macaddr, power, company};
        String macToDelete = "";
        //
        for (int i = 0; lengthDbCheckedItems > i; i++) {
            //
            if (locDbCheckedItems[i]) {
                // delete from db
                oneDbDeviceData = allDbDevices.get(i);
                macToDelete = oneDbDeviceData[2];
                mDeviceSql.deleteDeviceByMac(macToDelete);
                // sqlToView();
                Log.i(TAG, "deleteSomeDevices delete" + " " + i);
            }
        }
        sqlToView(); // Refresh view
    }

    private void getDeleteAllDevices() {
        Log.i(TAG, "getDeleteButtonAction start");
        mDeviceSql.deleteAllDevices();
        mDeviceSql.deleteDatabase();
        adapter.clear();
        // All devices in database deleted
        btHelp.callToast(context, res.getString(R.string.all_dev_del), 0);
    }

    private void sqlToView() {
        Log.i(TAG, "sqlToView start");
        Cursor mDbCursor;
        allDbDevices.clear();
        allDevicesReadIn = false;
        noDbDev = 0;
        String num = "0";
        String name = "No Name";
        String macaddr = "00:00:00:00:00:00";
        String power = "-99"; // temp to later coding
        String company = "No Company Name";
        mDbCursor = mDeviceSql.getAllDevices();
        mDbCursor.moveToFirst();
        int dbNum = mDbCursor.getCount();
        for (int i = 0; i < dbNum; i++) {
            num = String.valueOf(i + 1);
            name = mDbCursor.getString(2);
            macaddr = mDbCursor.getString(3);
            power = mDbCursor.getString(4);
            company = mDbCursor.getString(5);
            String[] oneDbDeviceData = {num, name, macaddr, power, company};
            allDbDevices.add(oneDbDeviceData);
            mDbCursor.moveToNext();
        }
        adapter = new DbViewList(context, allDbDevices);
        dbDevList = (ListView) dbRootView.findViewById(R.id.db_list);
        dbDevList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dbDevList.setAdapter(adapter);
        allDevicesReadIn = true;
        Log.i(TAG, "sqlToView End");
    }

}
