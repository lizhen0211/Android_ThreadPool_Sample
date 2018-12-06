package com.example.lz.android_threadpool_sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 核心线程数、最大线程数、工作队列
 */
public class CoreSzieMaxSizeWorkQueueActivity extends Activity {

    /**
     * 任务提交时，判断的顺序为 corePoolSize --> workQueue --> maximumPoolSize
     * 如果三者都满了，使用handler处理被拒绝的任务。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_szie_max_size_work_queue);
    }

    /**
     * 任务数量 小于 核心线程
     *
     * @param view
     */
    public void onTask_LT_CoreSize(View view) {
        //如果运行的线程数量 小于 核心线程数.
        //则创建新线程来处理任务，即使线程池中的其他线程是空闲的；

        //此处，任务数量是3，核心线程数是5。运行线程数 小于 核心线程数。
        //可以看到，任务执行没有等待，而是并行执行。
        int taskCount = 3;
        int corePoolSize = 5;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(128);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 任务数量 大于 核心线程，小于最大线程数，工作队列未满
     *
     * @param view
     */
    public void onTask_GT_CoreSize_LT_MaxnumSize_WorkQueue_Not_Full(View view) {
        //如果运行的线程数量 大于 核心线程数量 且小于 最大线程数量，且workQueue未满。
        //不会创建新线程处理，超出核心线程部分加到工作队列中，等待核心线程有空闲再执行；

        //此处，任务数量是5，核心线程数是3，最大线程数是10。运行线程数 大于 核心线程数，小于 最大线程数，且workQueue未满
        //则 超过核心线程数的任务 会被缓存到 workQueue中，待核心线程空闲再执行。
        //可以看到，前3个任务并行执行，后两个任务则被放到了工作队列中，3秒后，前三个任务执行完成核心线程空闲后，才执行。
        int taskCount = 5;
        int corePoolSize = 3;
        int maximumPoolSize = 10;
        long keepAliveTime = 1;
        int workQueueCount = 128;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 任务数量 大于 核心线程，小于最大线程数，工作队列已满
     *
     * @param view
     */
    public void onTask_GT_CoreSize_LT_MaxnumSize_WorkQueue_Full(View view) {
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

    /**
     * 任务数量 大于 最大线程数 工作队列未满
     *
     * @param view
     */
    public void onTask_GT_MaxnumSize_WorkQueue_Not_Full(View view) {
        //如果线程池中的线程数量 大于 最大线程数 且workQueue未满；
        //则将请求放入workQueue中，等待有空闲的线程去从workQueue中取任务并处理，此时仍然只有两个线程在工作；
        //可以看出 当线程数量 大于 核心线程数，并且工作队列未满时，跟最大线程数无关。
        int taskCount = 6;
        int corePoolSize = 2;
        int maximumPoolSize = 4;
        long keepAliveTime = 1;
        int workQueueCount = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 任务数量 大于 最大线程数 工作队列已满
     *
     * @param view
     */
    public void onTask_GT_MaxnumSize_WorkQueue_Full(View view) {
        //如果线程池中的线程数量 大于 最大线程数 且workQueue已满；
        //则通过handler所指定的策略来处理任务；
        //此处，任务数量是10，核心线程数是3。运行线程数 大于 核心线程数（接受3个任务），使用工作队列，工作队列缓存（接受2个任务），
        //最大线程数时5，还可以 接受 5-3 = 2 个任务，超出的8、9、10 任务将被拒绝

        //可以看到，由于默认使用 AbortPolicy
        //抛出了异常，rejected from java.util.concurrent.ThreadPoolExecutor@bf4d680[Running, pool size = 5, active threads = 5, queued tasks = 2, completed tasks = 0]
        //MainActivity (server)' ~ Channel is unrecoverably broken and will be disposed!

        int taskCount = 10;
        int corePoolSize = 3;
        int maximumPoolSize = 5;
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
}
