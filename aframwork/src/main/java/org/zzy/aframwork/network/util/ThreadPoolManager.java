package org.zzy.aframwork.network.util;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private ExecutorService service;

    private ThreadPoolManager(){
        int num = Runtime.getRuntime().availableProcessors();//cpu 密集型任务，num*2
        service = Executors.newFixedThreadPool(num*2+2); //io密集型 多大才好？需要实际测试得出结果，需要根据task的数量级做出调整
    }

    private static final ThreadPoolManager manager= new ThreadPoolManager();

    public static ThreadPoolManager getInstance(){
        return manager;
    }

    public void addTask(Runnable runnable){
        try {
            service.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("线程池满了，请稍后再试！");
        }
    }
}
