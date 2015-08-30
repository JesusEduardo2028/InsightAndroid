package com.unicauca.jesusmunoz.services;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEdkErrorCode;

import java.io.BufferedWriter;

/**
 * Created by jesuseduardomunoz on 8/29/15.
 */
public class EmotivService extends IntentService {

    private boolean insightDeviceConnected;


    public EmotivService() {
        super("Emotivservice");
        insightDeviceConnected = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        IEdk.IEE_EngineConnect(EmotivService.this);

        // Wait 20 seconds max for detecting an insight device
        long endTime = System.currentTimeMillis() + 20*1000;
        while (System.currentTimeMillis() < endTime || insightDeviceConnected) {
            synchronized (this) {
                int number = IEdk.IEE_GetNumberDeviceInsight();
                if (number != 0) {
                    if (!insightDeviceConnected) {
                        insightDeviceConnected = true;
                        IEdk.IEE_ConnectDevice(0);
                        Toast.makeText(EmotivService.this, "Device connected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
