package com.lws.allapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lws on 2017/11/14.
 */

public class AppInfoListAdapter extends ArrayAdapter<AppInfo> {

    private List<AppInfo> mAppInfos;

    public AppInfoListAdapter(Context context, List<AppInfo> objects) {
        super(context, R.layout.browse_app_list_item, objects);
        this.mAppInfos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.browse_app_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppInfo appInfo = mAppInfos.get(position);
        holder.mIvAppIcon.setImageDrawable(appInfo.getAppIcon());
        holder.mTvAppLabel.setText(appInfo.getAppLabel());
        holder.mTvPkgName.setText(appInfo.getPkgName());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_app_icon)
        ImageView mIvAppIcon;
        @BindView(R.id.tv_app_label)
        TextView mTvAppLabel;
        @BindView(R.id.tv_pkg_name)
        TextView mTvPkgName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
