package com.unicauca.jesusmunoz.insightandroid.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.unicauca.jesusmunoz.insightandroid.R;
import com.unicauca.jesusmunoz.services.EmotivService;

/**
 * Created by jesuseduardomunoz on 9/4/15.
 */
public class MotionDataFragment extends Fragment {
    public static String TAG = "MOTION_DATA";
    private InsightReceiver receiver;
    IntentFilter filter;


    TextView tv_motion_data;
    TextView tv_number_samples;


    public MotionDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new InsightReceiver();
        filter = new IntentFilter("EMO_STATE_FILTER");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(receiver);
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motion_data, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //You can define an insight receiver either inside an Activity or a Fragment.
    //If the receiver is in the activity you have to checkout what fragment is displayed before updating any view component
    public class InsightReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            tv_motion_data = (TextView) getView().findViewById(R.id.tv_motion_data);
            tv_number_samples = (TextView) getView().findViewById(R.id.tv_number_samples);
            double[] eeg_data = intent.getDoubleArrayExtra(EmotivService.MOTION_DATA);
            int motion_number_sample = intent.getIntExtra(EmotivService.MOTION_NUMBER_OF_SAMPLE, 0);
            printFirstMotionDataSample(eeg_data);
            tv_number_samples.setText(motion_number_sample + "");
        }

        //The Emotiv Sdk reads a number of eeg data samples every time
        //This method just prints the first Sample of eeg data
        //Each eeg data sample is composed for 11 elements
        public void printFirstMotionDataSample(double[] eeg_data) {
            tv_motion_data.setText("");
            if (eeg_data.length >= 11) {

                String input = "";
                for (int i = 0; i < 11; i++) {
                    input += (String.valueOf(eeg_data[i]) + ",");
                }
                input = input.substring(0, input.length() - 1);
                tv_motion_data.append(input);
            }
        }
    }
}