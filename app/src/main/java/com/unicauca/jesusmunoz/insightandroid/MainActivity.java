package com.unicauca.jesusmunoz.insightandroid;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.unicauca.jesusmunoz.main.EmotivConnectTask;
import com.unicauca.jesusmunoz.insightandroid.fragments.PerformanceMetrics;
import com.unicauca.jesusmunoz.insightandroid.fragments.SettingsFragment;
import com.unicauca.jesusmunoz.services.EmotivService;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


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


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private  Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        IntentFilter filter = new IntentFilter("EMO_STATE_FILTER");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new InsightReceiver();

        registerReceiver(receiver, filter);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragment = null;
        switch (position) {
            case 0:
                fragment = new PerformanceMetrics();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, PerformanceMetrics.TAG)
                        .commit();
                break;
            case 1:
                fragment = new SettingsFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, SettingsFragment.TAG)
                        .commit();
                break;
            case 2:
                fragment = new SettingsFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, SettingsFragment.TAG)
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onNavigationDrawerItemSelected(2);
        }
        return super.onOptionsItemSelected(item);
    }


    public void startEmoStates() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        AsyncTask searchDevicesTask = new EmotivConnectTask(this).execute();
        searchDevicesTask.getStatus();
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void changeActivity(View v){
        startActivity(new Intent(this,HomeActivity.class));
    }

    public class InsightReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PerformanceMetrics myFragment = (PerformanceMetrics) getSupportFragmentManager().findFragmentByTag(PerformanceMetrics.TAG);
            if (myFragment != null && myFragment.isVisible()) {
                // add your code here
                float engagementBoredomScore = intent.getFloatExtra(EmotivService.ENGAGEMENT_BOREDOM_SCORE, 0);
                float excitementLongTermScore = intent.getFloatExtra(EmotivService.EXCITEMENT_LONG_TERM_SCORE, 0);
                float instantaneousExcitementScore = intent.getFloatExtra(EmotivService.INSTANTANEOUS_EXCITEMENT_SCORE, 0);
                float relaxationScore = intent.getFloatExtra(EmotivService.RELAXATION_SCORE, 0);
                float stressScore = intent.getFloatExtra(EmotivService.STRESS_SCORE, 0);
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

                tv_relaxationScore.setText(relaxationScore + "");
                tv_engagementBoredomScore.setText(engagementBoredomScore + "");
                tv_excitementLongTermScore.setText(excitementLongTermScore + "");
                tv_instantaneousExcitementScore.setText(instantaneousExcitementScore + "");
                tv_stressScore.setText(stressScore + "");
                tv_interestScore.setText(interestScore + "");
            }
        }
    }
}