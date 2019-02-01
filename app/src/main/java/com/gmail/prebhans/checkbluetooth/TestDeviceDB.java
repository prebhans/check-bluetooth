package com.gmail.prebhans.checkbluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestDeviceDB extends Activity {
	private static final String TAG = "TestDeviceDB";
	int id = 0;
	int idMax = 50;
	String[] name = new String[idMax];
	String[] power = new String[idMax];
	String[] macaddr = new String[idMax];
	String[] type = new String[idMax];
	String[] date = new String[idMax];
	String[] time = new String[idMax];
	String[] timestamp = new String[idMax];

	public TestDeviceDB() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate start");
	} // onCreate

	public void getDevices() {
		Log.i(TAG, "getDevices start");
		initDevicesA();
		initDevicesB();
		initDevicesC();
		initDevicesD();
	}

	public void initDevicesA() {
		Log.i(TAG, "initDevices start");

		// device [0] [8] =
		// {"no","name","power","macaddr","type","date","time","timestamp"};

		id = 1; // 1,”Desire
				// HD”,"-42”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”11:40”,”1413272078881”
		name[id] = "Desire HD";
		power[id] = "-42";
		macaddr[id] = "BC:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "04/11/2014";
		time[id] = "11:40";
		timestamp[id] = "1413272078881";

		id = 2; // 2,"YP-G70","-40”,"BC:F5:AC:89:CD:08",”1”,”03/11/2014”,”08:09”,”1413272000843”
		name[id] = "YP-G70";
		power[id] = "-40";
		macaddr[id] = "DE:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "03/11/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 3; // 3,"Samsung LED46","-79”,"BC:F5:AC:89:CD:08",”1”,”26/10/2014”,”16:11”,"1413186240280"
		name[id] = "Samsung LED46";
		power[id] = "-79";
		macaddr[id] = "FA:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "26/10/2014";
		time[id] = "16:11";
		timestamp[id] = "1413272078881";

		id = 4; // 4,"DTVBluetooth","-55”,"BC:F5:AC:89:CD:08",”1”,”18/10/2014”,”13:09”,"1413186238581”
		name[id] = "DTVBluetooth";
		power[id] = "-42";
		macaddr[id] = "A4:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "18/10/2014";
		time[id] = "13:09";
		timestamp[id] = "1413272078881";

		id = 5; // 5,”Samsung
				// Mini,”_64”,”90:21:55:AB:AD:BF”,”1”,”09/09/2014”,”08:09”,”1415084976158”
		name[id] = "Samsung Mini";
		power[id] = "-64";
		macaddr[id] = "B5:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "09/09/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 6; // 6,”Nexus
				// 5”,”-33”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”08:17”,”1234567890465”
		name[id] = "Nexus 5";
		power[id] = "-33";
		macaddr[id] = "C6:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "04/11/2014";
		time[id] = "08:17";
		timestamp[id] = "1413272078881";

		id = 7; // 7,”JABRA
				// EASYGO”,”-75”,"BC:F5:AC:89:CD:08",”2”,”17/09/2013”,”15:33”,”1413435760938”
		name[id] = "JABRA EASYGO";
		power[id] = "-75";
		macaddr[id] = "D7:F5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "17/09/2013";
		time[id] = "15:33";
		timestamp[id] = "1413272078881";

		id = 8; // 8,”Nokia
				// old”,”-81”,"BC:F5:AC:89:CD:08",”2”,”04/06/1999”,”17:17”,”1413435384038”
		name[id] = "Nokia old";
		power[id] = "-81";
		macaddr[id] = "E8:F5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "04/06/1999";
		time[id] = "17:17";
		timestamp[id] = "1413272078881";

		id = 9; // 9,”Sony
				// Ericsson”,”-77”,"BC:F5:AC:89:CD:08",”1”,”04/04/2004”,”09:09”,”1413439772484”
		name[id] = "Sony Ericsson";
		power[id] = "-77";
		macaddr[id] = "F9:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "04/04/2004";
		time[id] = "09:09";
		timestamp[id] = "1413272078881";

		id = 10; // 10,”LG Galaxi
					// 900”,”-63”,"BC:F5:AC:89:CD:08",”1”,”21:04/2007”,”11:11”,”1413439712840”
		name[id] = "LG Galaxi 900";
		power[id] = "-63";
		macaddr[id] = "A1:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "21/04/2007";
		time[id] = "11:11";
		timestamp[id] = "1413272078881";

	}

	public String getName(int id) {
		Log.i(TAG, "getName start");
		if ((0 < id) && (id < 51))
			return name[id];
		return "";
	}

	public String getPower(int id) {
		Log.i(TAG, "getPower start");
		if ((0 < id) && (id < 51))
			return power[id];
		return "";
	}

	public String getMacAddr(int id) {
		Log.i(TAG, "getMacAddr start");
		if ((0 < id) && (id < 51))
			return macaddr[id];
		return "";
	}

	public String getType(int id) {
		Log.i(TAG, "getType start");
		if ((0 < id) && (id < 51))
			return type[id];
		return "";
	}

	public String getDate(int id) {
		Log.i(TAG, "getDate start");
		if ((0 < id) && (id < 51))
			return date[id];
		return "";
	}

	public String getTime(int id) {
		Log.i(TAG, "getTime start");
		if ((0 < id) && (id < 51))
			return time[id];
		return "";
	}

	public String getTimestamp(int id) {
		Log.i(TAG, "getTimestamp start");
		if ((0 < id) && (id < 51))
			return timestamp[id];
		return "";
	}

	public void initDevicesB() {
		Log.i(TAG, "initDevices start");

		// device [0] [8] =
		// {"no","name","power","macaddr","type","date","time","timestamp"};

		id = 11; // 1,”Desire
					// HD”,"-42”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”11:40”,”1413272078881”
		name[id] = "Desire HD_BB";
		power[id] = "-62";
		macaddr[id] = "BC:F5:BC:89:CD:08";
		type[id] = "2";
		date[id] = "04/11/2014";
		time[id] = "11:40";
		timestamp[id] = "1413272078881";

		id = 12; // 2,"YP-G70","-40”,"BC:F5:AC:89:CD:08",”1”,”03/11/2014”,”08:09”,”1413272000843”
		name[id] = "YP-G70_BB";
		power[id] = "-45";
		macaddr[id] = "DE:F5:BC:89:CD:08";
		type[id] = "3";
		date[id] = "03/11/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 13; // 3,"Samsung LED46","-79”,"BC:F5:AC:89:CD:08",”1”,”26/10/2014”,”16:11”,"1413186240280"
		name[id] = "Samsung LED46_BB";
		power[id] = "-69";
		macaddr[id] = "FA:F5:BC:89:CD:08";
		type[id] = "1";
		date[id] = "26/10/2014";
		time[id] = "16:11";
		timestamp[id] = "1413272078881";

		id = 14; // 4,"DTVBluetooth","-55”,"BC:F5:AC:89:CD:08",”1”,”18/10/2014”,”13:09”,"1413186238581”
		name[id] = "DTVBluetooth_BB";
		power[id] = "-52";
		macaddr[id] = "A4:F5:BC:89:CD:08";
		type[id] = "1";
		date[id] = "18/10/2014";
		time[id] = "13:09";
		timestamp[id] = "1413272078881";

		id = 15; // 5,”Samsung
					// Mini,”_64”,”90:21:55:AB:AD:BF”,”1”,”09/09/2014”,”08:09”,”1415084976158”
		name[id] = "Samsung Mini_BB";
		power[id] = "-74";
		macaddr[id] = "B5:F5:BC:89:CD:08";
		type[id] = "1";
		date[id] = "09/09/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 16; // 6,”Nexus
					// 5”,”-33”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”08:17”,”1234567890465”
		name[id] = "Nexus 5_BB";
		power[id] = "-83";
		macaddr[id] = "C6:F5:BC:89:CD:08";
		type[id] = "0";
		date[id] = "04/11/2014";
		time[id] = "08:17";
		timestamp[id] = "1413272078881";

		id = 17; // 7,”JABRA
					// EASYGO”,”-75”,"BC:F5:AC:89:CD:08",”2”,”17/09/2013”,”15:33”,”1413435760938”
		name[id] = "JABRA EASYGO_BB";
		power[id] = "-55";
		macaddr[id] = "D7:F5:BC:89:CD:08";
		type[id] = "2";
		date[id] = "17/09/2013";
		time[id] = "15:33";
		timestamp[id] = "1413272078881";

		id = 18; // 8,”Nokia
					// old”,”-81”,"BC:F5:AC:89:CD:08",”2”,”04/06/1999”,”17:17”,”1413435384038”
		name[id] = "Nokia old_BB";
		power[id] = "-71";
		macaddr[id] = "E8:F5:BC:89:CD:08";
		type[id] = "2";
		date[id] = "04/06/1999";
		time[id] = "17:17";
		timestamp[id] = "1413272078881";

		id = 19; // 9,”Sony
					// Ericsson”,”-77”,"BC:F5:AC:89:CD:08",”1”,”04/04/2004”,”09:09”,”1413439772484”
		name[id] = "Sony Ericsson_BB";
		power[id] = "-37";
		macaddr[id] = "F9:F5:BC:89:CD:08";
		type[id] = "2";
		date[id] = "04/04/2004";
		time[id] = "09:09";
		timestamp[id] = "1413272078881";

		id = 20; // 10,”LG Galaxi
					// 900”,”-63”,"BC:F5:AC:89:CD:08",”1”,”21:04/2007”,”11:11”,”1413439712840”
		name[id] = "LG Galaxi 900_BB";
		power[id] = "-53";
		macaddr[id] = "A1:F5:BC:89:CD:08";
		type[id] = "1";
		date[id] = "21/04/2007";
		time[id] = "11:11";
		timestamp[id] = "1413272078881";

	}

	public void initDevicesC() {
		Log.i(TAG, "initDevices start");

		// device [0] [8] =
		// {"no","name","power","macaddr","type","date","time","timestamp"};

		id = 21; // 1,”Desire
				// HD”,"-42”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”11:40”,”1413272078881”
		name[id] = "Desire HD CC";
		power[id] = "-62";
		macaddr[id] = "BC:C5:AC:89:CD:08";
		type[id] = "0";
		date[id] = "04/11/2014";
		time[id] = "11:40";
		timestamp[id] = "1413272078881";

		id = 22; // 2,"YP-G70","-40”,"BC:F5:AC:89:CD:08",”1”,”03/11/2014”,”08:09”,”1413272000843”
		name[id] = "YP-G70 CC";
		power[id] = "-70";
		macaddr[id] = "DE:C5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "03/11/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 23; // 3,"Samsung LED46","-79”,"BC:F5:AC:89:CD:08",”1”,”26/10/2014”,”16:11”,"1413186240280"
		name[id] = "Samsung LED46 CC";
		power[id] = "-39";
		macaddr[id] = "FA:C5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "26/10/2014";
		time[id] = "16:11";
		timestamp[id] = "1413272078881";

		id = 24; // 4,"DTVBluetooth","-55”,"BC:F5:AC:89:CD:08",”1”,”18/10/2014”,”13:09”,"1413186238581”
		name[id] = "DTVBluetooth CC";
		power[id] = "-72";
		macaddr[id] = "A4:C5:AC:89:CD:08";
		type[id] = "3";
		date[id] = "18/10/2014";
		time[id] = "13:09";
		timestamp[id] = "1413272078881";

		id = 25; // 5,”Samsung
				// Mini,”_64”,”90:21:55:AB:AD:BF”,”1”,”09/09/2014”,”08:09”,”1415084976158”
		name[id] = "Samsung Mini CC";
		power[id] = "-34";
		macaddr[id] = "B5:C5:AC:89:CD:08";
		type[id] = "0";
		date[id] = "09/09/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 26; // 6,”Nexus
				// 5”,”-33”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”08:17”,”1234567890465”
		name[id] = "Nexus 5 CC";
		power[id] = "-83";
		macaddr[id] = "C6:C5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "04/11/2014";
		time[id] = "08:17";
		timestamp[id] = "1413272078881";

		id = 27; // 7,”JABRA
				// EASYGO”,”-75”,"BC:F5:AC:89:CD:08",”2”,”17/09/2013”,”15:33”,”1413435760938”
		name[id] = "JABRA EASYGO CC";
		power[id] = "-45";
		macaddr[id] = "D7:C5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "17/09/2013";
		time[id] = "15:33";
		timestamp[id] = "1413272078881";

		id = 28; // 8,”Nokia
				// old”,”-81”,"BC:F5:AC:89:CD:08",”2”,”04/06/1999”,”17:17”,”1413435384038”
		name[id] = "Nokia old CC";
		power[id] = "-61";
		macaddr[id] = "E8:C5:AC:89:CD:08";
		type[id] = "3";
		date[id] = "04/06/1999";
		time[id] = "17:17";
		timestamp[id] = "1413272078881";

		id = 29; // 9,”Sony
				// Ericsson”,”-77”,"BC:F5:AC:89:CD:08",”1”,”04/04/2004”,”09:09”,”1413439772484”
		name[id] = "Sony Ericsson CC";
		power[id] = "-57";
		macaddr[id] = "F9:C5:AC:89:CD:08";
		type[id] = "0";
		date[id] = "04/04/2004";
		time[id] = "09:09";
		timestamp[id] = "1413272078881";

		id = 30; // 10,”LG Galaxi
					// 900”,”-63”,"BC:F5:AC:89:CD:08",”1”,”21:04/2007”,”11:11”,”1413439712840”
		name[id] = "LG Galaxi 900 CC";
		power[id] = "-73";
		macaddr[id] = "A1:C5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "21/04/2007";
		time[id] = "11:11";
		timestamp[id] = "1413272078881";

	}

	public void initDevicesD() {
		Log.i(TAG, "initDevices start");

		// device [0] [8] =
		// {"no","name","power","macaddr","type","date","time","timestamp"};

		id = 31; // 1,”Desire
				// HD”,"-42”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”11:40”,”1413272078881”
		name[id] = "Desire HD-dd";
		power[id] = "-42";
		macaddr[id] = "BC:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "04/11/2014";
		time[id] = "11:40";
		timestamp[id] = "1413272078881";

		id = 32; // 2,"YP-G70","-40”,"BC:F5:AC:89:CD:08",”1”,”03/11/2014”,”08:09”,”1413272000843”
		name[id] = "YP-G70-dd";
		power[id] = "-40";
		macaddr[id] = "DE:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "03/11/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 33; // 3,"Samsung LED46","-79”,"BC:F5:AC:89:CD:08",”1”,”26/10/2014”,”16:11”,"1413186240280"
		name[id] = "Samsung LED46-dd";
		power[id] = "-79";
		macaddr[id] = "FA:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "26/10/2014";
		time[id] = "16:11";
		timestamp[id] = "1413272078881";

		id = 34; // 4,"DTVBluetooth","-55”,"BC:F5:AC:89:CD:08",”1”,”18/10/2014”,”13:09”,"1413186238581”
		name[id] = "DTVBluetooth-dd";
		power[id] = "-42";
		macaddr[id] = "A4:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "18/10/2014";
		time[id] = "13:09";
		timestamp[id] = "1413272078881";

		id = 35; // 5,”Samsung
				// Mini,”_64”,”90:21:55:AB:AD:BF”,”1”,”09/09/2014”,”08:09”,”1415084976158”
		name[id] = "Samsung-dd Mini";
		power[id] = "-64";
		macaddr[id] = "B5:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "09/09/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 36; // 6,”Nexus
				// 5”,”-33”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”08:17”,”1234567890465”
		name[id] = "Nexus-dd 5";
		power[id] = "-33";
		macaddr[id] = "C6:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "04/11/2014";
		time[id] = "08:17";
		timestamp[id] = "1413272078881";

		id = 37; // 7,”JABRA
				// EASYGO”,”-75”,"BC:F5:AC:89:CD:08",”2”,”17/09/2013”,”15:33”,”1413435760938”
		name[id] = "JABRA-dd EASYGO";
		power[id] = "-75";
		macaddr[id] = "D7:F5:AC:D9:CD:08";
		type[id] = "2";
		date[id] = "17/09/2013";
		time[id] = "15:33";
		timestamp[id] = "1413272078881";

		id = 38; // 8,”Nokia
				// old”,”-81”,"BC:F5:AC:89:CD:08",”2”,”04/06/1999”,”17:17”,”1413435384038”
		name[id] = "Nokia-dd old";
		power[id] = "-81";
		macaddr[id] = "E8:F5:AC:D9:CD:08";
		type[id] = "2";
		date[id] = "04/06/1999";
		time[id] = "17:17";
		timestamp[id] = "1413272078881";

		id = 39; // 9,”Sony
				// Ericsson”,”-77”,"BC:F5:AC:89:CD:08",”1”,”04/04/2004”,”09:09”,”1413439772484”
		name[id] = "Sony-dd Ericsson";
		power[id] = "-77";
		macaddr[id] = "F9:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "04/04/2004";
		time[id] = "09:09";
		timestamp[id] = "1413272078881";

		id = 40; // 10,”LG Galaxi
					// 900”,”-63”,"BC:F5:AC:89:CD:08",”1”,”21:04/2007”,”11:11”,”1413439712840”
		name[id] = "LG-dd Galaxi 900";
		power[id] = "-63";
		macaddr[id] = "A1:F5:AC:D9:CD:08";
		type[id] = "1";
		date[id] = "21/04/2007";
		time[id] = "11:11";
		timestamp[id] = "1413272078881";

	}

	public void initDevicesE() {
		Log.i(TAG, "initDevices start");

		// device [0] [8] =
		// {"no","name","power","macaddr","type","date","time","timestamp"};

		id = 1; // 1,”Desire
				// HD”,"-42”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”11:40”,”1413272078881”
		name[id] = "Desire HD";
		power[id] = "-42";
		macaddr[id] = "BC:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "04/11/2014";
		time[id] = "11:40";
		timestamp[id] = "1413272078881";

		id = 2; // 2,"YP-G70","-40”,"BC:F5:AC:89:CD:08",”1”,”03/11/2014”,”08:09”,”1413272000843”
		name[id] = "YP-G70";
		power[id] = "-40";
		macaddr[id] = "DE:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "03/11/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 3; // 3,"Samsung LED46","-79”,"BC:F5:AC:89:CD:08",”1”,”26/10/2014”,”16:11”,"1413186240280"
		name[id] = "Samsung LED46";
		power[id] = "-79";
		macaddr[id] = "FA:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "26/10/2014";
		time[id] = "16:11";
		timestamp[id] = "1413272078881";

		id = 4; // 4,"DTVBluetooth","-55”,"BC:F5:AC:89:CD:08",”1”,”18/10/2014”,”13:09”,"1413186238581”
		name[id] = "DTVBluetooth";
		power[id] = "-42";
		macaddr[id] = "A4:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "18/10/2014";
		time[id] = "13:09";
		timestamp[id] = "1413272078881";

		id = 5; // 5,”Samsung
				// Mini,”_64”,”90:21:55:AB:AD:BF”,”1”,”09/09/2014”,”08:09”,”1415084976158”
		name[id] = "Samsung Mini";
		power[id] = "-64";
		macaddr[id] = "B5:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "09/09/2014";
		time[id] = "08:09";
		timestamp[id] = "1413272078881";

		id = 6; // 6,”Nexus
				// 5”,”-33”,"BC:F5:AC:89:CD:08",”1”,”04/11/2014”,”08:17”,”1234567890465”
		name[id] = "Nexus 5";
		power[id] = "-33";
		macaddr[id] = "C6:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "04/11/2014";
		time[id] = "08:17";
		timestamp[id] = "1413272078881";

		id = 7; // 7,”JABRA
				// EASYGO”,”-75”,"BC:F5:AC:89:CD:08",”2”,”17/09/2013”,”15:33”,”1413435760938”
		name[id] = "JABRA EASYGO";
		power[id] = "-75";
		macaddr[id] = "D7:F5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "17/09/2013";
		time[id] = "15:33";
		timestamp[id] = "1413272078881";

		id = 8; // 8,”Nokia
				// old”,”-81”,"BC:F5:AC:89:CD:08",”2”,”04/06/1999”,”17:17”,”1413435384038”
		name[id] = "Nokia old";
		power[id] = "-81";
		macaddr[id] = "E8:F5:AC:89:CD:08";
		type[id] = "2";
		date[id] = "04/06/1999";
		time[id] = "17:17";
		timestamp[id] = "1413272078881";

		id = 9; // 9,”Sony
				// Ericsson”,”-77”,"BC:F5:AC:89:CD:08",”1”,”04/04/2004”,”09:09”,”1413439772484”
		name[id] = "Sony Ericsson";
		power[id] = "-77";
		macaddr[id] = "F9:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "04/04/2004";
		time[id] = "09:09";
		timestamp[id] = "1413272078881";

		id = 10; // 10,”LG Galaxi
					// 900”,”-63”,"BC:F5:AC:89:CD:08",”1”,”21:04/2007”,”11:11”,”1413439712840”
		name[id] = "LG Galaxi 900";
		power[id] = "-63";
		macaddr[id] = "A1:F5:AC:89:CD:08";
		type[id] = "1";
		date[id] = "21/04/2007";
		time[id] = "11:11";
		timestamp[id] = "1413272078881";

	}

}
