package com.unicauca.jesusmunoz.insightandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unicauca.jesusmunoz.insightaffectiv.EmoStatesActivity;
import com.unicauca.jesusmunoz.insightaffectiv.EmotivConnectTask;
import com.unicauca.jesusmunoz.services.EmotivService;

import java.io.BufferedWriter;

public class HomeActivity extends AppCompatActivity {


    private Boolean lock = false, engineConnector = false;
    private BufferedWriter Emo_writer;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private MyWebRequestReceiver receiver;

    IntentFilter myFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myFilter = new IntentFilter("RECEIVER_FILTER");
        myFilter.addAction("RECEIVER_FILTER");

        IntentFilter filter = new IntentFilter("EMO_STATE_FILTER");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
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

        AsyncTask searchDevicesTask = new EmotivConnectTask(this).execute();
    }



    public class MyWebRequestReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {


            Toast.makeText(HomeActivity.this, "OKOK", Toast.LENGTH_SHORT).show();


        }


    }




}
