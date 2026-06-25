package com.moa.backend.notification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void add(Long memberId, SseEmitter emitter) {
        emitter.onCompletion(() -> remove(memberId));
        emitter.onTimeout(() -> remove(memberId));
        emitter.onError((e) -> remove(memberId));
        emitters.put(memberId, emitter);
    }

    public void remove(Long memberId) {
        emitters.remove(memberId);
    }

    public void sendNotification(Long memberId, String content) {
        SseEmitter emitter = emitters.get(memberId);
        if(emitter != null) {
            try {
                emitter.send(content);
            } catch(IOException e) {
                remove(memberId);
            }
        }
    }
}