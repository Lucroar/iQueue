package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuDTO {
    private String name;
    private Category category;
    private MultipartFile img_url;
    private String description;
    private int price;
}
