package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.CategoryImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryImageRepository extends MongoRepository<CategoryImage, String> {

}
