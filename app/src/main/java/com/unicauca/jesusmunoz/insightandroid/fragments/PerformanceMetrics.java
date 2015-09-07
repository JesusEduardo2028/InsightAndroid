package com.unicauca.jesusmunoz.insightandroid.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.unicauca.jesusmunoz.insightandroid.R;
import com.unicauca.jesusmunoz.main.Graph;
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
    private LineChart engagementBoredomChart;
    LineDataSet data_engagementBoredom;


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

        View root = inflater.inflate(R.layout.fragment_performance_metrics, container, false);

        tv_relaxationScore = (TextView) root.findViewById(R.id.tv_relaxationScore);
        tv_engagementBoredomScore = (TextView) root.findViewById(R.id.tv_enboredom);
        tv_excitementLongTermScore = (TextView) root.findViewById(R.id.tv_exlong);
        tv_instantaneousExcitementScore = (TextView) root.findViewById(R.id.tv_exins);
        tv_stressScore = (TextView) root.findViewById(R.id.tv_stress_score);
        tv_interestScore = (TextView) root.findViewById(R.id.tv_interest_score);

        engagementBoredomChart = (LineChart) root.findViewById(R.id.chart_engagementBoredom);

        engagementBoredomChart.setDescription("");
        engagementBoredomChart.setNoDataTextDescription("No data for the moment");
        engagementBoredomChart.setHighlightEnabled(true);
        engagementBoredomChart.setTouchEnabled(true);
        engagementBoredomChart.setDragEnabled(true);
        engagementBoredomChart.setDragEnabled(true);
        engagementBoredomChart.setDrawGridBackground(false);
        engagementBoredomChart.setPinchZoom(true);
        engagementBoredomChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        engagementBoredomChart.setData(data);

        Legend l = engagementBoredomChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);


        XAxis x1   = engagementBoredomChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = engagementBoredomChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(1f);
        y1.setDrawGridLines(true);

        YAxis y2 = engagementBoredomChart.getAxisRight();
        y2.setEnabled(false);


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
                addEntry();
                    //graphEngagementBoredom.addData(engagementBoredomScore)
                // ;
                //LineData data = engagementBoredomChart.getData();
                //LineDataSet set = data.getDataSetByIndex(0);

                //data.addEntry(new Entry(engagementBoredomScore,set.getEntryCount()),0);
                //engagementBoredomChart.notifyDataSetChanged();
                //engagementBoredomChart.setVisibleXRangeMaximum(6);
                //engagementBoredomChart.moveViewToX(data.getXValCount() - 7);
                tv_relaxationScore.setText(relaxationScore + "");
                tv_engagementBoredomScore.setText(engagementBoredomScore + "");
                tv_excitementLongTermScore.setText(excitementLongTermScore + "");
                tv_instantaneousExcitementScore.setText(instantaneousExcitementScore + "");
                tv_stressScore.setText(stressScore + "");
                tv_interestScore.setText(interestScore + "");
            }

        public void addEntry(){
            if (engagementBoredomChart != null) {
                LineData data = engagementBoredomChart.getData();
                if (data != null) {
                    LineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet();
                        data.addDataSet(set);
                    }
                    data.addXValue("");
                    data.addEntry(new Entry((float) Math.random(), set.getEntryCount()), 0);
                    engagementBoredomChart.notifyDataSetChanged();
                    engagementBoredomChart.setVisibleXRange(0, 6);
                    engagementBoredomChart.moveViewToX(data.getXValCount() - 7);

                }

            }
        }
        public LineDataSet createSet(){
            LineDataSet set = new LineDataSet(null, "engagementBoredom");
            set.setDrawCubic(true);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(ColorTemplate.getHoloBlue());
            set.setLineWidth(2f);
            set.setCircleSize(4f);
            set.setFillColor(ColorTemplate.getHoloBlue());
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setValueTextColor(Color.WHITE);
            set.setValueTextSize(10f);

            return set;
        }

        }
}
