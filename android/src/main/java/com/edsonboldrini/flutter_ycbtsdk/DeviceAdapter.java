package com.edsonboldrini.flutter_ycbtsdk;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// import com.example.ycblesdkdemo.R;
import com.yucheng.ycbtsdk.bean.ScanDeviceBean;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

	private List<ScanDeviceBean> scanDevicesList;

	public DeviceAdapter(List<ScanDeviceBean> list) {
		this.scanDevicesList = list;
	}

	public void addModel(ScanDeviceBean scanDeviceBean) {
		scanDevicesList.add(scanDeviceBean);
		notifyDataSetChanged();
	}

	public List<ScanDeviceBean> getScanDevicesList() {
		return scanDevicesList;
	}

	public void setScanDevicesList(List<ScanDeviceBean> scanDevicesList) {
		this.scanDevicesList = scanDevicesList;
	}

	@Override
	public int getCount() {
		return scanDevicesList.size();
	}

	@Override
	public Object getItem(int i) {
		return scanDevicesList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		ViewHolder vh;

		// if (view == null) {
		// vh = new ViewHolder();
		// view = LayoutInflater.from(context).inflate(R.layout.device_item_layout,
		// null);
		// // vh.headImg = (ImageView)
		// view.findViewById(R.id.check_box_select_img_view);
		// vh.nameView = (TextView) view.findViewById(R.id.item_name_view);
		// vh.macView = (TextView) view.findViewById(R.id.item_mac_view);
		// view.setTag(vh);
		// } else {
		// vh = (ViewHolder) view.getTag();
		// }

		// final ScanDeviceBean checkBoxModel = listVals.get(i);

		// vh.nameView.setText(checkBoxModel.getDeviceName());
		// vh.macView.setText(checkBoxModel.getDeviceMac());

		return view;
	}

	public final class ViewHolder {
		// public ImageView headImg;
		public TextView nameView;
		public TextView macView;
	}

	// public interface FriendStateListener {
	// void aggreeClick(FriendListModel friendListModel);
	//
	// void refuseClick(FriendListModel friendListModel);
	// }

}
