package com.example.wanderxx.spotifytest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;


public class RealModeActivity extends Activity implements  PlayerNotificationCallback, ConnectionStateCallback

    {
        TextView sensorDataAttentionView;
        TextView sensorDataMeditationView;
        TextView sensorDataBlinkLevelView;
        Button stopButton;
        BluetoothAdapter bluetoothAdapter;

        TGDevice tgDevice;
        final boolean rawEnabled = false;


        // TODO: Replace with your client ID
        private static final String CLIENT_ID = "ca8d24ae219744a49f10ac5dedff74df";
        // TODO: Replace with your redirect URI
        private static final String REDIRECT_URI = "com.example.wanderxx.spotifytest://callback";

        private Player mPlayer;

        private LongOperation lop;
        final Activity thisact=this;

        public Button btnhappy;
        public Button btnsad;

        public int last=-1;
        public int count=0;
        public boolean lastHappy;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorDataBlinkLevelView=(TextView) findViewById(R.id.sensorDataBlinkLevel);
        sensorDataMeditationView=(TextView) findViewById(R.id.sensorDataMeditation);
        sensorDataAttentionView= (TextView) findViewById(R.id.sensorDataAttention);

        stopButton = (Button)findViewById(R.id.sensorStop);
        stopButton.setEnabled(false);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setEnabled(false);
                sensorDataAttentionView.setText(getString(R.string.sensor_data_inital_text));
                sensorDataBlinkLevelView.setText("");
                sensorDataMeditationView.setText("");
            }
        });

        if(bluetoothAdapter == null) {
            // Alert user that Bluetooth is not available
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }else {
        /* create the TGDevice */
            tgDevice = new TGDevice(bluetoothAdapter, handler);
        }


        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "streaming"}, null, this);
    }

        @Override
        protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        System.out.println("The system generated uri is " +uri);
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "My Company Name", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    //    mPlayer.addConnectionStateCallback(MainActivity.this);
                    //    mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    //    mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });

            btnhappy = (Button) findViewById(R.id.btnHappy);
            btnhappy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // mPlayer.pause();
                    mPlayer.addConnectionStateCallback(RealModeActivity.this);
                    mPlayer.addPlayerNotificationCallback(RealModeActivity.this);
                    mPlayer.play("spotify:track:6NmXV4o6bmp704aPGyTVVG");
                    lop=new LongOperation(thisact);
                    lop.execute("spotify:track:6NmXV4o6bmp704aPGyTVVG");
                    //   ShowMeta("spotify:track:6NmXV4o6bmp704aPGyTVVG");

                }
            });



            Button btnChangeMode= (Button)findViewById(R.id.btnModeChange);
            btnChangeMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1= new Intent(RealModeActivity.this, ModeSelectionActivity.class);
                    startActivity(intent1);
                    finish();

                }
            });
            btnsad = (Button) findViewById(R.id.btnSad);
            btnsad.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // mPlayer.pause();
                    mPlayer.addConnectionStateCallback(RealModeActivity.this);
                    mPlayer.addPlayerNotificationCallback(RealModeActivity.this);
                    mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    lop=new LongOperation(thisact);
                    lop.execute("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    // ShowMeta("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");

                }
            });

        }
    }

        @Override
        public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

        @Override
        public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

        @Override
        public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

        @Override
        public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

        @Override
        public void onNewCredentials(String s) {
        Log.d("MainActivity", "User credentials blob received");
    }

        @Override
        public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

        @Override
        public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState
        playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

        @Override
        protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TGDevice.MSG_STATE_CHANGE:

                    switch (msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            break;
                        case TGDevice.STATE_CONNECTING:
                            Toast.makeText(thisact, "Connecting...\n", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.STATE_CONNECTED:
                            Toast.makeText(thisact, "Connected.\n", Toast.LENGTH_SHORT).show();
                            tgDevice.start();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            Toast.makeText(thisact, "Can't find\n", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.STATE_NOT_PAIRED:
                            Toast.makeText(thisact, "not paired\n", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            Toast.makeText(thisact, "Disconnected mang\n", Toast.LENGTH_SHORT).show();

                    }

                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    //signal = msg.arg1;
                   // tv.append("PoorSignal: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_RAW_DATA:
                    //raw1 = msg.arg1;
                    //tv.append("Got raw: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_HEART_RATE:
                   // tv.append("Heart rate: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_ATTENTION:
                    //att = msg.arg1;
                   // tv.append("Attention: " + msg.arg1 + "\n");
                    //Log.v("HelloA", "Attention: " + att + "\n");
                    sensorDataAttentionView.setText("Attention: "+ msg.arg1 + "\n");

                    break;
                case TGDevice.MSG_MEDITATION:
                    sensorDataMeditationView.setText("Meditation: "+ msg.arg1 + "\n");

                    if(last==-1) {
                        btnhappy.callOnClick();
                        lastHappy=true;
                    }
                    if(msg.arg1>=50){

                        if(last>=50){
                            count++;
                        }else{
                            count=1;
                        }
                        last=msg.arg1;
                        if(count==3 && !lastHappy){
                            Log.d("playhappy",count+"times >50");
                            count=1;
                            lastHappy=true;
                            btnhappy.callOnClick();
                        }
                    }
                    else{
                        if(last<50){
                            count++;
                        }else{
                            count=1;
                        }
                        last=msg.arg1;
                        if(count==3 && lastHappy) {
                            Log.d("playsad",count+"times <50");
                            count=1;
                            lastHappy=false;
                            btnsad.callOnClick();
                        }
                    }

                    break;

                case TGDevice.MSG_BLINK:
                    sensorDataBlinkLevelView.setText("Blink Level: "+  msg.arg1 + "\n");
                   // tv.append("Blink: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_RAW_COUNT:
                    //tv.append("Raw Count: " + msg.arg1 + "\n");
                    break;
                case TGDevice.MSG_LOW_BATTERY:
                    Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.MSG_RAW_MULTI:
                    //TGRawMulti rawM = (TGRawMulti)msg.obj;
                    //tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
                default:
                    break;
            }
        }
    };

    public void doStuff(View view) {
        if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
            tgDevice.connect(rawEnabled);
        //tgDevice.ena
    }

}
