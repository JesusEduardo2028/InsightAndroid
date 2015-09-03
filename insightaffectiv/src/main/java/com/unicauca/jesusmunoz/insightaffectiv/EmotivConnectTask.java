package com.unicauca.jesusmunoz.insightaffectiv;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.emotiv.insight.IEdk;
import com.unicauca.jesusmunoz.services.EmotivService;

/**
 * Created by jesuseduardomunoz on 8/29/15.
 */

public class EmotivConnectTask extends AsyncTask<String, Void, Boolean> {

    public static int  INSIGHT_NOTIFICATION_ID = 100;

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
            showNotification();
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
                            IEdk.IEE_ConnectDevice(number);
                            IEdk.IEE_MotionDataCreate();
                            Log.e("NUMBER", number+"");
                            break;
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
    public void showNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Emotiv Device connected")
                        .setContentText("The system is listening");
        mBuilder.setOngoing(true);
        // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(activity, activity.getClass());


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(activity.getClass());
        // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
                mNotificationManager.notify(INSIGHT_NOTIFICATION_ID, mBuilder.build());
    }

}
