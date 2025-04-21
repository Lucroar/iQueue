package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.MenuDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Menu")
@Data
@NoArgsConstructor
public class Menu {
    @Id
    private String menu_id;
    private String name;
    private Category category;
    private String img_url;
    private String description;
    private int price;

    public Menu(MenuDTO menu, String img_url) {
        this.name = menu.getName();
        this.category = menu.getCategory();
        this.img_url = img_url;
        this.description = menu.getDescription();
        this.price = menu.getPrice();
    }
}