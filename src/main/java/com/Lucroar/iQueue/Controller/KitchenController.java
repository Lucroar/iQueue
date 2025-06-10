package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Service.KitchenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kitchen")
public class KitchenController {
    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @PostMapping("/serve")
    public ResponseEntity<?> orderServed(@RequestBody Table table){
        return ResponseEntity.ok(kitchenService.orderServed(table.getTableNumber()));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(){
        return ResponseEntity.ok(kitchenService.viewAllOrders());
    }
}
