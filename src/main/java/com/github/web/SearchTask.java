package com.github.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SearchTask {

    // 模拟搜索方法，返回搜索结果
    private static String search(String keyword) {
        System.out.println("Searching for: " + keyword);
        // 这里可以添加实际的搜索逻辑，例如发送HTTP请求
        return "Result for " + keyword;
    }

    public static void main(String[] args) {
        // 创建一个更大的线程池，根据系统资源调整大小
//        int corePoolSize = 40;
//        ExecutorService executorService = new ThreadPoolExecutor(
//            corePoolSize,
//            corePoolSize,
//            0L,
//            TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<>());
//
//        // 创建一个定时任务调度器
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        // 定义搜索关键词列表
//        String[] keywords = {"苹果"};
//
//        // 定时任务，每10秒执行一次
//        scheduler.scheduleAtFixedRate(() -> {
//            List<Callable<String>> tasks = new ArrayList<>();
//            for (String keyword : keywords) {
//                tasks.add(() -> search(keyword));
//            }
//            try {
//                List<Future<String>> results = executorService.invokeAll(tasks);
//                for (Future<String> result : results) {
//                    try {
//                        System.out.println(result.get());
//                    } catch (InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 0, 10, TimeUnit.SECONDS);
//
//        // 添加ShutdownHook来捕获系统关闭信号
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Received shutdown signal. Shutting down...");
//            executorService.shutdown();
//            scheduler.shutdown();
//            try {
//                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                    executorService.shutdownNow();
//                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                        System.out.println("Pool did not terminate");
//                    }
//                }
//                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
//                    scheduler.shutdownNow();
//                    if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
//                        System.out.println("Scheduler did not terminate");
//                    }
//                }
//            } catch (InterruptedException ie) {
//                executorService.shutdownNow();
//                scheduler.shutdownNow();
//                Thread.currentThread().interrupt();
//            }
//        }));

    }
}
