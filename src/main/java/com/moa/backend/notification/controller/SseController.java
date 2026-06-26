package com.moa.backend.notification.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.ResponseEntity;
import com.moa.backend.common.config.jwt.JwtProvider;
import com.moa.backend.notification.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import java.io.IOException;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {
	private final SseEmitterRepository sseEmitterRepository;
	private final JwtProvider jwtProvider;
	@GetMapping("/connect")
	public ResponseEntity<?> connect(@RequestParam("token") String token) {
	    Long memberId = jwtProvider.getMemberId(token);
	    if(memberId == null) {
	        return ResponseEntity.status(401).build();
	    }
	    SseEmitter sseEmitter = new SseEmitter(60 * 60 * 1000L);
	    sseEmitterRepository.add(memberId, sseEmitter);
	    try {
	        sseEmitter.send(SseEmitter.event().name("connect").data("connected"));
	    } catch(IOException e) {
	        sseEmitterRepository.remove(memberId);
	    }
	    return ResponseEntity.ok(sseEmitter);
	}
}
