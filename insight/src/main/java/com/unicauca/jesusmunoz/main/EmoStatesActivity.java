package com.unicauca.jesusmunoz.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emotiv.insight.*;
import com.emotiv.insight.IEdk.IEE_Event_t;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EmoStatesActivity extends Activity {

	private static float[] Data = new float[4];
	RelativeLayout reEngagementBoredom, reLongExcitement, reInstantaneousExcitement, reMeditation;
	Graph graphEngagementBoredom, graphLongExcitement, graphInstantaneousExcitement, graphMeditation;
	Button btstartRecord;
	private Boolean lock = false, engineConnector = false;
	private BufferedWriter Emo_writer;
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		reEngagementBoredom = (RelativeLayout) findViewById(R.id.reEngagement);
		reLongExcitement = (RelativeLayout) findViewById(R.id.relongExcitement);
		reInstantaneousExcitement = (RelativeLayout) findViewById(R.id.reInstantaneousExcitement);
		reMeditation = (RelativeLayout) findViewById(R.id.reMeditation);

		Point pt = new Point();
		getWindowManager().getDefaultDisplay().getSize(pt);
		int graphWidth = pt.x - 10;
		graphEngagementBoredom = new Graph(this, graphWidth, Color.rgb(146, 211, 238));
		graphEngagementBoredom.setScale(0.5f);
		graphLongExcitement = new Graph(this, graphWidth, Color.parseColor("#22b371"));
		graphLongExcitement.setScale(0.5f);
		graphInstantaneousExcitement = new Graph(this, graphWidth, Color.parseColor("#ffa500"));
		graphInstantaneousExcitement.setScale(0.5f);
		graphMeditation = new Graph(this, graphWidth, Color.parseColor("#a6c67b"));
		graphMeditation.setScale(0.5f);

		reEngagementBoredom.addView(graphEngagementBoredom);
		reLongExcitement.addView(graphLongExcitement);
		reInstantaneousExcitement.addView(graphInstantaneousExcitement);
		reMeditation.addView(graphMeditation);

		btstartRecord = (Button) findViewById(R.id.btstartRecord);
		btstartRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



            /*    if (!engineConnector)
                    Toast.makeText(EmoStatesActivity.this, "You need to connect to your headset.", Toast.LENGTH_SHORT).show();
                else {
                    if (btstartRecord.getText().toString().equals("Start Record")) {
                        File root = Environment.getExternalStorageDirectory();
                        String path = root.getAbsolutePath() + "/Emostate/";
                        File pending = new File(path);
                        if (!pending.exists())
                            pending.mkdirs();
                        setdataFilePath();
                        btstartRecord.setText("Stop Record");
                    } else {
                        try {
                            Emo_writer.flush();
                            Emo_writer.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        btstartRecord.setText("Start Record");
                    }
                }*/
            }
        });

        IEdk.IEE_EngineConnect(EmoStatesActivity.this);
        if (!connectThread.isAlive()) connectThread.start();
	}

	public void setdataFilePath() {
		File root = Environment.getExternalStorageDirectory();
		String path = root.getAbsolutePath() + "/Emostate/emotionalState_insight.csv";
		try {
			this.Emo_writer = new BufferedWriter(new FileWriter(path));
			String emoHeader = "Engagement/Boredom,LongTermExcitement,InstantaneousExcitement,Meditation";
			this.Emo_writer.write(emoHeader);
			this.Emo_writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Thread connectThread = new Thread() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (true) {
				try {
					int number = IEdk.IEE_GetNumberDeviceInsight();
					if (number != 0) {
						if (!lock) {
							lock = true;
							IEdk.IEE_ConnectDevice(0);
						}
					} else {
						lock = false;
					}
					int stateConnect = IEdk.IEE_EngineGetNextEvent();

					if (stateConnect == IEdkErrorCode.EDK_OK.ToInt()) {
                        Log.d("code", stateConnect + "");
						int eventType = IEdk.IEE_EmoEngineEventGetType();
						if (eventType == IEE_Event_t.IEE_UserAdded.ToInt()) {
							engineConnector = true;

                            Message message = dialogHandler.obtainMessage();
                            Bundle bundle  = new Bundle();
                            bundle.putString("message","Connect insight successful.");
                            message.setData(bundle);
                            dialogHandler.sendMessage(message);

						} else if (eventType == IEE_Event_t.IEE_UserRemoved.ToInt()) {
							engineConnector = false;

                            Message message = dialogHandler.obtainMessage();
                            Bundle bundle  = new Bundle();
                            bundle.putString("message","Insight disconnected.");
                            message.setData(bundle);
                            dialogHandler.sendMessage(message);

						}
						if (eventType == IEE_Event_t.IEE_EmoStateUpdated.ToInt()) {
                            Log.d("DATA", "EMO DATA");
							IEdk.IEE_EmoEngineEventGetEmoState();
							Data[0] = IEmoStateDLL.IS_PerformanceMetricGetEngagementBoredomScore();
							Data[1] = IEmoStateDLL.IS_PerformanceMetricGetExcitementLongTermScore();
							Data[2] = IEmoStateDLL.IS_PerformanceMetricGetInstantaneousExcitementScore();
							Data[3] = IEmoStateDLL.IS_PerformanceMetricGetRelaxationScore();
							handler.sendMessage(handler.obtainMessage(0, Data));
						}else{
                            Log.d("NO DATA", "NO DATA");
                        }
					}
					Thread.sleep(5);
				} catch (Exception ex) {
					ex.printStackTrace();
                    Log.d("ERROR", "ERROR******************************");
				}
			}
		}
	};

    public Handler dialogHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {

            String message = msg.getData().getString("message");

            final Dialog dialog = new Dialog(EmoStatesActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_connect_success);
            TextView tv_connect_success = (TextView) dialog.findViewById(R.id.tv_connect_success);
            tv_connect_success.setText(message);

            Button btn_connect = (Button) dialog.findViewById(R.id.btn_connect);
            btn_connect.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    };
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
            Log.d("Emo MESSAGE", msg.toString());
			switch (msg.what) {
				case 0:
					float[] data = (float[]) msg.obj;
					graphEngagementBoredom.addData(data[0]);
					graphLongExcitement.addData(data[1]);
					graphInstantaneousExcitement.addData(data[2]);
					graphMeditation.addData(data[3]);
				/*	String input = "";
					if (Emo_writer != null) {
						input += data[0] + "," + data[1] + "," + data[2] + "," + data[3];
						try {
							Emo_writer.write(input);
							Emo_writer.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;*/
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		if (connectThread.isAlive()) {
            IEdk.IEE_EngineDisconnect();
            connectThread.interrupt(); // request to terminate thread in regular way
            try {
				connectThread.join(1000); // wait until thread ends or timeout after 0.5 second
                Toast.makeText(EmoStatesActivity.this, "Insight disconnected", Toast.LENGTH_SHORT).show();
                super.onDestroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}