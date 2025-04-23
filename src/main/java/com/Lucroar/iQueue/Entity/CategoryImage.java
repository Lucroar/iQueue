package com.Lucroar.iQueue.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CategoryImage")
@Data
@NoArgsConstructor
public class CategoryImage {
    @Id
    private String category_id;
    private Category category;
    private String imageURL;

    public CategoryImage(Category category, String image) {
        this.category = category;
        this.imageURL = image;
    }
}
