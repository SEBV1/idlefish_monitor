package com.github.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.entity.SearchView;
import com.github.entity.SendEventMsg;
import com.github.threading.SearchTask;
import com.github.websocket.Emitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {


    private static final int THREAD_POOL_SIZE = 10;
    private static final long INTERVAL = 30000;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    final Emitter emitter;

    final JNICLibraryV7_18_92ServiceWorker jnicLibraryV7_18_92ServiceWorker;

    private final RedisTemplate<String, Object> redisTemplate;
    private final PriceMonitorService priceMonitorService;

    public SearchService(Emitter emitter, JNICLibraryV7_18_92ServiceWorker jnicLibraryV7_18_92ServiceWorker, RedisTemplate<String, Object> redisTemplate, PriceMonitorService priceMonitorService) {
        this.emitter = emitter;
        this.jnicLibraryV7_18_92ServiceWorker = jnicLibraryV7_18_92ServiceWorker;
        this.redisTemplate = redisTemplate;
        this.priceMonitorService = priceMonitorService;
    }

    @Scheduled(fixedRate = INTERVAL)
    public void searchWords() {
        List<SearchTask> tasks = new ArrayList<>();

        tasks.add(new SearchTask(jnicLibraryV7_18_92ServiceWorker, redisTemplate));
        try {
            List<Future<List<SearchView>>> futures = executorService.invokeAll(tasks);
            List<SearchView> all = new ArrayList<>();
            for (Future<List<SearchView>> future : futures) {
                try {
                    List<SearchView> searchViewList = future.get();
                    all.addAll(searchViewList);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("InterruptedException | ExecutionException", e);
                }
            }

            List<SearchView> searchViewList = getSearchViewList();
            priceMonitorService.comparePrices(searchViewList);

            List<SearchView> unequalData = findUnequalData(all, searchViewList);
            saveData(unequalData, searchViewList);

            String msg = JSONObject.toJSONString(SendEventMsg.builder().code(1).data(searchViewList).build());
            emitter.sendEvent(msg);
            redisTemplate.opsForValue().set("products", JSONObject.toJSONString(searchViewList));
        } catch (InterruptedException e) {
            log.error("error", e);
        }
    }


    private List<SearchView> getSearchViewList() {
        String products = (String) redisTemplate.opsForValue().get("products");
        return products == null ? new ArrayList<>() : JSONObject.parseObject(products, new TypeReference<List<SearchView>>() {
        });
    }

    /**
     * 找出不同数据
     */
    private static List<SearchView> findUnequalData(List<SearchView> searchViewNewDataList, List<SearchView> searchViewOldDataList) {
        Set<Long> idSet = searchViewOldDataList.stream()
            .map(SearchView::getItemId)
            .collect(Collectors.toSet());
        return searchViewNewDataList.stream()
            .filter(obj -> !idSet.contains(obj.getItemId()))
            .map(obj -> SearchView.builder().itemId(obj.getItemId()).
                title(obj.getTitle()).area(obj.getArea()).price(obj.getPrice()).
                keyword(obj.getKeyword()).picUrl(obj.getPicUrl()).publishTime(obj.getPublishTime()).
                userNickName(obj.getUserNickName()).build())
            .collect(Collectors.toList());
    }

    /**
     * 保存不同数据
     */
    private void saveData(List<SearchView> dataList, List<SearchView> searchViewOldDataList) {

        List<SearchView> newDataList = dataList.stream()
            .filter(newData -> searchViewOldDataList.stream()
                .noneMatch(oldData -> oldData.getItemId().equals(newData.getItemId())))
            .collect(Collectors.toList());

        if (!newDataList.isEmpty()) {
            newDataList.forEach(newData -> {
                log.info(newData.toString());
                searchViewOldDataList.add(0, newData);
            });
            String msg = JSONObject.toJSONString(SendEventMsg.builder().code(2).build());
            emitter.sendEvent(msg);
        }
        searchViewOldDataList.sort(Comparator.comparing(SearchView::getPublishTime).reversed());
    }
}
