package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.ReturnRequestDTO;
import com.Lucroar.iQueue.DTO.TableOrderDTO;
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
    public ResponseEntity<?> orderServed(@RequestBody TableOrderDTO orderDTO){
        return ResponseEntity.ok(kitchenService.orderServed(orderDTO));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(){
        return ResponseEntity.ok(kitchenService.viewAllOrders());
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnOrder(@RequestBody ReturnRequestDTO returnRequest){
        return ResponseEntity.ok(kitchenService.resolveReturningOrder(returnRequest.getId(), returnRequest.getAction()));
    }
}
