package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryImageDTO {
    private Category category;
    private MultipartFile image;
}
