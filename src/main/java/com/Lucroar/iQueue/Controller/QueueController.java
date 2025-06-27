package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.QueueCreationRequest;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Service.LastSeatedService;
import com.Lucroar.iQueue.Service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;
    private final LastSeatedService lastSeatedService;

    public QueueController(QueueService queueService, LastSeatedService lastSeatedService) {
        this.queueService = queueService;
        this.lastSeatedService = lastSeatedService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQueue(@AuthenticationPrincipal Customer customer, @RequestBody QueueCreationRequest queueCreationRequest) {
        if (queueService.existingOrderHistory(customer)){
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Can't enter queue. You have an unpaid order"));
        }
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

    @GetMapping("/check")
    public ResponseEntity<?> checkQueue(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(queueService.checkQueue(customer));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelQueue(@AuthenticationPrincipal Customer customer) {
        QueueDTO queueDTO = queueService.cancelQueue(customer);
        if (queueDTO == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "No Waiting queue to Cancel"));
        }
        return ResponseEntity.ok(queueDTO);
    }

    @PatchMapping("/done")
    public ResponseEntity<?> doneQueue(@AuthenticationPrincipal Customer customer) {
        QueueDTO queueDTO = queueService.finishedQueue(customer);
        if (queueDTO == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "No Seated queue to bill out"));
        }
        return ResponseEntity.ok(queueDTO);
    }

    @GetMapping("/last-seated/{tier}")
    public ResponseEntity<?> getLastSeated(@PathVariable int tier) {
        return ResponseEntity.ok(lastSeatedService.getLastSeatedByTier(tier));
    }
}
