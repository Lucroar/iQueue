package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.*;
import com.Lucroar.iQueue.Entity.Orders;
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
    public ResponseEntity<?> orderPayment(@RequestBody OrderPaymentDTO payment) {
        return ResponseEntity.ok(cashierMenuService.orderPayment(payment));
    }

    @PostMapping("/done-table")
    public ResponseEntity<?> doneTable(@RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(queueService.doneTable(customerDTO));
    }

    @PostMapping("/enter-queue")
    public ResponseEntity<?> enterQueue(@RequestBody QueueCreationRequest creationRequest) {
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
        if (cashierMenuService.usernameForTakeOutIsExisting(orderDTO.getUsername())){
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Username for take out already exist"));
        }
        if (cashierCartService.checkTableNumber(orderDTO.getUsername()) == 0 && !orderDTO.isTakeOut()){
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Table not assigned yet"));
        }
        Orders orders = cashierCartService.createOrder(orderDTO);
        if (orders == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Username do not exist"));
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> viewAllReservations(){
        return ResponseEntity.ok(cashierMenuService.viewAllReservations());
    }
}
