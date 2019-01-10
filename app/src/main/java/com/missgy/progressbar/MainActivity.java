package com.missgy.progressbar;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {
    private CircleProgressView circleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleProgress = findViewById(R.id.circleProgress);
        circleProgress.setProgressNum(0.9,3000);
    }
}
