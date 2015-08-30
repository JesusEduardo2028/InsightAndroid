package com.unicauca.jesusmunoz.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEdkErrorCode;
import com.emotiv.insight.IEmoStateDLL;

/**
 * Created by jesuseduardomunoz on 8/29/15.
 */
public class EmotivService extends IntentService {

    private boolean insightDeviceConnected;
    public static final String REQUEST_STRING = "myRequest";
    public static final String RESPONSE_STRING = "myResponse";
    public static final String RESPONSE_MESSAGE = "myResponseMessage";


    public EmotivService() {
        super("Emotivservice");
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Server", ">>>onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Toast.makeText(this, "Start Insight listening!!!", Toast.LENGTH_LONG).show();
        insightDeviceConnected = true;


        while (insightDeviceConnected) {
            try {

                int stateConnect = IEdk.IEE_EngineGetNextEvent();

                if (stateConnect == IEdkErrorCode.EDK_OK.ToInt()) {
                    int eventType = IEdk.IEE_EmoEngineEventGetType();

                  /*  Log.d("code", stateConnect + "");

                    if (eventType == IEdk.IEE_Event_t.IEE_UserAdded.ToInt()) {
                        engineConnector = true;

                        Message message = dialogHandler.obtainMessage();
                        Bundle bundle  = new Bundle();
                        bundle.putString("message","Connect insight successful.");
                        message.setData(bundle);
                        dialogHandler.sendMessage(message);

                    } else if (eventType == IEdk.IEE_Event_t.IEE_UserRemoved.ToInt()) {
                        engineConnector = false;

                        Message message = dialogHandler.obtainMessage();
                        Bundle bundle  = new Bundle();
                        bundle.putString("message","Insight disconnected.");
                        message.setData(bundle);
                        dialogHandler.sendMessage(message);

                    }*/

                    if (eventType == IEdk.IEE_Event_t.IEE_UserRemoved.ToInt()) {
                        Log.d("DISCONNECT",  "********************************************");
                        //IEdk.IEE_EngineDisconnect();
                        break;
                    }


                    if (eventType == IEdk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()) {
                        Log.d("INFOOOO", "INFO--------------------------------**^^^^^^^");

                        IEdk.IEE_EmoEngineEventGetEmoState();
                        Log.d("New_EMO_STATE", IEmoStateDLL.IS_PerformanceMetricGetEngagementBoredomScore() + "");


                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("EMO_STATE_FILTER");
                        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        broadcastIntent.putExtra("engagementBoredomScore", IEmoStateDLL.IS_PerformanceMetricGetEngagementBoredomScore());
                        broadcastIntent.putExtra("excitementLongTermScore", IEmoStateDLL.IS_PerformanceMetricGetExcitementLongTermScore());
                        broadcastIntent.putExtra("instantaneousExcitementScore", IEmoStateDLL.IS_PerformanceMetricGetInstantaneousExcitementScore());
                        broadcastIntent.putExtra("relaxationScore", IEmoStateDLL.IS_PerformanceMetricGetRelaxationScore());
                        sendBroadcast(broadcastIntent);

                       /* Data[0] = IEmoStateDLL.IS_PerformanceMetricGetEngagementBoredomScore();
                        Data[1] = IEmoStateDLL.IS_PerformanceMetricGetExcitementLongTermScore();
                        Data[2] = IEmoStateDLL.IS_PerformanceMetricGetInstantaneousExcitementScore();
                        Data[3] = IEmoStateDLL.IS_PerformanceMetricGetRelaxationScore();
                        handler.sendMessage(handler.obtainMessage(0, Data));*/
                    }else{
                        Log.d("NO DATA", "NO DATA");
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("ERROR", "ERROR******************************");
            }

        }
    }

}
