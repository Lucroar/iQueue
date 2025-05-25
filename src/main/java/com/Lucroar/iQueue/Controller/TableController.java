package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/table")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/new-table")
    public ResponseEntity<?> newTable(@RequestBody Table table) {
        return ResponseEntity.ok(tableService.addTable(table));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTable());
    }
}
