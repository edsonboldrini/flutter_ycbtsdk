package com.example.ycblesdkdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ycblesdkdemo.R;
import com.yucheng.ycbtsdk.bean.ScanDeviceBean;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private Context context;

    private List<ScanDeviceBean> listVals;

    public DeviceAdapter(Context context, List<ScanDeviceBean> listVal) {
        this.context = context;
        this.listVals = listVal;
    }

    public void addModel(ScanDeviceBean scanDeviceBean){
        listVals.add(scanDeviceBean);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listVals.size();
    }

    @Override
    public Object getItem(int i) {
        return listVals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;

        if (view == null) {
            vh = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.device_item_layout, null);
//            vh.headImg = (ImageView) view.findViewById(R.id.check_box_select_img_view);
            vh.nameView = (TextView) view.findViewById(R.id.item_name_view);
            vh.macView = (TextView) view.findViewById(R.id.item_mac_view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        final ScanDeviceBean checkBoxModel = listVals.get(i);

        vh.nameView.setText(checkBoxModel.getDeviceName());
        vh.macView.setText(checkBoxModel.getDeviceMac());

        return view;
    }

    public final class ViewHolder {
        //        public ImageView headImg;
        public TextView nameView;
        public TextView macView;
    }

//    public interface FriendStateListener {
//        void aggreeClick(FriendListModel friendListModel);
//
//        void refuseClick(FriendListModel friendListModel);
//    }


}
