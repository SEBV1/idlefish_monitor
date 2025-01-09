package com.github.service;

import com.github.config.UnidbgProperties;
import com.github.unidbg.worker.Worker;
import com.github.unidbg.worker.WorkerPool;
import com.github.unidbg.worker.WorkerPoolFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class JNICLibraryV7_18_92ServiceWorker extends Worker {

    private UnidbgProperties unidbgProperties;
    private WorkerPool pool;
    private JNICLibraryV7_18_92Service jnicLibraryV7_18_92Service;

    @Autowired
    public void init(UnidbgProperties unidbgProperties) {
        this.unidbgProperties = unidbgProperties;
    }

    public JNICLibraryV7_18_92ServiceWorker() {
        super(WorkerPoolFactory.create(JNICLibraryV7_18_92ServiceWorker::new, Runtime.getRuntime().availableProcessors()));
    }

    public JNICLibraryV7_18_92ServiceWorker(WorkerPool pool) {
        super(pool);
    }

    @Autowired
    public JNICLibraryV7_18_92ServiceWorker(UnidbgProperties unidbgProperties,
                                            @Value("${spring.task.execution.pool.core-size:4}") int poolSize) {
        super(WorkerPoolFactory.create(JNICLibraryV7_18_92ServiceWorker::new, Runtime.getRuntime().availableProcessors()));
        this.unidbgProperties = unidbgProperties;
        if (this.unidbgProperties.isAsync()) {
            pool = WorkerPoolFactory.create(pool -> new JNICLibraryV7_18_92ServiceWorker(unidbgProperties.isDynarmic(),
                unidbgProperties.isVerbose(), pool), Math.max(poolSize, 4));
            log.info("线程池为:{}", Math.max(poolSize, 4));
        } else {
            this.jnicLibraryV7_18_92Service = new JNICLibraryV7_18_92Service(unidbgProperties);
        }
    }

    public JNICLibraryV7_18_92ServiceWorker(boolean dynarmic, boolean verbose, WorkerPool pool) {
        super(pool);
        this.unidbgProperties = new UnidbgProperties();
        unidbgProperties.setDynarmic(dynarmic);
        unidbgProperties.setVerbose(verbose);
        log.info("是否启用动态引擎:{},是否打印详细信息:{}", dynarmic, verbose);
        this.jnicLibraryV7_18_92Service = new JNICLibraryV7_18_92Service(unidbgProperties);
    }

    @Async
    @SneakyThrows
    public CompletableFuture<Map<String, String>> doCommandNative_70102(String appKey, String input, String api) {

        JNICLibraryV7_18_92ServiceWorker worker;
        Map<String, String> data;
        if (this.unidbgProperties.isAsync()) {
            while (true) {
                if ((worker = pool.borrow(2, TimeUnit.SECONDS)) == null) {
                    continue;
                }
                data = worker.doWork(appKey, input, api);
                pool.release(worker);
                break;
            }
        } else {
            synchronized (this) {
                data = this.doWork(appKey, input, api);
            }
        }
        return CompletableFuture.completedFuture(data);
    }

    private Map<String, String> doWork(String appKey, String input, String api) {
        return jnicLibraryV7_18_92Service.doCommandNative_70102(appKey, input, api);
    }

    @SneakyThrows
    @Override
    public void destroy() {
        jnicLibraryV7_18_92Service.destroy();
    }
}
