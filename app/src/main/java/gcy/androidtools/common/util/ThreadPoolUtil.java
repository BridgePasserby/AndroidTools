package gcy.androidtools.common.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2018, 北京视达科科技有限责任公司 All rights reserved.
 * author：chongyang.gao
 * date：2018/12/17
 * description：
 */

public class ThreadPoolUtil {

    // mNormalPool 访问网络用的，
    private static ThreadPoolProxy mNormalPool   = new ThreadPoolProxy(1, 3, 5 * 1000);
    //mDownloadPool 是下载用的
    private static ThreadPoolProxy mDownloadPool = new ThreadPoolProxy(3, 3, 5 * 1000);
    //得到安卓一般配置的线程池
    private static ThreadPoolProxy mCommonPool   = new ThreadPoolProxy();

    public static ThreadPoolProxy getNormalPool() {
        return mNormalPool;
    }

    public static ThreadPoolProxy getDownloadPool() {
        return mDownloadPool;
    }

    public static ThreadPoolProxy getCommonPool() {
        return mCommonPool;
    }

    public static class ThreadPoolProxy {
        private final int mCorePoolSize;     // 核心线程数
        private final int mMaximumPoolSize;  // 最大线程数
        private final long mKeepAliveTime;    // 所有任务执行完毕后普通线程回收的时间间隔
        private ThreadPoolExecutor mPool;  // 代理对象内部保存的是原来类的对象

        /**
         * 可自行配制的构造器
         * @param corePoolSize
         * @param maximumPoolSize
         * @param keepAliveTime
         */
        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.mCorePoolSize = corePoolSize;
            this.mMaximumPoolSize = maximumPoolSize;
            this.mKeepAliveTime = keepAliveTime;
            if (mPool == null || mPool.isShutdown()) {
                //int corePoolSize = 1;//核心线程池大小
                //int maximumPoolSize = 3;//最大线程池大小
                //long keepAliveTime = 5 * 1000;//保持存活的时间
                TimeUnit unit = TimeUnit.MILLISECONDS;//单位
                BlockingQueue<Runnable> workQueue = null;//阻塞队列
                workQueue = new ArrayBlockingQueue<Runnable>(3);//FIFO,大小有限制，为3个
                //workQueue = new LinkedBlockingQueue();  //队列类型为linked，其大小不定，无限大小
                //workQueue = new PriorityBlockingQueue();
                ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂
                RejectedExecutionHandler handler = null;//异常捕获器
                //handler = new ThreadPoolExecutor.DiscardOldestPolicy();//去掉队列中首个任务，将新加入的放到队列中去
                //handler = new ThreadPoolExecutor.AbortPolicy();//触发异常
                //handler = new ThreadPoolExecutor.CallerRunsPolicy();//直接执行，不归线程池控制,在调用线程中执行
                handler = new ThreadPoolExecutor.DiscardPolicy();//不做任何处理
                // 创建线程池
                mPool = new ThreadPoolExecutor(mCorePoolSize,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        unit,
                        workQueue,
                        threadFactory,
                        handler);
            }
        }

        //安卓通常的配置方式
        public ThreadPoolProxy() {
            this.mCorePoolSize = Runtime.getRuntime().availableProcessors();
            this.mMaximumPoolSize = mCorePoolSize * 2;
            this.mKeepAliveTime = 1;
            BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
            ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂
            RejectedExecutionHandler handler = null;//异常捕获器
            handler = new ThreadPoolExecutor.DiscardPolicy();//不做任何处理
            mPool = new ThreadPoolExecutor(mCorePoolSize,
                    mMaximumPoolSize,
                    mKeepAliveTime,
                    TimeUnit.SECONDS,
                    taskQueue, threadFactory, handler);
        }

        /**
         * 执行任务
         * @param task
         */
        public void execute(Runnable task) {
            //执行任务
            mPool.execute(task);
        }

        // 提交任务
        public Future<?> submit(Runnable task) {
            return mPool.submit(task);
        }

        // 取消任务
        public void remove(Runnable task) {
            if (mPool != null && !mPool.isShutdown()) {
                mPool.getQueue().remove(task);
            }
        }
    }

}
