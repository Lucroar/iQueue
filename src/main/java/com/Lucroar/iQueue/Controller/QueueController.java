package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.QueueCreationRequest;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQueue(@AuthenticationPrincipal Customer customer, @RequestBody QueueCreationRequest queueCreationRequest) {
        if (!queueCreationRequest.getAccessCode().equals(queueService.getAccessCode())){
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Wrong Access Code"));
        }
        if (queueCreationRequest.getNum_people() > 6) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Number of people must not exceed 6"));
        }

        QueueDTO queueDTO = queueService.createQueue(customer, queueCreationRequest);
        if (queueDTO == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Already Created a QueueEntry"));
        }
        return ResponseEntity.ok(queueDTO);
    }

//    @PatchMapping("/enter")
//    public ResponseEntity<?> enterQueue(@RequestBody QueueDTO queue) {
//        return ResponseEntity.ok(queueService.enterQueue(queue));
//    }

    @GetMapping("/check")
    public ResponseEntity<?> checkQueue(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(queueService.checkQueue(customer));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelQueue(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(queueService.cancelQueue(customer));
    }

    @PatchMapping("/done")
    public ResponseEntity<?> doneQueue(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(queueService.finishedQueue(customer));
    }
}
