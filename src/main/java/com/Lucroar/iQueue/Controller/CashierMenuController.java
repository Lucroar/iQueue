package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Service.CashierMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
