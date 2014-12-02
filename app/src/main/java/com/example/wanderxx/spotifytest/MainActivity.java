package com.example.wanderxx.spotifytest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wanderxx.neurosky.NeuroskyUtility;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements

    PlayerNotificationCallback, ConnectionStateCallback {
    TextView sensorDataAttentionView;
    TextView sensorDataMeditationView;
    TextView sensorDataBlinkLevelView;
    Button generateButton;
    Button stopButton;
    Timer timer;

    NeuroskyUtility neuroskyUtility=null;
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "ca8d24ae219744a49f10ac5dedff74df";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "com.example.wanderxx.spotifytest://callback";

    private Player mPlayer;
    private LongOperation lop;
    final Activity thisact=this;

    public Button btnhappy;
    public Button btnsad;

    String[] happylist={"spotify:track:1j2sRZpFUzG6wPJGfRvet3",
            "spotify:track:4kgsK0fftHtg9gZOzkU5T2",
            "spotify:track:0ifSeVGUr7py5GggttDhXw",
            "spotify:track:3F3Ggfzm4DwDB42xJOrB2L",
            "spotify:track:6NmXV4o6bmp704aPGyTVVG"};
    String[] sadlist={"spotify:track:7rJLjgqq5rmHxWitapy0me",
            "spotify:track:0sGNcx6lfc3zrbiIOxY4vb",
            "spotify:track:6HJujxcjMmVZhd8z5EpPri",
            "spotify:track:51pQ7vY7WXzxskwloaeqyj",
            "spotify:track:2TpxZ7JUBn3uw46aR7qd6V"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        neuroskyUtility= NeuroskyUtility.getInstance();

        sensorDataBlinkLevelView=(TextView) findViewById(R.id.sensorDataBlinkLevel);
        sensorDataMeditationView=(TextView) findViewById(R.id.sensorDataMeditation);
        sensorDataAttentionView= (TextView) findViewById(R.id.sensorDataAttention);
        generateButton = (Button)findViewById(R.id.sensorGenerate);
        stopButton = (Button)findViewById(R.id.sensorStop);
        stopButton.setEnabled(false);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer= new Timer();
                stopButton.setEnabled(true);
                generateButton.setEnabled(false);
                UpdateViewTimerTask updateViewTimerTask = new UpdateViewTimerTask();
                timer.schedule(updateViewTimerTask,1000,1200);
                //     stopButton.setActivated(false);
            }
        });

        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "streaming"}, null, this);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
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

            Button btnChangeMode= (Button)findViewById(R.id.btnModeChange);
            btnChangeMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1= new Intent(MainActivity.this, ModeSelectionActivity.class);
                    startActivity(intent1);
                    finish();

                }
            });

            btnhappy = (Button) findViewById(R.id.btnHappy);
            btnhappy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   // mPlayer.pause();
                    mPlayer.addConnectionStateCallback(MainActivity.this);
                    mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    String song=happylist[(int)(Math.random()*5)];
                    mPlayer.play(song);
                    lop=new LongOperation(thisact);
                    lop.execute("spotify:track:6NmXV4o6bmp704aPGyTVVG");
                 //   ShowMeta("spotify:track:6NmXV4o6bmp704aPGyTVVG");

                }
            });

            btnsad = (Button) findViewById(R.id.btnSad);
            btnsad.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   // mPlayer.pause();
                    mPlayer.addConnectionStateCallback(MainActivity.this);
                    mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    String song=sadlist[(int)(Math.random()*5)];
                    mPlayer.play(song);
                    lop=new LongOperation(thisact);
                    lop.execute("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                   // ShowMeta("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");

                }
            });

            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generateButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    timer.cancel();
                    timer.purge();
                    timer=null;
                    mPlayer.pause();
                    sensorDataAttentionView.setText(getString(R.string.sensor_data_inital_text));
                    sensorDataBlinkLevelView.setText("");
                    sensorDataMeditationView.setText("");
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
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    public void playMode(int medi){
        if (medi>=50){
            btnhappy.callOnClick();
        }
        else{
            btnsad.callOnClick();
        }
    }


    class UpdateViewTimerTask extends TimerTask {
        int last=-1;
        int count=0;
        boolean lastHappy;
        @Override
        public void run() {

            final int data[] = neuroskyUtility.randNeuroskyData();
            Log.e("Main Activity", Integer.toString(data[0]));
            sensorDataAttentionView.post(new Runnable() {
                @Override
                public void run() {
                    sensorDataAttentionView.setText("Attention: "+ Integer.toString(data[0]));
                }
            });
            Log.e("Main Activity", Integer.toString(data[1]));

            sensorDataMeditationView.post(new Runnable() {
                @Override
                public void run() {
                    sensorDataMeditationView.setText("Meditation: "+ Integer.toString(data[1]));
                    if(last==-1) {
                        btnhappy.callOnClick();
                        lastHappy=true;
                    }
                    if(data[1]>=50){

                        if(last>=50){
                            count++;
                        }else{
                            count=1;
                        }
                        last=data[1];
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
                        last=data[1];
                        if(count==3 && lastHappy) {
                            Log.d("playsad",count+"times <50");
                            count=1;
                            lastHappy=false;
                            btnsad.callOnClick();
                        }
                    }

                }
            });
            Log.e("Main Activity", Integer.toString(data[2]));

            sensorDataBlinkLevelView.post(new Runnable() {
                @Override
                public void run() {
                    sensorDataBlinkLevelView.setText("Blink Level: "+ Integer.toString(data[2]));
                }
            });

        }
    }

}
