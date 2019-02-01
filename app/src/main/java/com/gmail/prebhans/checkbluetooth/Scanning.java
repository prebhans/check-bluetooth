package com.gmail.prebhans.checkbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothAdapter.checkBluetoothAddress;

public class Scanning extends Fragment {
    private static final String TAG = "Scanning";
    // TODO Run for emulator true for real devices
    private FirebaseAnalytics mFirebaseAnalytics;
    private final boolean runEmulator = false;
    private static final int REQUEST_ENABLE_BT = 3;
    private final CbtHelpMethods btHelp = new CbtHelpMethods();
    private Context context;
    private View scanRootView;
    private Bundle args;
    private Button startButton;
    private Button stopButton;
    private LinearLayout ring_scan_buttons;
    private DBHelper mDeviceSql;
    private int noScannedDev = 0;
    private int noDbDevs = 0;
    private final int maxDbDevs = 25;
    private final int maxDevicesScan = 15;
    private boolean maxDeviceInDb = false;
    private final boolean userTestDevices = false;
    private final List<ScanDevice> allScannedDevices = new ArrayList<ScanDevice>();
    private ScanViewList adapter;
    private ListView devList;
    private BluetoothAdapter mBtAdapter;
    private boolean isRecRegOn = false; // start stop scan
    private Resources res;
    private boolean activityCreated = false;
    private TestDeviceDB deviceToSql;
    private ProgressBar mProgressBar;
    private DownloadResultReceiver mLookupReceiver;
    private String foundMAC;
    private int countCall = 100;

    private boolean runPermissionFirst = false;
    private boolean runPermissionSecond = false;
    private long startProgressTime; // start of progress for BT scan
    private long stopProgressTime; // stop of progress for BT scan

    private boolean callVolleyOngoing = false;
    private boolean bluetoothScanStarted = false;
    private int bluetoothDeveceFound = 0;
    private String deviceMacLastScanned;

    private final List<BluetoothDevice> tmpBtChecker = new ArrayList<BluetoothDevice>();

    private long volleyStartTime;
    private long volleyReturnTime;

    private Handler mHandler;
    private boolean timeLookupRun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreateView start");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        res = getResources();
        Crashlytics.log("Scanning created");
        mHandler = new Handler();
    } // onCreate

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView start");
        scanRootView = inflater.inflate(R.layout.scanning, container, false);
        args = getArguments();
        context = container.getContext();
        setupButtonLister();
        res = container.getResources();
        mDeviceSql = new DBHelper(context);
        return scanRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume start");
        long now = System.currentTimeMillis();
        if (!runEmulator)
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!allScannedDevices.isEmpty()) {
            adapter = new ScanViewList(context, allScannedDevices);
            devList = (ListView) scanRootView
                    .findViewById(R.id.list_device_new);
            devList.setAdapter(adapter);
            noScannedDev = allScannedDevices.size();
        } else {
            // #ToDo demo device set up
            String demoNum = "xx";
            String demoName = "Bluetooth name";
            String demoMacaddr = "XX:XX:XX:XX:XX:XX";
            String demoPower = "-99";
            String demoCompany = "Company name";
            ScanDevice oneDemoDeviceData = new ScanDevice(demoNum, demoName, demoMacaddr, demoPower, demoCompany);
            allScannedDevices.add(oneDemoDeviceData);
            adapter = new ScanViewList(context, allScannedDevices);
            devList = (ListView) scanRootView
                    .findViewById(R.id.list_device_new);
            devList.setAdapter(adapter);
        }
        Log.w(TAG, "onResume" + (System.currentTimeMillis() - now) + "ms");
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
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Scanning");
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
        if (!runEmulator) {
            if (mBtAdapter != null) {
                Log.i(TAG, "onDestroy mBtAdapter cancel");
                mBtAdapter.cancelDiscovery();
            }
        }
        if (isRecRegOn) {
            context.unregisterReceiver(mReceiver);
            isRecRegOn = false;
            Log.i(TAG, "onDestroy mReceiver cancel");
        }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint start");
        if (isVisibleToUser) {
            Log.i(TAG, "Fragment is visible.");
        } else {
            Log.i(TAG, "Fragment is not visible.");
        }
    }

    private void setupButtonLister() {
        Log.i(TAG, "setupButtonLister start");
        startButton = (Button) scanRootView.findViewById(R.id.start_scan);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick startButton");
                boolean mPermissionGranted = ((CheckBluetooth) getActivity()).checkFineLocation(); // prefs.getBoolean(READ_EXT_IMAGE, false);
                if (!mPermissionGranted) {
                    ((CheckBluetooth) getActivity()).checkPermission();
                    Log.i(TAG, "setupButtonLister Not Granted");
                    if (runPermissionFirst) runPermissionSecond = true;
                    runPermissionFirst = true;
                    if (runPermissionSecond) {
                        Toast mytoast = Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT);
                        //mytoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        //mytoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        mytoast.show();
                    }
                    //else {
                    //    Log.i(TAG, "setupButtonLister Granted");
                    //    if (mPermissionGranted) getStartButtonAction();
                    //}
                }
                Log.i(TAG, "setupButtonLister Granted" + " " + mPermissionGranted);
                if (mPermissionGranted) getStartButtonAction();
            }
        });
        stopButton = (Button) scanRootView.findViewById(R.id.stop_scan);
        stopButton.setOnClickListener(new

                                              OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Log.i(TAG, "onClick stopButton");
                                                      getStopButtonAction();
                                                  }
                                              }
        );
    }

    private void getStartButtonAction() {
        Log.i(TAG, "getStartButtonAction start");
        if (!runEmulator) {
            if (!mBtAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }
        }
        startProgressBar();
        ring_scan_buttons = (LinearLayout) scanRootView
                .findViewById(R.id.button_scanning);
        ring_scan_buttons
                .setBackgroundResource(R.drawable.main_button_red_ring);
        allScannedDevices.clear();
        maxDeviceInDb = false;
        noScannedDev = 0;
        //
        startDeviceScan();
        startTimeLookup();
        timeLookupRun = true;
        //checkNextLookupDevice();
        doDiscovery();
        if (userTestDevices) { // normal false
            deviceToSql = new TestDeviceDB();
            deviceToSql.getDevices();
            testDeviceToViewAndSql();
        }
    }

    private void getStopButtonAction() {
        Log.i(TAG, "getStopButtonAction start");
        if (!runEmulator) {
            if (mBtAdapter != null) {
                mBtAdapter.cancelDiscovery();
            }
        }
        if (isRecRegOn) {
            context.unregisterReceiver(mReceiver);
            isRecRegOn = false;
        }
        stopProgressBar();
        timeLookupRun = false;
        ring_scan_buttons = (LinearLayout) scanRootView
                .findViewById(R.id.button_scanning);
        ring_scan_buttons
                .setBackgroundResource(R.drawable.main_button_green_ring);
    }

    private void startProgressBar() {
        Log.i(TAG, "startProgressBar start");
        mProgressBar = (ProgressBar) scanRootView
                .findViewById(R.id.progressBar1);
        mProgressBar.setVisibility(View.VISIBLE);
        startProgressTime = System.currentTimeMillis() / 1000;
    }

    private void stopProgressBar() {
        Log.i(TAG, "stopProgressBar start");
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        stopProgressTime = System.currentTimeMillis() / 1000;
        Log.i(TAG, "stopProgressBar timer" + " " + (stopProgressTime - startProgressTime));
    }

    private void startDeviceScan() {
        Log.i(TAG, "startDeviceScan start");
        // TODO Intent
        // Register for broadcasts when a device is discovered
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter1);
        // Register for broadcasts when discovery has finished
        IntentFilter filter2 = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter2);
        IntentFilter filter3 = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        context.registerReceiver(mReceiver, filter3);
        isRecRegOn = true; // Bluetooth is started
        //
        if (adapter != null)
            adapter.clear();
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    // TODO Broadcast
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        //Log.i(TAG, "setupButtonLister start");
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.i(TAG, "mReceiver start");
            String action = intent.getAction();
            // When discovery starts
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i(TAG, "mReceiver start ACTION_DISCOVERY_STARTED");
                tmpBtChecker.clear();
                bluetoothScanStarted = true;
                bluetoothDeveceFound = 0;
            }
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!tmpBtChecker.contains(device)) {
                    //Log.i(TAG, "mReceiver contain not device");
                    tmpBtChecker.add(device);
                    bluetoothDeveceFound++;
                } else {
                    //Log.i(TAG, "mReceiver contain device");
                    return;
                }
                // get power rssi
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
                        Short.MIN_VALUE);
                Log.i(TAG, "mReceiver update List Gui" + " " + device + " " + rssi);

                if(bluetoothDeveceFound < maxDevicesScan) {
                    updateListGui(device, rssi);
                } else {
                    Toast.makeText(getActivity(), "Max scanned devices",
                            Toast.LENGTH_SHORT).show();
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                Log.i(TAG, "mReceiver end ACTION_DISCOVERY_FINISHED");
                getStopButtonAction();
                bluetoothScanStarted = false;
            }
        }
    };

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        Log.i(TAG, "doDiscovery start");
        // If we're already discovering, stop it
        if (!runEmulator) {
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }
            // Request discover from BluetoothAdapter
            mBtAdapter.startDiscovery();
        }
    }

    private void updateListGui(BluetoothDevice device, int rssi) {
        Log.i(TAG, "updateListGui");
        boolean isMacInScanning = false;
        String name = device.getName();
        String power = String.valueOf(rssi);
        String macaddr = device.getAddress();
        String company = "Company name";  // Company name No Vendor Found

        for (int i = 0; i < allScannedDevices.size(); i++) {
            String mac1 = allScannedDevices.get(i).getmMacaddr();
            String mac2 = macaddr;
            isMacInScanning = mac1.equals(mac2);
            if (isMacInScanning) {
                Log.i(TAG, "updateListGui isMacInScanning" + " " + isMacInScanning);
                return;
            }
        }

        if (!isMacInScanning) {
            noScannedDev++;
            String num = String.valueOf(noScannedDev);
            ScanDevice oneScanDeviceData = new ScanDevice(num, name, macaddr, power, company);
            allScannedDevices.add(oneScanDeviceData);
            adapter = new ScanViewList(context, allScannedDevices);
            devList = (ListView) scanRootView.findViewById(R.id.list_device_new);
            devList.setAdapter(adapter);
            String mDeviceStr = "Num : " + num + "\n" + "Name : " + name + "\n"
                    + "MAC Addr. : " + macaddr + "\n" + "pwr : " + power + "\n" + "Company :" + company;
            Log.i(TAG + " Str", "deviceToSql" + "\n " + mDeviceStr);
            // Start lookup, call class LookupMac
            //lookupCompanyName(macaddr);
            //checkNextLookupDevice();
        } else {
            Log.i(TAG, "deviceToSql all ready");
        }
        if (!isMacInScanning) {
            deviceToSql(device, rssi);
        }
    }


    // this is the right database accress
    private void deviceToSql(BluetoothDevice device, int rssi) {
        Log.i(TAG, "deviceToSql start");
        boolean deviceInDb;
        String name = device.getName();
        String power = String.valueOf(rssi);
        String macaddr = device.getAddress();
        String num = String.valueOf(noScannedDev);
        String company = "No Vendor Found";
        //
        Log.i(TAG, "deviceToSql noScannedDev++" + noScannedDev);
        noDbDevs = mDeviceSql.getDevicesCount();
        if (noDbDevs >= maxDbDevs)
            maxDeviceInDb = true;
        if (!maxDeviceInDb) {            // Read device to database
            //
            deviceInDb = mDeviceSql.isDeviceInDb(device.getAddress());
            if (!deviceInDb) {
                mDeviceSql.insertDevice(num, name, macaddr, power, company);
            }
        } else {
            btHelp.callToast(context, "Only " + " " + maxDbDevs + " "
                    + "devices in database", 0);
        }
        Log.i(TAG, "deviceToSql end");
    }

    private void checkNextLookupDevice() {
        Log.i(TAG, "checkNextLookupDevice" + " " + callVolleyOngoing);
        int devices = allScannedDevices.size();
        //boolean foundOne = false;
        if (!callVolleyOngoing) {
            //for (int i = 0; allScannedDevices.size() > i; i++) {
            for (ScanDevice scanDevice : allScannedDevices) {
                if (scanDevice.getmCompany().contains("Company name") && !scanDevice.isFirstCheckOk()) {
                    Log.i(TAG, "checkNextLookupDevice first" + " " + scanDevice.getmMacaddr());
                    lookupCompanyName(scanDevice.getmMacaddr());
                    scanDevice.setFirstCheckOk(true); // set first check
                    //foundOne = true;
                    break;
                } else if (scanDevice.getmCompany().contains("No Vendor Found") && !scanDevice.isSecondCheckOk()) {   // check for second time isSecondCheckOk
                    Log.i(TAG, "checkNextLookupDevice second" + " " + scanDevice.getmMacaddr());
                    lookupCompanyName(scanDevice.getmMacaddr());
                    scanDevice.setSecondCheckOk(true); // set first check
                    //foundOne = true;
                    break;
                    // check for second time isSecondCheckOk
                } else {
                    Log.i(TAG, "checkNextLookupDevice third");
                    //for (ScanDevice scanDevice2 : allScannedDevices) {
                        //Log.i(TAG, "checkNextLookupDevice third" + " " + scanDevice.getmMacaddr() + " " + scanDevice.getmCompany());
                    //}
                }
            }
            Log.i(TAG, "checkNextLookupDevice end " + " " + devices);
        }
    }



    // get company name from look up
    private void lookupCompanyName(String macaddr) {
        Log.i(TAG, "lookupCompanyName");
        boolean bluetoothAddressOK;
        boolean networkOK;
        //String stringUrl = "http://www.macvendorlookup.com/api/v2/";
        //String stringUrlNew = "https://api.macvendors.com/";
        String stripMacAddress;
        String localResultFromSendUrl;
        // check MAC address
        bluetoothAddressOK = checkBluetoothAddress(macaddr);
        // Is network OK
        networkOK = checkNetworkOK();
        //if (bluetoothAddressOK && networkOK && !cdtOn && !callVolleyOngoing) {
        if (bluetoothAddressOK && networkOK) {
            Log.i(TAG, "lookupCompanyName start lookup");
            //stripMacAddress = getStrippedMacAddress(macaddr);
            final String stringUrlNew = "https://api.macvendors.com/" + macaddr;
            //stringUrlNew = stringUrlNew + macaddr;
            volleyCall(macaddr, stringUrlNew, countCall++);
            //volleyCallDelay(macaddr, stringUrlNew, countCall++);
            //callVolleyOngoing = true;
            deviceMacLastScanned = macaddr;
        }
    }

    private String getStrippedMacAddress(String macaddr) {
        String localStripMacAddress;
        localStripMacAddress = macaddr.replace(":", "-");
        return localStripMacAddress;
    }

    // check network before call
    private boolean checkNetworkOK() {
        //
        Log.i(TAG, "checkNetworkOK start");
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            return true;
        } else {
            // display error
            return false;
        }
    }

    private void volleyCallDelay(final String macaddr, final String urlToSend, final int controlValue) {
        Log.i(TAG, "volleyCallDelay");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                volleyCall(macaddr, urlToSend, controlValue);
            }
        }, 1500);

    }


    private void volleyCall(final String macaddr, String urlToSend, int controlValue) {
        Log.i(TAG, "volleyCall start" + " " + urlToSend);
        volleyStartTime = System.currentTimeMillis();
        //Log.i(TAG, "volleyCall start" + " " + macaddr);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = urlToSend;
        callVolleyOngoing = true;
        //String url = macaddr;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "volleyCall response" + " " + response);
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        callVolleyOngoing = false;
                        //companyNameToDb(response);
                        volleyReturnTime = System.currentTimeMillis();
                        Log.i(TAG, "volleyReturnTime" + " " + (volleyReturnTime - volleyStartTime));

                        updateWithCompanyName(macaddr, response);

                        Log.i(TAG, "volleyCall response" + " " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "volleyCall response error" + " " + error);
                callVolleyOngoing = false;
                volleyReturnTime = System.currentTimeMillis();
                updateWithCompanyName(macaddr, "No name found");
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        Log.i(TAG, "volleyCall end");
    }

    private void updateWithCompanyName(String firstHalfMac, String company) {
        Log.i(TAG, "updateWithCompanyName start");
        String localFirstHalfMac;
        int id;
        String lNum = "";
        String lName = "";
        String lMacaddr = "";
        String lPower = "";
        String lCompany = "";
        String foundMac;
        boolean found = false;
        boolean deviceInDb;
        localFirstHalfMac = firstHalfMac.substring(0, 8);
        //
        for (int i = 0; allScannedDevices.size() > i; i++) {
            String localOneScanDeviceData = allScannedDevices.get(i).getmMacaddr();
            //Log.i(TAG, "updateWithCompanyName for" + " " + localOneScanDeviceData);
            if (localOneScanDeviceData.contains(localFirstHalfMac)) {
                foundMac = localOneScanDeviceData;
                allScannedDevices.get(i).setmCompany(company);
                found = true;
                allScannedDevices.get(i).setFirstCheckOk(true);
                adapter = new ScanViewList(context, allScannedDevices);
                devList = (ListView) scanRootView.findViewById(R.id.list_device_new);
                devList.setAdapter(adapter);
                // Read device to database
                deviceInDb = mDeviceSql.isDeviceInDb(foundMac);
                if (deviceInDb) {
                    id = mDeviceSql.getDeviceId(foundMac);
                    mDeviceSql.updateDevice(id, lNum, lName, lMacaddr, lPower, company);
                    Log.i(TAG, "updateWithCompanyName company" + " " + company);
                }
            }
            if (found) return;
        }

    }


    private void startTimeLookup() {
        Log.i(TAG, "startTimeLookup");
//      New thread to perform background operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                //for (int i = 0; i <= 30; i++) {
                while (timeLookupRun) {
                    Log.i(TAG, "startTimeLookup mHandler start");
                    //final int currentProgressCount = i;
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                  Update the value background thread to UI thread
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "startTimeLookup mHandler post");
                    checkNextLookupDevice();
                            //mProgressBar.setProgress(currentProgressCount);
                        }
                    });
                }
            }
        }).start();
    }


    // this is a test option with TestDeviceDB
    private void testDeviceToViewAndSql() {
        Log.i(TAG, "testDeviceToViewAndSql start");
        // TestDeviceDB deviceToSql = new TestDeviceDB();
        // deviceToSql.initDevices();
        int countDevInDb;
        boolean deviceInDb;
        // String tenDevice[] =
        // {"name","power","macaddr","type","date","time","timestamp"};
        String num;
        String name;
        String macaddr;
        String power;
        String company = "No device name";
        for (int id = 1; id < 10; id++) {
            deviceInDb = false;
            //
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            noScannedDev++;
            num = String.valueOf(noScannedDev);
            // num = String.valueOf(noScannedDev);
            name = deviceToSql.getName(id);
            power = deviceToSql.getPower(id);
            macaddr = deviceToSql.getMacAddr(id);
            countDevInDb = mDeviceSql.getDevicesCount();
            if (countDevInDb >= maxDbDevs)
                maxDeviceInDb = true;
            if (!maxDeviceInDb) {
                //
                macaddr = deviceToSql.getMacAddr(id);
                if (macaddr != null && macaddr.length() == 17) {
                    deviceInDb = mDeviceSql.isDeviceInDb(deviceToSql
                            .getMacAddr(id));
                }
                // noScannedDev++;
                if (!deviceInDb) {
                    // if (device.getName() != null) {
                    mDeviceSql.insertDevice(String.valueOf(noScannedDev),
                            deviceToSql.getName(id),
                            deviceToSql.getMacAddr(id),
                            deviceToSql.getPower(id),
                            company);
                    Log.i(TAG, "testDeviceToViewAndSql count" + " "
                            + noScannedDev);
                }
            }
            ScanDevice oneScanDeviceData = new ScanDevice(num, name, macaddr, power, company);
            allScannedDevices.add(oneScanDeviceData);
            adapter = new ScanViewList(context, allScannedDevices);
            devList = (ListView) scanRootView
                    .findViewById(R.id.list_device_new);
            devList.setAdapter(adapter);
            // allDevicesReadIn = true;
            // Start lookup, call class LookupMac
            //lookupCompanyName(macaddr);
        }
    }


}
