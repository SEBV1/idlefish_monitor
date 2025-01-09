package com.github.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.entity.PriceMonitorMsg;
import com.github.entity.SearchView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class PriceMonitorService {


    private final RedisTemplate<String, Object> redisTemplate;

    public PriceMonitorService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void comparePrices(List<SearchView> searchViewList) {
        String priceMonitors = (String) redisTemplate.opsForValue().get("priceMonitors");
        if (StringUtils.isNotEmpty(priceMonitors)) {
            List<SearchView> priceMonitorList = parsePriceMonitors(priceMonitors);
            if (priceMonitorList != null) {
                Map<Long, SearchView> priceMonitorMap = buildPriceMonitorMap(priceMonitorList);
                searchViewList.forEach(searchView -> {
                    SearchView priceMonitor = priceMonitorMap.get(searchView.getItemId());
                    if (priceMonitor != null) {
                        comparePricesAndLog(priceMonitor, searchView, priceMonitorList);
                    }
                });
            }
        }
    }

    /**
     * 解析json数据
     */
    private List<SearchView> parsePriceMonitors(String priceMonitors) {
        try {
            return JSONObject.parseObject(priceMonitors, new TypeReference<List<SearchView>>() {
            });
        } catch (Exception e) {
            log.error("解析 priceMonitors JSON 字符串失败", e);
            return null;
        }
    }

    private Map<Long, SearchView> buildPriceMonitorMap(List<SearchView> priceMonitorList) {
        Map<Long, SearchView> map = new HashMap<>();
        for (SearchView priceMonitor : priceMonitorList) {
            map.put(priceMonitor.getItemId(), priceMonitor);
        }
        return map;
    }

    private void comparePricesAndLog(SearchView priceMonitor, SearchView searchView, List<SearchView> priceMonitorList) {
        BigDecimal monitorPrice = priceMonitor.getPrice();
        BigDecimal searchViewPrice = searchView.getPrice();
        int compareTo = monitorPrice.compareTo(searchViewPrice);

        if (compareTo != 0) {
            String finalNotice = generateNotice(compareTo, monitorPrice, searchViewPrice);
            priceMonitorList.stream().filter(item -> item.getItemId().equals(searchView.getItemId())).findFirst()
                .ifPresent(item -> {
                    int indexOf = priceMonitorList.indexOf(item);
                    if (indexOf != -1) {
                        savePriMonitorMsg(searchViewPrice, monitorPrice, finalNotice, searchView.getTitle(), searchView.getItemId());
                        priceMonitorList.set(indexOf, searchView);
                    }
                });
        }
        String json = JSONObject.toJSONString(priceMonitorList);
        redisTemplate.opsForValue().set("priceMonitors", json);
    }

    @NotNull
    private static String generateNotice(int compareTo, BigDecimal monitorPrice, BigDecimal searchViewPrice) {
        String notice = "";
        if (compareTo > 0) {
            notice = notice + "📉降价啦！原价:" + monitorPrice + ",现价:" + searchViewPrice;
        } else if (compareTo < 0) {
            notice = notice + "📈涨价啦！原价:" + monitorPrice + ",现价:" + searchViewPrice;
        }
        return notice;
    }

    /**
     * 保存监控价格变更记录
     */
    private void savePriMonitorMsg(BigDecimal searchViewPrice, BigDecimal monitorPrice, String finalNotice, String title, long itemId) {
        List<PriceMonitorMsg> priceMonitorMsgList = getPriceMonitorMsgListFromRedis().orElseGet(ArrayList::new);

        PriceMonitorMsg priceMonitorMsg = PriceMonitorMsg.builder().currentPrice(searchViewPrice).
            originalPrice(monitorPrice).msg(finalNotice).updateTime(System.currentTimeMillis() / 1000).title(title).itemId(itemId).build();

        priceMonitorMsgList.add(priceMonitorMsg);
        redisTemplate.opsForValue().set("priceMonitorMsgList", JSONObject.toJSONString(priceMonitorMsgList));
    }


    private Optional<List<PriceMonitorMsg>> getPriceMonitorMsgListFromRedis() {
        String json = (String) redisTemplate.opsForValue().get("priceMonitorMsgList");
        return Optional.ofNullable(JSONObject.parseObject(json, new TypeReference<List<PriceMonitorMsg>>() {
        }));
    }

}
