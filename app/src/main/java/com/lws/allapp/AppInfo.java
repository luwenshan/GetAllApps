package com.lws.allapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by lws on 2017/11/14.
 */

public class AppInfo {
    private String mAppLabel;
    private Drawable mAppIcon;
    private Intent mIntent;
    private String mPkgName;

    public String getAppLabel() {
        return mAppLabel;
    }

    public void setAppLabel(String appLabel) {
        this.mAppLabel = appLabel;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.mAppIcon = appIcon;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public String getPkgName() {
        return mPkgName;
    }

    public void setPkgName(String pkgName) {
        this.mPkgName = pkgName;
    }
}
