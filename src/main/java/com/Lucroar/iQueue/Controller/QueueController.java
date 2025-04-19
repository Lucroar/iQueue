package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Queue;
import com.Lucroar.iQueue.Service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQueue(@AuthenticationPrincipal Customer customer, @RequestBody Queue queue) {
        return ResponseEntity.ok(queueService.createQueue(customer, queue));
    }

    @PatchMapping("/enter")
    public ResponseEntity<?> enterQueue(@RequestBody QueueDTO queue) {
        return ResponseEntity.ok(queueService.enterQueue(queue));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkQueue(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(queueService.checkQueue(customer));
    }
}
