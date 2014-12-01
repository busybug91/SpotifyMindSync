package com.example.wanderxx.spotifytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wanderxx.neurosky.HelloEEGActivity;

public class ModeSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);
        Toast.makeText(this, "This is a toast", Toast.LENGTH_SHORT).show();
        Button btnSimulation=(Button)findViewById(R.id.btnSimulation);
        btnSimulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ModeSelectionActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Button btnRealMode=(Button)findViewById(R.id.btnReal);
        btnRealMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ModeSelectionActivity.this, RealModeActivity.class);
                startActivity(intent);
            }
        });

        Button btnLiveMode=(Button)findViewById(R.id.btnLive);
        btnLiveMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ModeSelectionActivity.this, HelloEEGActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mode_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
