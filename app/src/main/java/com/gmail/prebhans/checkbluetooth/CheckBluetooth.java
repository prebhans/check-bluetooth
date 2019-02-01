package com.gmail.prebhans.checkbluetooth;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

//import com.google.analytics.tracking.android.EasyTracker;

public class CheckBluetooth extends FragmentActivity {
    private static final String TAG = "CheckBluetooth";
    private FirebaseAnalytics mFirebaseAnalytics;
    // TODO set to false in release
    private final boolean test = false; // false by release
    private boolean adOn = true; // true by release
    //private final boolean trackerOn = true; // true by release
    // --Commented out by Inspection (02-03-15 10:55):int whichPageInView;
    private int currentapiVersion;
    private int versionNumber;
    private String versionName;
    private SharedPreferences pasPrefs;
    private boolean isFirstTime;
    // int whichTabSelected; // should show selected tabs
    private Resources res;
    /**
     * The view to show the ad.
     */
    private AdView mAdView;
    private AdRequest adRequest;
    private String deviceId = "AD194A9698BB69E5B93F6F5A7779431D"; // samsung 5.0
    private final String nexus5 = "EA60F3BF1AF83A83F7BA0E63CD779CB8";
    private final String nexus5x = "4AACB479AEDC919FC77D0A3765DED17D";
    private final String pixel2 = "B410CE54C4F27202C516A0DEDACF96BA";
    private final String samsungg950 = "9A6F0260639436423BE4A65229685F0A";
    private final String samsungt815 = "3FE5B937E71702B9B7E56F0915F68197";
    private String htc = "08A30DE8DF6BE57C499DB852B30F12C9"; // HTC Desire HD
    /* Your ad unit id. Replace with your actual ad unit id. */
    //private final String AD_UNIT_ID_MAIN = "ca-app-pub-";
    private AdSize adSize = AdSize.BANNER;
    //private EasyTracker myTracker = null;
    //private static GoogleAnalytics analytics;
    //private static Tracker tracker;

    // --Commented out by Inspection START (02-03-15 10:55):
//	/**
//	 * The {@link ViewPager} that will host the section contents.
//	 */
//	ViewPager mViewPager;
// --Commented out by Inspection STOP (02-03-15 10:55)
    private int actPagePosition = 0;
    private PackageManager localPackageManager;
    private Context context;

    protected boolean mPermissionGranted;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate start");
        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        res = getResources();
        // Sample AdMob app ID: ca-app-pub-
        MobileAds.initialize(this, getString(R.string.ad_unit_id_main_tilt));

        // supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //new AppEULA(this).show();
        //#ToDo check new release
        if (test)
            measDimension();
        setContentView(R.layout.activity_main);
        String appTitle = res.getString(R.string.app_fuld_name);
        //setTitle(" " + appTitle);
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setTitle(" " + res.getString(R.string.set_title));
        bar.setSubtitle("     " + res.getString(R.string.set_sub_title));

        localPackageManager = getPackageManager();
        context = getApplicationContext();
        /** For sliding tabs **/
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment(
                    context);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        //Todo Permission check
        mPermissionGranted = checkFineLocation(); // prefs.getBoolean(READ_EXT_IMAGE, false);
        if (!mPermissionGranted) checkPermission();

        //#ToDo check new release
        /** go to adMob setup **/
        if (adOn)
            adSetup(); // go to advertising setup
        if (test)
            callAdListener();
        Crashlytics.log("A15Puzzle onCreate");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu start");
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected start");
        /*
         * Handle action bar item clicks here. The action bar will automatically
         * handle clicks on the Home/Up button, so long as you specify a parent
         * activity in AndroidManifest.xml.
         */
        int id = item.getItemId();
        //
        if (id == R.id.menu_bluetooth) {
            getBtSettings();
            return true;
        }
        //
        if (id == R.id.menu_settings) {
            getMainSettings();
            return true;
        }
        //
        if (id == R.id.menu_help) {
            getHelpMenu();
            return true;
        }
        //
        if (id == R.id.menu_rate) {
            getRateMenu();
            return true;
        }
        //
        if (id == R.id.menu_share) {
            getShareMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart start");
        // TODO release version
        // for analyzing user
        int versionCode = BuildConfig.VERSION_CODE;
        String versionCodeStr = Integer.toString(versionCode);
        String versionName = BuildConfig.VERSION_NAME;
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, versionCodeStr);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, versionName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "CheckBluetooth");
        bundle.putString(FirebaseAnalytics.Event.APP_OPEN, "onStart");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Log.i(TAG, "onStart end");
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart start");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume start");
        long now = System.currentTimeMillis();
        if (adOn) mAdView.resume();
        //#ToDo check new release
        if (test)
            checkGooglePlayService();
        pasPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstTime = pasPrefs.getBoolean("isFirstTime", true);
        isFirstTime = false;
        Log.w(TAG, "onResume" + (System.currentTimeMillis() - now) + "ms");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause start");
        pasPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pasPrefs.edit();
        editor.putBoolean("isFirstTime", isFirstTime);
        editor.apply();
        if (adOn) mAdView.pause();
        super.onPause();
    } // onPause

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop start");
        // TODO release version
        //if (trackerOn) {
        //EasyTracker.getInstance(this).activityStop(this);
        //analytics.reportActivityStop(this);

        //}
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy start");
        if (adOn) mAdView.destroy();
        super.onDestroy();
    }

    // onOverride ends
    /*
     * Only runned in test
     */
    private void measDimension() {
        Log.i(TAG, "measDimension start");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int sWidth = metrics.widthPixels;
        int sHeight = metrics.heightPixels;
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            //
            e.printStackTrace();
        }
        versionNumber = pinfo != null ? pinfo.versionCode : 0;
        versionName = pinfo != null ? pinfo.versionName : null;
        float sDensity = metrics.density;
        int sDensityDpi = metrics.densityDpi;
        float sScaledDensity = metrics.scaledDensity;
        float sXdpi = metrics.xdpi;
        float sYdpi = metrics.ydpi;
        Log.i(TAG, "measDimension sWidth" + " " + sWidth);
        Log.i(TAG, "measDimension sHeight" + " " + sHeight);

        Log.i(TAG, "measDimension density_default" + " " + DisplayMetrics.DENSITY_DEFAULT);

        Log.i(TAG, "measDimension sDensity" + " " + sDensity);
        Log.i(TAG, "measDimension sDensityDpi" + " " + sDensityDpi);
        Log.i(TAG, "measDimension sScaledDensity" + " " + sScaledDensity);
        Log.i(TAG, "measDimension sXdpi" + " " + sXdpi);
        Log.i(TAG, "measDimension sYdpi" + " " + sYdpi);

        Log.i(TAG, "measDimension currentapiVersion" + " " + currentapiVersion);
        Log.i(TAG, "measDimension versionNumber" + " " + versionNumber);
        Log.i(TAG, "measDimension versionName" + " " + versionName);

    } // measDimension - not used


    /*
     * Setup adMob and test devices
     */
    private void adSetup() {
        Log.i(TAG, "adSetuo start");
        // // Create an ad. Create an adView.

        //mAdView = new AdView(this);
        //mAdView.setAdSize(adSize);
        //mAdView.setAdUnitId(AD_UNIT_ID_MAIN);
        // Ad listener in test mode
        // TODO udv version test true
        //if (test)
        //    callAdListener();
        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        //AdView adView = (AdView) findViewById(R.id.adView);
        //adView.addView(mAdView);

        mAdView = findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        // TODO udv version test true
        if (test) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(deviceId) // My T-Mobile G1 test phone
                    .addTestDevice(nexus5)
                    .addTestDevice(nexus5x)
                    .addTestDevice(pixel2)
                    .addTestDevice(samsungg950)
                    .addTestDevice(samsungt815)
                    .addTestDevice(htc).build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        // load ad
        mAdView.loadAd(adRequest);
    } // adSetup end

    private void callAdListener() {
        Log.i(TAG, "callAdListener start");
        // Set the AdListener.
        if (adOn && mAdView != null) mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded");
                Toast.makeText(CheckBluetooth.this, "onAdLoaded",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad" + " " + errorCode);
                Toast.makeText(CheckBluetooth.this, "onAdFailedToLoad" + " " + errorCode,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG, "onAdOpened");
                Toast.makeText(CheckBluetooth.this, "onAdOpened",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG, "onAdLeftApplication");
                Toast.makeText(CheckBluetooth.this, "onAdLeftApplication",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "onAdClosed");
                Toast.makeText(CheckBluetooth.this, "onAdClosed",
                        Toast.LENGTH_SHORT).show();
            }
        });


    } // callAdListener end

    /**
     * Gets a string error reason from an error code.
     */
    private String getErrorReason(int errorCode) {
        Log.i(TAG, "getErrorReason start");
        String errorReason = "";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network Error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }
        return errorReason;
    } // getErrorReason end

    /*
     * Called from Menu
     */
    private void getBtSettings() {
        Log.i(TAG, "getBtSettings start");
        Intent settings_intent = new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivityForResult(settings_intent, 0);
    }

    /*
     * Called from Menu
     */
    private void getMainSettings() {
        Log.i(TAG, "getMainSettings start");
        Intent settings_intent = new Intent(
                android.provider.Settings.ACTION_SETTINGS);
        startActivityForResult(settings_intent, 0);
        //
    }

    /*
     * Called from Menu
     */
    private void getHelpMenu() {
        Log.i(TAG, "getHelpMenu start");
        //
        String title = this.getString(R.string.app_fuld_name);
        // String message = this.getString(R.string.main_help);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater localLayoutInflater = this.getLayoutInflater();
        builder.setIcon(getPackageManager().getApplicationIcon(
                getApplicationInfo()));
        builder.setTitle(title);
        // builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle(localLayoutInflater.inflate(R.layout.alert_head,
        // null));
        builder.setView(localLayoutInflater.inflate(R.layout.main_help, null));
        builder.setNeutralButton(R.string.ok, new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        // setMessage(message)
        builder.create().show();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    }

    /*
     * Called from Menu
     */
    private void getRateMenu() {
        Log.i(TAG, "getRateMenu start");
        Uri uri = Uri
                .parse(res.getString(R.string.link_google_play));
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(res.getString(R.string.link_google_play))));
        }

    }

    /*
     * Called from Menu
     */
    private void getShareMenu() {
        Log.i(TAG, "getShareMenu start");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType(res.getString(R.string.share_mode));
        String shareBody = res.getString(R.string.link_google_play);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                res.getString(R.string.share_friends));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,
                res.getString(R.string.share_via)));

        //
    }

    /*
     * Called only in test mode
     */
    private void checkGooglePlayService() {
        Log.i(TAG, "checkGooglePlayService start");
        // SUCCESS, SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
        // SERVICE_DISABLED, SERVICE_INVALID, DATE_INVALID.
        //int statusCode = GooglePlayServicesUtil
        //        .isGooglePlayServicesAvailable(getApplicationContext());

        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        if (statusCode == ConnectionResult.SUCCESS) {
            Toast.makeText(CheckBluetooth.this,
                    "checkGooglePlayService" + " " + "SUCCESS",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkGooglePlayService" + " " + "SUCCESS");
        }
        if (statusCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(CheckBluetooth.this,
                    "checkGooglePlayService" + " " + "SERVICE_MISSING",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkGooglePlayService" + " " + "SERVICE_MISSING");
        }
        if (statusCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            Toast.makeText(
                    CheckBluetooth.this,
                    "checkGooglePlayService" + " "
                            + "SERVICE_VERSION_UPDATE_REQUIRED",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkGooglePlayService" + " "
                    + "SERVICE_VERSION_UPDATE_REQUIRED");
        }
        if (statusCode == ConnectionResult.SERVICE_DISABLED) {
            Toast.makeText(CheckBluetooth.this,
                    "checkGooglePlayService" + " " + "SERVICE_DISABLED",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkGooglePlayService" + " " + "SERVICE_DISABLED");
        }
        if (statusCode == ConnectionResult.SERVICE_INVALID) {
            Toast.makeText(CheckBluetooth.this,
                    "checkGooglePlayService" + " " + "SERVICE_INVALID",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkGooglePlayService" + " " + "SERVICE_INVALID");
        }
        // if (statusCode == ConnectionResult.DATE_INVALID) {
        // Toast.makeText(CheckBluetooth.this,
        // "checkGooglePlayService" + " " + "DATE_INVALID",
        // Toast.LENGTH_SHORT).show();
        // Log.i(TAG, "checkGooglePlayService" + " " + "DATE_INVALID");
        // }
    }


// Permission check

    /**
     * Just a check
     *
     * @return
     */
    protected Boolean checkFineLocation() {
        Log.i(TAG, "checkFineLocation start");
        boolean permissionOk = true;  //local
        if ((android.os.Build.VERSION.SDK_INT) >= 23) {
            // Assume thisActivity is the current activity
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) permissionOk = false;
        } else {
            permissionOk = true;
        }
        return permissionOk;
    }

    /**
     * Override return permission and add to mPermissionGranted
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult start");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Log.i(TAG, "onRequestPermissionsResult true");
                    mPermissionGranted = true;
                } else {
                    // Permission Denied
                    Log.i(TAG, "onRequestPermissionsResult false");
                    mPermissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                Log.i(TAG, "onRequestPermissionsResult end");
        }
    }


    /**
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected boolean checkPermission() {
        Log.i(TAG, "checkPermission start");
        boolean permissionOk = false;

        if ((android.os.Build.VERSION.SDK_INT) >= 23) {
            // Assume thisActivity is the current activity
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;

                //SharedPreferences.Editor editor = prefs.edit();
                //editor.putBoolean(READ_EXT_IMAGE, mPermissionGranted);
                //editor.apply();

                permissionOk = false;

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // It is false.
                    //
                    // This method returns true if the app has requested this permission
                    // previously and the user denied the request.
                    //
                    // If the user turned down the permission request in the past and chose the
                    // Don't ask again option in the permission request system dialog, this method
                    // returns false.
                    //
                    // The method also returns false if a device policy prohibits the app from
                    // having that permission.
                    Log.i(TAG, "checkPermission should true");
                    showMessageOKCancel(R.string.need_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getrequestPermissions();
                        }
                    });

                } else {
                    Log.i(TAG, "checkPermission should false");
                    getrequestPermissions();
                }
            } else {
                mPermissionGranted = true;
                Log.i(TAG, "checkPermission granted true");
                permissionOk = true;
            }

        } else { // below API 23
            mPermissionGranted = true;
            permissionOk = true;
        }
        Log.i(TAG, "checkPermission end");
        return permissionOk;
    }

    /**
     * @param message
     * @param okListener
     */
    private void showMessageOKCancel(int message, DialogInterface.OnClickListener okListener) {
        Log.i(TAG, "showMessageOKCancel start");
        int intMessage = message;
        new AlertDialog.Builder(CheckBluetooth.this)
                .setMessage(intMessage)
                .setPositiveButton(R.string.ok, okListener)
                //.setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getrequestPermissions() {
        Log.i(TAG, "getrequestPermissions start");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_ASK_PERMISSIONS);
    }


}
