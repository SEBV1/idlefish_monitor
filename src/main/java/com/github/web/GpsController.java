package com.github.web;

import com.alibaba.fastjson.JSONObject;
import com.github.entity.Gps;
import com.github.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class GpsController {

    private final RedisTemplate<String, Object> redisTemplate;

    public GpsController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * gps编辑页面
     */
    @RequestMapping("/gps/edit")
    public String edit(Model model) {
        String json = (String) redisTemplate.opsForValue().get("gps");
        Gps gps = new Gps();
        if (json != null) {
            gps = JSONObject.parseObject(json, Gps.class);
        }
        model.addAttribute("gps", gps);
        return "/gps/edit";
    }

    /**
     * gps保存
     *
     */
    @RequestMapping("/gps/save")
    @ResponseBody
    public ResponseResult<?> save(Gps gps) {
        redisTemplate.opsForValue().set("gps", JSONObject.toJSONString(gps));
        return ResponseResult.success();
    }
}
