package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Category;
import com.Lucroar.iQueue.Entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    List<Menu> findByCategory(Category category);
}
