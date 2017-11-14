package com.lws.allapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppListActivity extends AppCompatActivity {

    @BindView(R.id.app_list_view)
    ListView mAppListView;
    private List<AppInfo> mAppInfos;
    private AppInfoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_app_list);
        ButterKnife.bind(this);
        mAppInfos = new ArrayList<>();
        int flag = getIntent().getIntExtra("flag", 0);
        if (flag == 0) {
            queryAppInfo();
        } else {
            queryAndFilterAppInfo(flag);
        }

        mAdapter = new AppInfoListAdapter(this, mAppInfos);
        mAppListView.setAdapter(mAdapter);
        mAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = mAppInfos.get(position).getIntent();
                if (intent != null) {
                    startActivity(intent);
                } else {
                    startAppWithPkgName(mAppInfos.get(position).getPkgName());
                }
            }
        });
    }

    private void queryAndFilterAppInfo(int flag) {
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        mAppInfos.clear();
        switch (flag) {
            case MainActivity.FLAG_ALL_APP:
                for (PackageInfo installedPackage : installedPackages) {
                    mAppInfos.add(getAppInfo(installedPackage, pm));
                }
                break;
            case MainActivity.FLAG_SYSTEM_APP:
                for (PackageInfo installedPackage : installedPackages) {
                    if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        mAppInfos.add(getAppInfo(installedPackage, pm));
                    }
                }
                break;
            case MainActivity.FLAG_THIRD_APP:
                for (PackageInfo installedPackage : installedPackages) {
                    //非系统程序
                    if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        mAppInfos.add(getAppInfo(installedPackage, pm));
                    }
                    //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                    else if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        mAppInfos.add(getAppInfo(installedPackage, pm));
                    }
                }
                break;
            case MainActivity.FLAG_SDCARD_APP:
                for (PackageInfo installedPackage : installedPackages) {
                    if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                        mAppInfos.add(getAppInfo(installedPackage, pm));
                    }
                }
                break;
        }
    }

    private AppInfo getAppInfo(PackageInfo packageInfo, PackageManager pm) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppLabel((String) packageInfo.applicationInfo.loadLabel(pm));
        appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
        appInfo.setPkgName(packageInfo.packageName);
        return appInfo;
    }

    // 生成Launcher列表
    private void queryAppInfo() {
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        mAppInfos.clear();
        for (ResolveInfo resolveInfo : resolveInfos) {
            AppInfo appInfo = new AppInfo();
            appInfo.setAppLabel((String) resolveInfo.loadLabel(pm));
            appInfo.setPkgName(resolveInfo.activityInfo.packageName);
            appInfo.setAppIcon(resolveInfo.loadIcon(pm));
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
            appInfo.setIntent(intent);
            mAppInfos.add(appInfo);
        }

    }

    //通过package name启动应用
    public void startAppWithPkgName(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);

        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(resolveIntent, 0);

        if (resolveInfos.size() > 0) {
            ResolveInfo resolveInfo = resolveInfos.get(0);
            String pkgName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName componentName = new ComponentName(pkgName, className);
            intent.setComponent(componentName);
            startActivity(intent);
        }
    }
}
