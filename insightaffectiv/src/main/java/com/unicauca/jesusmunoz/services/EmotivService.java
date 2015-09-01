package com.unicauca.jesusmunoz.services;

import android.app.IntentService;
import android.content.Intent;
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

    public static final String ENGAGEMENT_BOREDOM_SCORE  = "ENGAGEMENT_BOREDOM_SCORE";
    public static final String EXCITEMENT_LONG_TERM_SCORE = "EXCITEMENT_LONG_TERM_SCORE";
    public static final String INSTANTANEOUS_EXCITEMENT_SCORE = "INSTANTANEOUS_EXCITEMENT_SCORE";
    public static final String RELAXATION_SCORE = "RELAXATION_SCORE";
    public static final String STRESS_SCORE = "STRESS_SCORE";
    public static final String INTEREST_SCORE = "INTEREST_SCORE";
    public static  final String MOTION_DATA = "MOTION_DATA";
    public static final String MOTION_NUMBER_OF_SAMPLE = "MOTION_NUMBER_OF_SAMPLE";

    public static final String COUNTER_MEMS = "COUNTER_MEMS";
    public static final String GYROX = "GYROX";
    public static final String GYROY = "GYROY";
    public static final String GYROZ = "GYROZ";
    public static final String ACCX = "ACCX";
    public static final String ACCY = "ACCY";
    public static final String ACCZ = "ACCZ";
    public static final String MAGX = "MAGX";
    public static final String MAGY = "MAGY";
    public static final String MAGZ = "MAGZ";
    public static final String TimeStamp = "TimeStamp";

    public EmotivService() {
        super("Emotivservice");
    }

    public void onCreate() {
        super.onCreate();
        insightDeviceConnected = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (insightDeviceConnected) {
            try {
                int stateConnect = IEdk.IEE_EngineGetNextEvent();
                if (stateConnect == IEdkErrorCode.EDK_OK.ToInt()) {
                    int eventType = IEdk.IEE_EmoEngineEventGetType();

                    // If no insight device is detected exit from loop
                    if (eventType == IEdk.IEE_Event_t.IEE_UserRemoved.ToInt()) {
                        Log.d("INFO", "Device disconnected");
                        IEdk.IEE_MotionDataFree();
                        //IEdk.IEE_EngineDisconnect();
                        break;
                    }

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("EMO_STATE_FILTER");
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    // Get Raw data
                    Log.d("INFO", "New raw data..");
                    int userId = IEdk.IEE_EmoEngineEventGetUserId();
                    IEdk.IEE_MotionDataUpdateHandle(userId);
                    double[] eeg_data = IEdk.IEE_MotionDataGet();
                    broadcastIntent.putExtra(EmotivService.MOTION_DATA, eeg_data);
                    broadcastIntent.putExtra(EmotivService.MOTION_NUMBER_OF_SAMPLE,IEdk.IEE_MotionDataGetNumberOfSample(userId));

                    // Search for new emo state
                    if (eventType == IEdk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()) {

                        IEdk.IEE_EmoEngineEventGetEmoState();

                        Log.d("INFO", "New emo State..");
                        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        broadcastIntent.putExtra(EmotivService.ENGAGEMENT_BOREDOM_SCORE, IEmoStateDLL.IS_PerformanceMetricGetEngagementBoredomScore());
                        broadcastIntent.putExtra(EmotivService.EXCITEMENT_LONG_TERM_SCORE, IEmoStateDLL.IS_PerformanceMetricGetExcitementLongTermScore());
                        broadcastIntent.putExtra(EmotivService.INSTANTANEOUS_EXCITEMENT_SCORE, IEmoStateDLL.IS_PerformanceMetricGetInstantaneousExcitementScore());
                        broadcastIntent.putExtra(EmotivService.RELAXATION_SCORE, IEmoStateDLL.IS_PerformanceMetricGetRelaxationScore());
                        broadcastIntent.putExtra(EmotivService.STRESS_SCORE, IEmoStateDLL.IS_PerformanceMetricGetStressScore());
                        broadcastIntent.putExtra(EmotivService.INTEREST_SCORE, IEmoStateDLL.IS_PerformanceMetricGetInterestScore());

                    } else {
                        Log.d("INFO", "No emo State detected..");
                    }
                    //Send insight data to broadcast receiver
                    sendBroadcast(broadcastIntent);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
