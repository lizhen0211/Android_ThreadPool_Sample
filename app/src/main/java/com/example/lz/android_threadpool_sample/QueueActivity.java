package com.example.lz.android_threadpool_sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QueueActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
    }

    public void onArrayBlockingQueueClick(View view) {
        //如果线程池中的线程数量 大于 核心线程数 且小于 最大线程数，且workQueue已满，创建新线程处理；

        //此处，任务数量是6，核心线程数是3。运行线程数 大于 核心线程数，小于 最大线程数，且workQueue已满
        //则 超过核心线程数的任务 会被缓存到 workQueue中，待核心线程空闲再执行，超出工作队列长度的任务会被新启动的线程执行。
        //可以看到，前三个任务并行执行，第四、五个 任务被放到了工作队列里，延迟三秒执行，而第六个任务与前三个任务并行执行，因为启动了新的线程。
        int taskCount = 6;
        int corePoolSize = 3;
        int maximumPoolSize = 10;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    public void onLinkBlockingQueueClick(View view) {
        //与有界队列相比，除非系统资源耗尽，否则无界的任务队列不存在任务入队失败的情况
        int taskCount = 30;
        int corePoolSize = 3;
        int maximumPoolSize = 10;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        //此处，使用无参构造函数初始化，默认的容量是Integer.MAX_VALUE，将 LinkedBlockingDeque 作为无界队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }
}
