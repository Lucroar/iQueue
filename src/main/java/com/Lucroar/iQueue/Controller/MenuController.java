package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.MenuDTO;
import com.Lucroar.iQueue.Entity.Category;
import com.Lucroar.iQueue.Service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMenu(@ModelAttribute MenuDTO menuDTO) throws IOException {
        return ResponseEntity.ok(menuService.addMenu(menuDTO));
    }

    @GetMapping("/view/{category}")
    public ResponseEntity<?> viewMenu(@PathVariable Category category) {
        return ResponseEntity.ok(menuService.getMenuByCategory(category));
    }

    @GetMapping("/item/{menuId}")
    public ResponseEntity<?> viewItem(@PathVariable String menuId){
        return ResponseEntity.ok(menuService.findById(menuId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> allMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }
}
