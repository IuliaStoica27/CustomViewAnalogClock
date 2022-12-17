package com.link.analogclock;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    EditText h, m, s;
    MyAnalogClock c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = findViewById(R.id.hourEditText);
        m = findViewById(R.id.minEditText);
        s = findViewById(R.id.secEditText);
        c = findViewById(R.id.myClock);
    }

    public void btnOnClick(View view) {
        int hour, min, sec;

            try {
                hour = Integer.parseInt(h.getText().toString());
            } catch (Exception e){
                hour = 0;}

            try {
                min = Integer.parseInt(m.getText().toString());
            } catch (Exception e){
                min = 0;}

            try {
                sec = Integer.parseInt(s.getText().toString());
            } catch (Exception e){
                sec = 0;}


        c.setTime(hour, min, sec);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();}
}