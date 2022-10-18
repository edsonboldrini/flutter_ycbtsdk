package com.example.ycblesdkdemo.ecg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ycblesdkdemo.R;

import java.util.ArrayList;
import java.util.List;

public class EcgHisListAdapter extends BaseAdapter implements OnItemClickListener, View.OnClickListener {
    private List<String> list;
    private LayoutInflater mInflater;
    private Context context;
    OnCall onCall;

    public EcgHisListAdapter(Context context, List<String> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList();
        }
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setDataChanged(List<String> list){
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_ecg_his_list, parent, false);//item布局
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.tv_jilu = convertView.findViewById(R.id.tv_jilu);
            holder.tv_jiance = convertView.findViewById(R.id.tv_jiance);
            holder.btn_sycn = convertView.findViewById(R.id.btn_sycn);
            holder.lay_sycn = convertView.findViewById(R.id.lay_sycn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position >= list.size()) {
            return null;
        }
        holder.title.setText(list.get(position));
        if (list.get(position).length() > 16) {
            holder.lay_sycn.setVisibility(View.GONE);
            holder.tv_jilu.setVisibility(View.VISIBLE);
            holder.tv_jiance.setVisibility(View.VISIBLE);
        } else {
            holder.lay_sycn.setVisibility(View.VISIBLE);
            holder.tv_jilu.setVisibility(View.GONE);
            holder.tv_jiance.setVisibility(View.GONE);
        }

        holder.tv_jilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCall.setInfo(v, position);
            }
        });
        holder.tv_jiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCall.setInfo(v, position);
            }
        });

        holder.btn_sycn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCall.setInfo(view, position);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView title;
        LinearLayout tv_jilu;
        LinearLayout tv_jiance;
        LinearLayout lay_sycn;
        TextView btn_sycn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }

    public void setOnCall(OnCall onCall) {
        this.onCall = onCall;
    }

    public interface OnCall {
        void setInfo(View v, int position);
    }
}