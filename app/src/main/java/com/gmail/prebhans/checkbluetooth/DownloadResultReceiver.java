package com.gmail.prebhans.checkbluetooth;


import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class DownloadResultReceiver extends ResultReceiver {
	
	private static final String TAG = "DownloadResultReceiver";
	
    private Receiver mReceiver;

    public DownloadResultReceiver(Handler handler) {
        super(handler);
        Log.i(TAG, "DownloadResultReceiver start");   
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
        Log.i(TAG, "onCreateView start");
    }

    public interface Receiver {
    	//Log.i(TAG, "Receiver start");
        void onReceiveResult(int resultCode, Bundle resultData);
        //Log.i(TAG, "onReceiveResult Down start");
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
    	Log.i(TAG, "onReceiveResult Down start");
    	if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}

