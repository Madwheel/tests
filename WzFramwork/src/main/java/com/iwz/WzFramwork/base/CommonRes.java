package com.iwz.WzFramwork.base;


/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/213:23
 * desc   :
 */
public class CommonRes<T> extends MyObject {
    protected T mResObj;
    protected String mResult;
    protected int mErrorCode;
    protected boolean mFinBool;

    public CommonRes(boolean finBool) {
        mFinBool = finBool;
        mErrorCode = 0;
        mResObj = null;
        mResult = "";
    }

    public CommonRes(boolean finBool, int errorCode) {
        mFinBool = finBool;
        mErrorCode = errorCode;
        mResObj = null;
        mResult = "";
    }

    public CommonRes(boolean finBool, int errorCode, T resObj) {
        mFinBool = finBool;
        mErrorCode = errorCode;
        mResObj = resObj;
        mResult = "";
    }

    public CommonRes(boolean finBool, String response) {
        mFinBool = finBool;
        mErrorCode = 0;
        mResObj = null;
        mResult = response;
    }

    public CommonRes(boolean finBool, int errorCode, String response) {
        mFinBool = finBool;
        mErrorCode = errorCode;
        mResObj = null;
        mResult = response;
    }

    public CommonRes(boolean finBool, int errorCode, T resObj, String response) {
        mFinBool = finBool;
        mErrorCode = errorCode;
        mResObj = resObj;
        mResult = response;
    }

    public T getResObj() {
        return mResObj;
    }

    public void setResObj(T mResObj) {
        this.mResObj = mResObj;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String mResult) {
        this.mResult = mResult;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public boolean isFinBool() {
        return mFinBool;
    }

    public void setFinBool(boolean mFinBool) {
        this.mFinBool = mFinBool;
    }

    public boolean isOk() {
        return mFinBool && mErrorCode == 0;
    }
}
