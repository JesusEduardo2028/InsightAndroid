package com.unicauca.jesusmunoz.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.emotiv.insight.IEdk;
import com.unicauca.jesusmunoz.main.R;
import com.unicauca.jesusmunoz.services.EmotivService;
import com.unicauca.jesusmunoz.util.InsightDevice;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jesuseduardomunoz on 9/3/15.
 */
public class InsightDevicesAdapter extends ArrayAdapter<InsightDevice>{

    Context context;
    int layoutResourceId;
    AdapterConnectInterface buttonListener;
    ArrayList<InsightDevice> devices;

    public interface AdapterConnectInterface {
        public void buttonPressed(int deviceID);
    }

    public InsightDevicesAdapter(Context context, int resource, ArrayList<InsightDevice> devices,  AdapterConnectInterface buttonListener) {
        super(context, resource, devices);
        this.context = context;
        this.layoutResourceId = resource;
        this.devices = devices;
        this.buttonListener = buttonListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeviceHolder holder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
            holder = new DeviceHolder();
            holder.tv_device_id = (TextView) row.findViewById(R.id.tv_device_id);
            holder.tv_device_name = (TextView) row.findViewById(R.id.tv_device_name);
            holder.bt_device_connect = (Button) row.findViewById(R.id.bt_device_connect);
            row.setTag(holder);
        }else{
            holder = (DeviceHolder) row.getTag();
        }
        final InsightDevice device = devices.get(position);
        holder.tv_device_name.setText(device.getName());
        holder.tv_device_id.setText(device.getID() + "");
        holder.bt_device_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.buttonPressed(device.getID());
            }
        });
        return row;
    }

    static class DeviceHolder
    {
        TextView tv_device_id;
        TextView tv_device_name;
        Button bt_device_connect;
    }

}
