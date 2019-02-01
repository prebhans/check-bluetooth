package com.gmail.prebhans.checkbluetooth;

public class ScanDevice {

    private String mNum = "0";
    private String mName = "No Name";
    private String mMacaddr = "00:00:00:00:00:00";
    private String mPower = "-99"; // temp to later coding
    private String mCompany = "No Company Name";
    private boolean firstCheckOk;
    private boolean secondCheckOk;

    public ScanDevice(String num, String name, String macaddr, String power, String company){

        this.mNum = num;
        this.mName = name;
        this.mMacaddr = macaddr;
        this.mPower = power;
        this.mCompany = company;

    }


    public String getmNum() {
        return mNum;
    }

    public void setmNum(String mNum) {
        this.mNum = mNum;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmMacaddr() {
        return mMacaddr;
    }

    public void setmMacaddr(String mMacaddr) {
        this.mMacaddr = mMacaddr;
    }

    public String getmPower() {
        return mPower;
    }

    public void setmPower(String mPower) {
        this.mPower = mPower;
    }

    public String getmCompany() {
        return mCompany;
    }

    public void setmCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public boolean isFirstCheckOk() {
        return firstCheckOk;
    }

    public void setFirstCheckOk(boolean firstCheckOk) {
        this.firstCheckOk = firstCheckOk;
    }

    public boolean isSecondCheckOk() {
        return secondCheckOk;
    }

    public void setSecondCheckOk(boolean secondCheckOk) {
        this.secondCheckOk = secondCheckOk;
    }
}
