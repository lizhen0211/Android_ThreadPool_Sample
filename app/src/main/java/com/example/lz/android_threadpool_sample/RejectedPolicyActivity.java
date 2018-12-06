package com.example.lz.android_threadpool_sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectedPolicyActivity extends Activity {


    /**
     * 线程池的拒绝策略，是指当任务添加到线程池中被拒绝，而采取的处理措施。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_policy);
    }

    /**
     * 丢弃任务并抛出RejectedExecutionException异常
     *
     * @param view
     */
    public void onAbortPolicyClick(View view) {
        //此处，只能执行前7个任务，8、9、10 任务将被拒绝
        int taskCount = 10;
        int corePoolSize = 3;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 当任务添加到线程池中被拒绝时，线程池将丢弃被拒绝的任务。
     *
     * @param view
     */
    public void onDiscardPolicyClick(View view) {
        //此处，只能执行前7个任务，8、9、10 任务将被拒绝
        //但是并不会抛出异常
        int taskCount = 10;
        int corePoolSize = 3;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.DiscardPolicy());
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 当任务添加到线程池中被拒绝时，线程池会放弃等待队列中最旧的未处理任务，然后将被拒绝的任务添加到等待队列中。
     *
     * @param view
     */
    public void onDiscardOldestPolicyClick(View view) {
        //此处，前7个任务会被执行，最后的任务会被执行。其他任务被抛弃
        int taskCount = 10;
        int corePoolSize = 3;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    /**
     * 当任务添加到线程池中被拒绝时，会在线程池当前正在运行的Thread线程池中处理被拒绝的任务。
     *
     * @param view
     */
    public void onCallerRunsPolicyClick(View view) {
        //此处，前7个任务会被执行，第8个任务（被抛弃的任务）可能在被调用线程（主线程）执行
        int taskCount = 10;
        int corePoolSize = 3;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }

    public void onRejectedExecutionHandlerClick(View view) {
        int taskCount = 10;
        int corePoolSize = 3;
        int maximumPoolSize = 5;
        long keepAliveTime = 1;
        int workQueueCount = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(workQueueCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //移除任务
                executor.remove(r);
                MyRunnable myTask = (MyRunnable) r;
                int index = myTask.getIndex();
                Log.e(MyRunnable.class.getSimpleName(), "第" + index + "被拒绝后移除");
            }
        });
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new MyRunnable(i));
            Log.e(MyRunnable.class.getSimpleName(), "活跃线程数量" + executor.getActiveCount() + "");
        }
    }
}
