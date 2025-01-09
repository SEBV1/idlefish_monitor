package com.github.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.entity.ResponseResult;
import com.github.entity.SearchView;
import com.github.websocket.Emitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class SearchController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Emitter emitter;

    public SearchController(RedisTemplate<String, Object> redisTemplate, Emitter emitter) {
        this.redisTemplate = redisTemplate;
        this.emitter = emitter;
    }

    /**
     * 实时搜索页面
     */
    @RequestMapping("/search/list")
    public String list() {
        return "/search/list";
    }

    @GetMapping(value = "/search/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return emitter.init();
    }


    @RequestMapping("/search/listData")
    @ResponseBody
    public ResponseResult<?> listData() {
        String json = (String) redisTemplate.opsForValue().get("products");
        List<SearchView> searchViews = null;
        if (StringUtils.isNotEmpty(json)) {
            searchViews = JSONObject.parseObject(json, new TypeReference<List<SearchView>>() {
            });
        }
        return ResponseResult.success(searchViews);
    }

    @RequestMapping("/search/clear")
    @ResponseBody
    public ResponseResult<?> clear() {
        redisTemplate.delete("products");
        redisTemplate.delete("priceMonitors");
        redisTemplate.delete("priceMonitorMsgList");
        return ResponseResult.success();
    }

    @RequestMapping("/search/priceMonitor")
    @ResponseBody
    public ResponseResult<?> priceMonitor(SearchView searchView) {
        List<SearchView> priceMonitorSearchViews = getPriceMonitorSearchViews();
        boolean anyMatch = priceMonitorSearchViews.stream().anyMatch(item -> item.getItemId().equals(searchView.getItemId()));
        if (anyMatch) {
            return ResponseResult.failure("重复监控");
        }
        priceMonitorSearchViews.add(searchView);
        priceMonitorSearchViewsToRedis(priceMonitorSearchViews);
        return ResponseResult.success();
    }

    private void priceMonitorSearchViewsToRedis(List<SearchView> searchViewList) {
        String json = JSONObject.toJSONString(searchViewList);
        redisTemplate.opsForValue().set("priceMonitors", json);
    }

    private List<SearchView> getPriceMonitorSearchViews() {
        return Optional.ofNullable(redisTemplate.opsForValue().get("priceMonitors"))
            .map(value -> (String) value)
            .filter(StringUtils::isNotEmpty).map(priceMonitors -> JSONObject.parseObject(priceMonitors, new TypeReference<List<SearchView>>() {
            })).orElseGet(ArrayList::new);
    }

}

