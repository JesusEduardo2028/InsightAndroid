package com.unicauca.jesusmunoz.insightaffectiv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.emotiv.insight.IEdk;
import com.unicauca.jesusmunoz.services.EmotivService;

/**
 * Created by jesuseduardomunoz on 8/29/15.
 */

public class EmotivConnectTask extends AsyncTask<String, Void, Boolean> {

    public EmotivConnectTask(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
    }

    /**
     * progress dialog to show user that the backup is processing.
     */
    private ProgressDialog dialog;
    /**
     * application context.
     */
    private Activity activity;

    private Boolean insightDeviceConnected;

    protected void onPreExecute() {
        IEdk.IEE_EngineConnect(activity);
        insightDeviceConnected = false;
        this.dialog.setMessage("Looking for insight devices...");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (success) {
            activity.startService(new Intent(activity, EmotivService.class));
            Toast.makeText(activity, "Device Connected!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "No Insight device was found", Toast.LENGTH_LONG).show();
        }
    }

    protected Boolean doInBackground(final String... args) {

        //Wait 10 seconds for insight devices

        long endTime = System.currentTimeMillis() + 5 * 1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    int number = IEdk.IEE_GetNumberDeviceInsight();
                    if (number != 0) {
                        if (!insightDeviceConnected) {
                            insightDeviceConnected = true;
                            IEdk.IEE_ConnectDevice(0);
                        }
                    }
                } catch (Exception e) {
                    Log.e("tag", "error", e);
                }
            }
        }

        if (insightDeviceConnected){
            return true;
        }else {
            return false;
        }
    }

}
