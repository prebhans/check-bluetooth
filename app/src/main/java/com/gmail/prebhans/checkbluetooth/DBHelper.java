package com.gmail.prebhans.checkbluetooth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    /*
     * public static final String DATABASE_NAME = "MyDBName.db"; public static
     * final String CONTACTS_TABLE_NAME = "contacts"; public static final String
     * CONTACTS_COLUMN_ID = "id"; public static final String
     * CONTACTS_COLUMN_NAME = "name"; public static final String
     * CONTACTS_COLUMN_EMAIL = "email"; public static final String
     * CONTACTS_COLUMN_STREET = "street"; public static final String
     * CONTACTS_COLUMN_CITY = "place"; public static final String
     * CONTACTS_COLUMN_PHONE = "phone";
     */
    // All Static variables
    // Database Version
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "device_sql.db";
    private static final int DATABASE_VERSION = 1;
    //
    private static final String TABLE_NAME = "device"; // SQL name
    private static final String KEY_ID = "id"; // ID in SQL
    private static final String KEY_NUM = "num"; // number of device
    private static final String KEY_NAME = "name"; // name on scanned device
    private static final String KEY_MACADDR = "macaddr"; // MAC address
    private static final String KEY_POWER = "power"; // effect level
    private static final String KEY_COMPANY = "company"; // company name
    //
    SQLiteDatabase db;

    //
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DeviceSqlHelper start");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_DEVICES_TABLE = "create table " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_NUM
                + " TEXT," + KEY_NAME + " TEXT," + KEY_MACADDR + " TEXT,"
                + KEY_POWER + " TEXT," + KEY_COMPANY + " TEXT)";
        db.execSQL(CREATE_DEVICES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onUpgrade start");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    // Are used in scanning
    public int getDevicesCount() {
        int deviceCount;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        deviceCount = cursor.getCount();
        cursor.close();
        return deviceCount;
    }

    /*
     * Are used in scanning
     */
    public long insertDevice(String num, String name, String macaddr,
                                String power, String company) {

        Log.i(TAG, "insertDevice start");
        long rowId;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //
        values.put(KEY_NUM, num);
        values.put(KEY_NAME, name);
        values.put(KEY_MACADDR, macaddr);
        values.put(KEY_POWER, power);
        values.put(KEY_COMPANY, company);
        //
        rowId = db.insert(TABLE_NAME, null, values);

        return rowId;
    }

    public Cursor getData(int id) {
        Log.i(TAG, "getData start");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + "where id="
                + id + "", null);
        return res;
    }

    // Are used in Database
    public Cursor getAllDevices() {
        Log.i(TAG, "getAllDevices start");
        Cursor locCursor = null;
        //int countRow = 0;
        // return getReadableDatabase().query(TABLE_NAME, null, null, null,
        // null,
        // null, null);
        SQLiteDatabase db = this.getReadableDatabase();

        //String path = db.getPath();

        locCursor = db.rawQuery("select * from " + TABLE_NAME, null);

        //countRow = locCursor.getCount();

        return locCursor;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    // Are used
    public void deleteDevice(Integer id) {
        Log.i(TAG, "deleteDevices start");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    // Are used in Database
    public void deleteDeviceByMac(String macAddr) {
        Log.i(TAG, "deleteDeviceByMac start");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_MACADDR + " = ? ", new String[]{macAddr});
    }

    // Are used in Database
    public int deleteAllDevices() {
        Log.i(TAG, "deleteAllDevices start");
        SQLiteDatabase db = getReadableDatabase();
        int numberRows = db.delete(TABLE_NAME, "1", null);
        Log.i(TAG, "deleteAllDevices end" + " " + numberRows);
        return numberRows;
    }

    // Are used in Database
    public void deleteDatabase() {
        Log.i(TAG, "deleteAllDevices start");
        SQLiteDatabase db = getReadableDatabase();
        db.close();
        }

    public boolean isDatabaseEmpty() {
        Log.i(TAG, "isDatabaseEmpty start");
        return !getReadableDatabase().query(TABLE_NAME, null, null, null, null,
                null, null).moveToFirst();
    }

    // are used in scanning
    public boolean isDeviceInDb(String paramString) {
        Log.i(TAG, "isDeviceInDb start");
        boolean result;
        int deviceNumber;
        SQLiteDatabase db = getReadableDatabase();
        Cursor localCursor = db.query(TABLE_NAME, new String[]{KEY_ID},
                KEY_MACADDR + " = ?", new String[]{paramString}, null, null,
                null);
        deviceNumber = localCursor.getCount();
        localCursor.close();
        if (deviceNumber == 1) {
            result = true;
        } else if (deviceNumber > 1) {
            Log.i(TAG, "isDeviceInDb Should only be one MAC in DB " + deviceNumber);
            result = true;
        } else
            result = false;
        return result;
    }

    public String getDevicePower(String paramString) {
        Log.i(TAG, "getDevicePower start");
        int deviceCount;
        String devicePower;
        SQLiteDatabase db = getReadableDatabase();
        Cursor localCursor = db.query(TABLE_NAME, new String[]{KEY_POWER},
                KEY_MACADDR + " = ?", new String[]{paramString}, null, null,
                null);
        deviceCount = localCursor.getCount();

        if (deviceCount == 1) {
            localCursor.moveToFirst();
            devicePower = localCursor.getString(0);
            localCursor.close();
            return devicePower;
        }
        if (deviceCount > 1) {
            Log.i(TAG, "getDevicePower Should only be one MAC in DB " + localCursor.getCount());
            return "";
        }
        return "";
    }

    public String getCompanyName(String paramString) {
        Log.i(TAG, "getCompanyName start");
        int companyCount;
        String companyName;
        SQLiteDatabase db = getReadableDatabase();
        Cursor localCursor = db.query(TABLE_NAME, new String[]{KEY_COMPANY},
                KEY_MACADDR + " = ?", new String[]{paramString}, null, null,
                null);
        companyCount = localCursor.getCount();
        if (companyCount == 1) {
            localCursor.moveToFirst();
            companyName = localCursor.getString(0);
            localCursor.close();
            return companyName;
        }
        if (companyCount > 1) {
            Log.i(TAG, "getCompanyName Should only be one MAC in DB " + localCursor.getCount());
            return "";
        }
        return "";
    }

    // Are used
    public int getDeviceId(String paramString) {
        Log.i(TAG, "getDeviceId start");
        int deviceCount;
        int deviceId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor localCursor = db.query(TABLE_NAME, new String[]{KEY_ID},
                KEY_MACADDR + " = ? ", new String[]{paramString}, null,
                null, null);
        deviceCount = localCursor.getCount();
        if (deviceCount == 1) {
            localCursor.moveToFirst();
            deviceId = Integer.parseInt(localCursor.getString(0));
            localCursor.close();
            return deviceId;
        }
        if (deviceCount > 1) {
            Log.i(TAG, "getDeviceId Should only be one MAC in DB " + localCursor.getCount());
            return -1;
        }
        return -1;
    }

    public String getDeviceMac(int paramInt) {
        Log.i(TAG, "getDeviceMac start");
        int deviceCount;
        String deviceMac;
        SQLiteDatabase db = getReadableDatabase();
        String[] arrayOfString1 = {KEY_MACADDR};
        String[] arrayOfString2 = new String[1];
        arrayOfString2[0] = Integer.toString(paramInt);
        Cursor localCursor = db.query(TABLE_NAME, arrayOfString1, KEY_ID
                + " = ? ", arrayOfString2, null, null, null);
        String str;

        deviceCount = localCursor.getCount();
        if (deviceCount == 1) {
            localCursor.moveToFirst();
            deviceMac = localCursor.getString(0);
            localCursor.close();

        } else {

            deviceMac = "MAC conflict" + " " + String.valueOf(deviceCount);

            Log.i(TAG, "getDeviceMac Should only be one MAC in DB" + localCursor.getCount());
        }
        return deviceMac;
    }

    public int updateDevice(int id, String num, String name, String macaddr,
                                String power, String company) {

        Log.i(TAG, "updateDevice start");
        int numberRows;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //
        //values.put(KEY_NUM, num);
        //values.put(KEY_NAME, name);
        //values.put(KEY_MACADDR, macaddr);
        //values.put(KEY_POWER, power);
        values.put(KEY_COMPANY, company);
        //
        //db.update(TABLE_NAME, values, KEY_ID + " = ? ",
        //        new String[]{Integer.toString(id)});

        numberRows = db.update(TABLE_NAME, values, KEY_ID + "=" + id, null);


        return numberRows;
    }

}