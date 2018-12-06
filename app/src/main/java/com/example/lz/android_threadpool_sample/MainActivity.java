package com.example.lz.android_threadpool_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCoreSzieMaxSizeWorkQueueClick(View view) {
        Intent intent = new Intent(MainActivity.this, CoreSzieMaxSizeWorkQueueActivity.class);
        startActivity(intent);
    }

    public void onQueueClick(View view) {
        Intent intent = new Intent(MainActivity.this, QueueActivity.class);
        startActivity(intent);
    }

    public void onRejectedPolicyClick(View view) {
        Intent intent = new Intent(MainActivity.this, RejectedPolicyActivity.class);
        startActivity(intent);
    }

    public void onThreadFactoryClick(View view) {
        Intent intent = new Intent(MainActivity.this, ThreadFactoryActivity.class);
        startActivity(intent);
    }
}
