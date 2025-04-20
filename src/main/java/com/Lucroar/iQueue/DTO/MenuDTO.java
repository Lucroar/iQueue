package com.Lucroar.iQueue.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuDTO {
    private String name;
    private String category;
    private MultipartFile img_url;
    private String description;
    private int price;
}
