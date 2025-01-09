package com.github.threading;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class ThreadPoolAndScheduler {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // 假设这是一个自定义的方法，用于搜索键值
    private String searchKey() {
        return DateUtil.now() + " Search result";
    }

    public SseEmitter init() {
        SseEmitter emitter = new SseEmitter(60000L);
        // 连接成功需要返回数据，否则会出现待处理状态
        try {
            emitter.send(SseEmitter.event().comment("success"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void sendEvent(String event) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(event);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception, possibly remove the failed emitter
                emitters.remove(emitter);
            }
        });
    }

    public void runThreadingTasks() {
        // 创建一个更大的线程池，根据系统资源调整大小
        int corePoolSize = 15;
        ExecutorService executorService = new ThreadPoolExecutor(
            corePoolSize,
            corePoolSize * 2,
            10L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(20));

        // 创建一个定时任务调度器
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 定时任务，每10秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
            List<Callable<String>> tasks = new ArrayList<>();
            tasks.add(this::searchKey);
            try {
                // 设置超时时间为 5 秒
                List<Future<String>> results = executorService.invokeAll(tasks, 10, TimeUnit.SECONDS);
                for (Future<String> result : results) {
                    try {
                        if (result.isDone() &&!result.isCancelled()) {
                            sendEvent(result.get());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);

        // 添加ShutdownHook来捕获系统关闭信号
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received shutdown signal. Shutting down...");
            executorService.shutdown();
            scheduler.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.out.println("Pool did not terminate");
                    }
                }
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                    if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.out.println("Scheduler did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                executorService.shutdownNow();
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));
    }
}
