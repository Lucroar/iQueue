package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/insert-queue")
    public ResponseEntity<?> insertQueue(@AuthenticationPrincipal Jwt jwt, @RequestBody QueueDTO queue) {
        return ResponseEntity.ok(queueService.insertToQueue(jwt, queue));
    }
}
