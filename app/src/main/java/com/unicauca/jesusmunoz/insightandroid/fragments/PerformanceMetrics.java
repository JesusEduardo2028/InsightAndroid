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


public class PerformanceMetrics extends Fragment {
    public static String TAG = "PERFORMANCE_METRICS_FRAGMENT";
    private InsightReceiver receiver;
    IntentFilter filter;

    TextView tv_relaxationScore;
    TextView tv_engagementBoredomScore;
    TextView tv_instantaneousExcitementScore;
    TextView tv_excitementLongTermScore;
    TextView tv_stressScore;
    TextView tv_interestScore;

    public PerformanceMetrics() {
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
        return inflater.inflate(R.layout.fragment_performance_metrics, container, false);
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

                float engagementBoredomScore = intent.getFloatExtra(EmotivService.ENGAGEMENT_BOREDOM_SCORE, 0);
                float excitementLongTermScore = intent.getFloatExtra(EmotivService.EXCITEMENT_LONG_TERM_SCORE, 0);
                float instantaneousExcitementScore = intent.getFloatExtra(EmotivService.INSTANTANEOUS_EXCITEMENT_SCORE, 0);
                float relaxationScore = intent.getFloatExtra(EmotivService.RELAXATION_SCORE, 0);
                float stressScore = intent.getFloatExtra(EmotivService.STRESS_SCORE, 0);
                float interestScore = intent.getFloatExtra(EmotivService.INTEREST_SCORE, 0);

                tv_relaxationScore = (TextView) getView().findViewById(R.id.tv_relaxationScore);
                tv_engagementBoredomScore = (TextView) getView().findViewById(R.id.tv_enboredom);
                tv_excitementLongTermScore = (TextView) getView().findViewById(R.id.tv_exlong);
                tv_instantaneousExcitementScore = (TextView) getView().findViewById(R.id.tv_exins);
                tv_stressScore = (TextView) getView().findViewById(R.id.tv_stress_score);
                tv_interestScore = (TextView) getView().findViewById(R.id.tv_interest_score);


                tv_relaxationScore.setText(relaxationScore + "");
                tv_engagementBoredomScore.setText(engagementBoredomScore + "");
                tv_excitementLongTermScore.setText(excitementLongTermScore + "");
                tv_instantaneousExcitementScore.setText(instantaneousExcitementScore + "");
                tv_stressScore.setText(stressScore + "");
                tv_interestScore.setText(interestScore + "");
            }

        }
}
