package com.unicauca.jesusmunoz.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.emotiv.insight.IEdk;
import com.unicauca.jesusmunoz.adapters.InsightDevicesAdapter;
import com.unicauca.jesusmunoz.main.R;
import com.unicauca.jesusmunoz.services.EmotivService;
import com.unicauca.jesusmunoz.util.InsightDevice;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements InsightDevicesAdapter.AdapterConnectInterface {

    public static final String TAG = "SETTINGS_FRAGMENT";
    ArrayList<InsightDevice> devices;
    InsightDevicesAdapter adapter;
    ListView devices_list;
    private ProgressDialog dialog;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IEdk.IEE_EngineConnect(getActivity());
        devices = new ArrayList<>();
        searchDevices();
        adapter = new InsightDevicesAdapter(getContext(), R.layout.device_adapter_layout,devices,this);
        this.dialog = new ProgressDialog(getContext());
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.device_list_header, null);
        devices_list = (ListView) root.findViewById(R.id.lv_insight_devices);
        devices_list.addHeaderView(header);
        devices_list.setEmptyView(getActivity().findViewById(R.id.device_empty_list));

        devices_list.setAdapter(adapter);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void searchDevices(){
        int devicesNum = IEdk.IEE_GetNumberDeviceInsight();
        devices.clear();
        for(int i =0 ; i <  devicesNum; i++ ){
            devices.add(i,new InsightDevice(i,IEdk.IEE_GetNameDeviceInsightAtIndex(i)));
        }
    }

    public void redraw_listview(){
        searchDevices();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void buttonPressed(int deviceID) {
        Intent intent = new Intent(getContext(), EmotivService.class);
        intent.putExtra("deviceNum", deviceID);
        getContext().startService(intent);

        dialog.setMessage("Connecting device...");
        dialog.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
                redraw_listview();
                //Toast.makeText(getContext(), "Device connected!", Toast.LENGTH_SHORT).show();
                showNotification();
            }
        }, 1000);
    }

    public void showNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(com.unicauca.jesusmunoz.main.R.drawable.ic_launcher)
                        .setContentTitle("Emotiv Device connected")
                        .setContentText("The system is listening");
        //mBuilder.setOngoing(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), getActivity().getClass());


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(getActivity().getClass());
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(EmotivService.INSIGHT_NOTIFICATION_ID, mBuilder.build());
    }


}
