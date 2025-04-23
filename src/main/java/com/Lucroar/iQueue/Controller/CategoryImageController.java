package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.CategoryImageDTO;
import com.Lucroar.iQueue.Service.CategoryImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/category")
public class CategoryImageController {
    private final CategoryImageService categoryImageService;

    public CategoryImageController(CategoryImageService categoryImageService) {
        this.categoryImageService = categoryImageService;
    }

    @PutMapping("/create")
    public ResponseEntity<?> newCategoryImage(@RequestBody CategoryImageDTO categoryImage) throws IOException {
        return ResponseEntity.ok(categoryImageService.createCategory(categoryImage));
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewCategoryImage(){
        return ResponseEntity.ok(categoryImageService.getAllCategoryImages());
    }
}
