package com.example.lz.android_threadpool_sample;

import android.util.Log;

public class MyRunnable implements Runnable {

    private int index;

    public MyRunnable(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        Log.e(MyRunnable.class.getSimpleName(), "这是第" + (index + 1) + "个任务 " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getIndex() {
        return index;
    }
}
