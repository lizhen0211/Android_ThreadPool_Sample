package com.example.lz.android_threadpool_sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_factory);
    }

    /**
     * 自定义 ThreadFactory
     *
     * @param view
     */
    public void onCustomThreadFactoryClick(View view) {
        int taskCount = 3;
        int corePoolSize = 5;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(128);
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "pool-" + "thread-" + threadNumber.getAndIncrement());
            }
        };
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * ExcutorsThreadFactory
     *
     * @param view
     */
    public void onExcutorsThreadFactoryClick(View view) {
        int taskCount = 3;
        int corePoolSize = 5;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(128);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadFactory privilegedThreadFactory = Executors.privilegedThreadFactory();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 开源项目 ThreadFactory
     *
     * @param view
     */
    public void onOpenSourceThreadFactoryClick(View view) {
        int taskCount = 3;
        int corePoolSize = 5;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(128);
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("alias-pool-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }


    }


}
