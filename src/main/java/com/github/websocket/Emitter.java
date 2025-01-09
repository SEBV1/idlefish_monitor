package com.github.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Emitter {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

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
}
