package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.CashierOrderDTO;
import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.DTO.QueueCreationRequest;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Service.CashierCartService;
import com.Lucroar.iQueue.Service.CashierMenuService;
import com.Lucroar.iQueue.Service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/cashier/menu")
public class CashierMenuController {
    private final CashierMenuService cashierMenuService;
    private final QueueService queueService;
    private final CashierCartService cashierCartService;

    public CashierMenuController(CashierMenuService cashierMenuService, QueueService queueService, CashierCartService cashierCartService) {
        this.cashierMenuService = cashierMenuService;
        this.queueService = queueService;
        this.cashierCartService = cashierCartService;
    }

    @GetMapping("/main")
    public ResponseEntity<?> viewListOfTables(){
        return ResponseEntity.ok(cashierMenuService.viewListOfTables());
    }

    @GetMapping("/unpaid")
    public ResponseEntity<?> viewListOfTablesUnpaid() {
        return ResponseEntity.ok(cashierMenuService.viewAllUnpaid());
    }

    @PostMapping("/payment")
    public ResponseEntity<?> orderPayment(@RequestBody OrdersHistoryDTO history) {
        return ResponseEntity.ok(cashierMenuService.orderPayment(history));
    }

    @PostMapping("/enter-queue")
    public ResponseEntity<?> enterQueue(@RequestBody QueueCreationRequest creationRequest) {
        if (!creationRequest.getAccessCode().equals(queueService.getAccessCode())){
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Wrong Access Code"));
        }
        if (creationRequest.getNum_people() > 6) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Number of people must not exceed 6"));
        }
        QueueDTO queueDTO = queueService.cashierCreateQueue(creationRequest);
        if (queueDTO == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Already Created an Entry with the same username"));
        }
        return ResponseEntity.ok(queueDTO);
    }

    @PostMapping("/add-order")
    public ResponseEntity<?> order(@RequestBody CashierOrderDTO orderDTO) {
        return ResponseEntity.ok(cashierCartService.createOrder(orderDTO));
    }
}
