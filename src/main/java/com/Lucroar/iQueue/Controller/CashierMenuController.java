package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.DTO.QueueCreationRequest;
import com.Lucroar.iQueue.Service.CashierMenuService;
import com.Lucroar.iQueue.Service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cashier/menu")
public class CashierMenuController {
    private final CashierMenuService cashierMenuService;
    private final QueueService queueService;

    public CashierMenuController(CashierMenuService cashierMenuService, QueueService queueService) {
        this.cashierMenuService = cashierMenuService;
        this.queueService = queueService;
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
        return ResponseEntity.ok(queueService.cashierCreateQueue(creationRequest));
    }
}
