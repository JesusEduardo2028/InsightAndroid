package com.unicauca.jesusmunoz.insightandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unicauca.jesusmunoz.services.EmotivService;

public class HomeActivity extends AppCompatActivity {

    //private BufferedWriter Emo_writer;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private InsightReceiver receiver;

    TextView tv_relaxationScore;
    TextView tv_engagementBoredomScore;
    TextView tv_instantaneousExcitementScore;
    TextView tv_excitementLongTermScore;
    TextView tv_stressScore;
    TextView tv_interestScore;
    TextView tv_motion_data;
    TextView tv_number_samples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Register broadcast receiver
        IntentFilter filter = new IntentFilter("EMO_STATE_FILTER");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new InsightReceiver();

        registerReceiver(receiver, filter);
        setContentView(R.layout.activity_home);

    }


    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startEmoStates(View v)
    {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        //AsyncTask searchDevicesTask = new EmotivConnectTask(this).execute();
    }


    public class InsightReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            float engagementBoredomScore = intent.getFloatExtra(EmotivService.ENGAGEMENT_BOREDOM_SCORE,0);
            float excitementLongTermScore = intent.getFloatExtra(EmotivService.EXCITEMENT_LONG_TERM_SCORE,0);
            float instantaneousExcitementScore = intent.getFloatExtra(EmotivService.INSTANTANEOUS_EXCITEMENT_SCORE,0);
            float relaxationScore = intent.getFloatExtra(EmotivService.RELAXATION_SCORE,0);
            float stressScore = intent.getFloatExtra(EmotivService.STRESS_SCORE,0);
            float interestScore = intent.getFloatExtra(EmotivService.INTEREST_SCORE, 0);

            double[] eeg_data = intent.getDoubleArrayExtra(EmotivService.MOTION_DATA);
            int motion_number_sample = intent.getIntExtra(EmotivService.MOTION_NUMBER_OF_SAMPLE, 0);

            tv_relaxationScore = (TextView) findViewById(R.id.tv_relaxationScore);
            tv_engagementBoredomScore = (TextView) findViewById(R.id.tv_enboredom);
            tv_excitementLongTermScore = (TextView) findViewById(R.id.tv_exlong);
            tv_instantaneousExcitementScore = (TextView) findViewById(R.id.tv_exins);
            tv_stressScore = (TextView) findViewById(R.id.tv_stress_score);
            tv_interestScore = (TextView) findViewById(R.id.tv_interest_score);
            tv_motion_data = (TextView) findViewById(R.id.tv_motion_data);
            tv_number_samples = (TextView) findViewById(R.id.tv_number_samples);
            tv_relaxationScore.setText(relaxationScore+"");
            tv_engagementBoredomScore.setText(engagementBoredomScore+"");
            tv_excitementLongTermScore.setText(excitementLongTermScore+"");
            tv_instantaneousExcitementScore.setText(instantaneousExcitementScore+"");
            tv_stressScore.setText(stressScore+"");
            tv_interestScore.setText(interestScore+"");
            tv_number_samples.setText(motion_number_sample+"");

            printFirstMotionDataSample(eeg_data);
        }
    }


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
    public void WriteMotionData(double[] eeg_data){

        for (int i = 0; i < eeg_data.length/11; i++) {
            String input = "";
            for (int j = 0; j < 11; j++) {
                input += (String.valueOf(eeg_data[i*11+j]) + ",");
            }
            input = input.substring(0, input.length() - 1);

        }
    }

}
