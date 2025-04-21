package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CategoryImageDTO;
import com.Lucroar.iQueue.Entity.CategoryImage;
import com.Lucroar.iQueue.Repository.CategoryImageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CategoryImageService {
    private final CategoryImageRepository categoryImageRepository;
    private final S3Service s3Service;

    public CategoryImageService(CategoryImageRepository categoryImageRepository,
                                S3Service s3Service) {
        this.categoryImageRepository = categoryImageRepository;
        this.s3Service = s3Service;
    }

    public CategoryImage save(CategoryImageDTO categoryImage) throws IOException {
        String imageURl = s3Service.uploadFile(categoryImage.getImage());
        CategoryImage categoryImageEntity = new CategoryImage(categoryImage.getCategory(), imageURl);
        return categoryImageRepository.save(categoryImageEntity);
    }
}
