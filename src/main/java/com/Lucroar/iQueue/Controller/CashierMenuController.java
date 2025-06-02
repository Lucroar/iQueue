package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Service.CashierMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cashier/menu")
public class CashierMenuController {
    private final CashierMenuService cashierMenuService;
    public CashierMenuController(CashierMenuService cashierMenuService) {
        this.cashierMenuService = cashierMenuService;
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
}
