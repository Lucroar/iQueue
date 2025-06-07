package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.CashierMainMenuDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Service.SeatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/seating")
public class SeatingController {
    private final SeatingService seatingService;

    public SeatingController(SeatingService seatingService) {
        this.seatingService = seatingService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmSeating(@RequestBody CustomerDTO customer) {
        CashierMainMenuDTO mainMenuDTO = seatingService.confirmSeating(customer);
        if (mainMenuDTO == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "No Queue to mark as seated"));
        }
        return ResponseEntity.ok(mainMenuDTO);
    }

    @PostMapping("/missed")
    public ResponseEntity<?> missedSeating(@RequestBody CustomerDTO customer) {
        CashierMainMenuDTO mainMenuDTO = seatingService.missedSeating(customer);
        if (mainMenuDTO == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "No Queue to mark as missed"));
        }
        return ResponseEntity.ok(mainMenuDTO);
    }
}