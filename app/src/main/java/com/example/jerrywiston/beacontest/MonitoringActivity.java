package com.example.jerrywiston.beacontest;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 * Created by jerrywiston on 2017/7/27.
 */

public class MonitoringActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView)findViewById(R.id.textBeacon);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
                Log.i("GGGG", region.toString());
                showText(mTextView.getText() + "\nEnter " + region.toString());
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                showText(mTextView.getText() + "\nExit " + region.toString());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
                Log.i("GGGG", region.toString());
                showText(mTextView.getText() + "\nSwitch " + region.toString());
            }
        });

        try {
            //beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
            Identifier id1 = Identifier.parse("d3556e50-c856-11e3-8408-0221a885ef40");
            Identifier id2 = Identifier.parse("62182");
            Identifier id3 = Identifier.parse("48761");

            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", id1, id2, id3));
        } catch (RemoteException e) {    }
    }

    public void showText(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText(line);
            }
        });
    }


}
