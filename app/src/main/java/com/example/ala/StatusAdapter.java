package com.example.ala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ala.Inventory.Status;

import java.util.List;

public class StatusAdapter extends BaseAdapter {
    private Context context;
    private List<Status> statusList;

    public StatusAdapter(Context context, List<Status> statusList)
    {
        this.context = context;
        this.statusList = statusList;
    }

    @Override
    public int getCount() {
        return statusList != null ? statusList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_status, parent, false);

        TextView txtNameStatus = view.findViewById(R.id.txt_status_name);
        ImageView imageStatus = view.findViewById(R.id.status_img);

        txtNameStatus.setText(statusList.get(position).getName());
        imageStatus.setImageResource(statusList.get(position).getStatus_bar());

        return view;
    }
}
