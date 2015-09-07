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
    //Charts
    LineChart engagementBoredomChart;
    LineChart chart_instantateousExcitement;
    LineChart chart_longTermExcitement;
    LineChart chart_relaxationStress;
    LineChart chart_interest;
    //Datasets
    LineDataSet set_engagementBoredom;
    LineDataSet set_instantateousExcitement;
    LineDataSet set_longTermExcitement;
    LineDataSet set_relaxation;
    LineDataSet set_stress;
    LineDataSet set_interest;

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

        //Config engagement boredom chart
        engagementBoredomChart = (LineChart) root.findViewById(R.id.chart_engagementBoredom);
        setDefaultChartConfig(engagementBoredomChart);
        LineData data = new LineData();
        set_engagementBoredom = createDefaultSet("engagementBoredom", ColorTemplate.getHoloBlue());
        data.setValueTextColor(Color.WHITE);
        data.addDataSet(set_engagementBoredom);
        engagementBoredomChart.setData(data);

        //Config instantaneous excitement
        chart_instantateousExcitement = (LineChart) root.findViewById(R.id.chart_instantaneousExcitement);
        setDefaultChartConfig(chart_instantateousExcitement);
        LineData data2 = new LineData();
        set_instantateousExcitement = createDefaultSet("instantaneousExcitement",Color.MAGENTA);
        data2.setValueTextColor(Color.WHITE);
        data2.addDataSet(set_instantateousExcitement);
        chart_instantateousExcitement.setData(data2);

        //Config long term excitement
        chart_longTermExcitement = (LineChart) root.findViewById(R.id.chart_longTermExcitement);
        setDefaultChartConfig(chart_longTermExcitement);
        LineData data3 = new LineData();
        set_longTermExcitement = createDefaultSet("longTermExcitement", Color.GREEN);
        data3.setValueTextColor(Color.WHITE);
        data3.addDataSet(set_longTermExcitement);
        chart_longTermExcitement.setData(data3);

        //Config relaxation stress chart
        chart_relaxationStress = (LineChart) root.findViewById(R.id.chart_relaxationStress);
        setDefaultChartConfig(chart_relaxationStress);
        LineData data4 = new LineData();
        set_relaxation = createDefaultSet("relaxation",  Color.BLUE);
        set_stress = createDefaultSet("stress", Color.RED);
        data4.setValueTextColor(Color.WHITE);
        data4.addDataSet(set_relaxation);
        data4.addDataSet(set_stress);
        chart_relaxationStress.setData(data4);

        //Config interest chart
        chart_interest = (LineChart) root.findViewById(R.id.chart_interest);
        setDefaultChartConfig(chart_interest);
        LineData data5 = new LineData();
        set_interest = createDefaultSet("interest", Color.YELLOW);
        data5.setValueTextColor(Color.WHITE);
        data5.addDataSet(set_interest);
        chart_interest.setData(data5);

        return root;
    }

    private void setDefaultChartConfig(LineChart chart) {

        chart.setDescription("");
        chart.setNoDataTextDescription("No data for the moment");
        chart.setHighlightEnabled(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setDragEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.LTGRAY);


        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1   = chart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = chart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(1f);
        y1.setDrawGridLines(true);

        YAxis y2 = chart.getAxisRight();
        y2.setEnabled(false);


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

                LineData data = engagementBoredomChart.getData();
                if (data != null) {
                    LineDataSet set = data.getDataSetByIndex(0);
                    data.addXValue("");
                    data.addEntry(new Entry(engagementBoredomScore, set.getEntryCount()), 0);
                    engagementBoredomChart.notifyDataSetChanged();
                    engagementBoredomChart.setVisibleXRange(0, 50);
                    engagementBoredomChart.moveViewToX(data.getXValCount() - 51);
                }

            LineData data2 = chart_instantateousExcitement.getData();
            if (data2 != null) {
                LineDataSet set = data2.getDataSetByIndex(0);
                data2.addXValue("");
                data2.addEntry(new Entry(instantaneousExcitementScore, set.getEntryCount()), 0);
                chart_instantateousExcitement.notifyDataSetChanged();
                chart_instantateousExcitement.setVisibleXRange(0, 50);
                chart_instantateousExcitement.moveViewToX(data2.getXValCount() - 51);
            }

            LineData data3 = chart_longTermExcitement.getData();
            if (data3 != null) {
                LineDataSet set = data3.getDataSetByIndex(0);
                data3.addXValue("");
                data3.addEntry(new Entry(excitementLongTermScore, set.getEntryCount()), 0);
                chart_longTermExcitement.notifyDataSetChanged();
                chart_longTermExcitement.setVisibleXRange(0, 50);
                chart_longTermExcitement.moveViewToX(data3.getXValCount() - 51);
            }

            LineData data4 = chart_relaxationStress.getData();
            if (data4 != null) {
                LineDataSet set1 = data4.getDataSetByIndex(0);
                LineDataSet set2 = data4.getDataSetByIndex(1);
                data4.addXValue("");
                data4.addEntry(new Entry(relaxationScore, set1.getEntryCount()), 0);
                data4.addEntry(new Entry(stressScore, set2.getEntryCount()), 1);
                chart_relaxationStress.notifyDataSetChanged();
                chart_relaxationStress.setVisibleXRange(0, 50);
                chart_relaxationStress.moveViewToX(data4.getXValCount() - 51);
            }


            LineData data5 = chart_interest.getData();
            if (data5 != null) {
                LineDataSet set = data5.getDataSetByIndex(0);
                data5.addXValue("");
                data5.addEntry(new Entry(interestScore, set.getEntryCount()), 0);
                chart_interest.notifyDataSetChanged();
                chart_interest.setVisibleXRange(0, 50);
                chart_interest.moveViewToX(data5.getXValCount() - 51);
            }
                tv_relaxationScore.setText(relaxationScore + "");
                tv_engagementBoredomScore.setText(engagementBoredomScore + "");
                tv_excitementLongTermScore.setText(excitementLongTermScore + "");
                tv_instantaneousExcitementScore.setText(instantaneousExcitementScore + "");
                tv_stressScore.setText(stressScore + "");
                tv_interestScore.setText(interestScore + "");

            }

        public void addEntry(){

        }

        }

    public LineDataSet createDefaultSet(String title, int color ){
        LineDataSet set = new LineDataSet(null, title);
        set.setDrawCubic(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setLineWidth(2f);
        //set.setCircleSize(4f);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawCircles(false);
        //set.setValueTextColor(Color.WHITE);
        //set.setValueTextSize(10f);
        set.setDrawValues(false);

        return set;
    }
}
