package com.gmail.prebhans.checkbluetooth;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

class CbtHelpMethods {

	public String getBtType(int type) {
		String returnType = "";

		/**
		 * Bluetooth device type, Unknown
		 * 
		 * int DEVICE_TYPE_UNKNOWN = 0;
		 * 
		 * 
		 * Bluetooth device type, Classic - BR/EDR devices
		 * 
		 * int DEVICE_TYPE_CLASSIC = 1;
		 * 
		 * 
		 * Bluetooth device type, Low Energy - LE-only
		 * 
		 * int DEVICE_TYPE_LE = 2;
		 * 
		 * 
		 * Bluetooth device type, Dual Mode - BR/EDR/LE
		 * 
		 * int DEVICE_TYPE_DUAL = 3;
		 */
		switch (type) {

		case 0:
			returnType = "Unknown";
			break;
		case 1:
			returnType = "Classic";
			break;
		case 2:
			returnType = "Low Energy";
			break;
		case 3:
			returnType = "Dual Mode";
			break;

		}

		return returnType;
	}

	public void callToast(Context context, String aToast, int duration) {
		// Log.i(TAG, "callToast start");
		CharSequence text = aToast;
		switch (duration) {
		case 0:
			duration = Toast.LENGTH_SHORT;
			break;
		case 1:
			duration = Toast.LENGTH_LONG;
			break;
		default:
			duration = Toast.LENGTH_SHORT;
			break;
		}
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	} // callToast

}
