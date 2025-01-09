package com.github.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.entity.PriceMonitorMsg;
import com.github.entity.ResponseResult;
import com.github.entity.SearchRequest;
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
public class PriceMonitorController {

    private final RedisTemplate<String, Object> redisTemplate;


    public PriceMonitorController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 价格变更记录页面
     */
    @RequestMapping("priceMonitor/list")
    public String list() {
        return "/priceMonitor/list";
    }


    @RequestMapping("priceMonitor/listData")
    @ResponseBody
    public ResponseResult<?> listData() {
        String json = (String) redisTemplate.opsForValue().get("priceMonitorMsgList");
        List<PriceMonitorMsg> priceMonitorMsgList = Optional.ofNullable(JSONObject.parseObject(json, new TypeReference<List<PriceMonitorMsg>>() {
        })).orElseGet(ArrayList::new);
        return ResponseResult.success(priceMonitorMsgList);
    }
}

