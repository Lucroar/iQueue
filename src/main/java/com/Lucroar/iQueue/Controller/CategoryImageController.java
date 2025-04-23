package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.CategoryImageDTO;
import com.Lucroar.iQueue.Service.CategoryImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController()
@RequestMapping("/category")
public class CategoryImageController {
    private final CategoryImageService categoryImageService;

    public CategoryImageController(CategoryImageService categoryImageService) {
        this.categoryImageService = categoryImageService;
    }

    @PostMapping ("/add")
    public ResponseEntity<?> addCategoryImage(@ModelAttribute CategoryImageDTO categoryImage) throws IOException {
        return ResponseEntity.ok(categoryImageService.addCategory(categoryImage));
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewCategoryImage(){
        return ResponseEntity.ok(categoryImageService.getAllCategoryImages());
    }
}
