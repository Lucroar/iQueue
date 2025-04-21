package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.MenuDTO;
import com.Lucroar.iQueue.Entity.Category;
import com.Lucroar.iQueue.Entity.Menu;
import com.Lucroar.iQueue.Repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final S3Service s3Service;

    public MenuService(MenuRepository menuRepository, S3Service s3Service) {
        this.menuRepository = menuRepository;
        this.s3Service = s3Service;
    }

    public Menu addMenu(MenuDTO menuDTO) throws IOException {
        String img_url = s3Service.uploadFile(menuDTO.getImg_url());
        Menu menu = new Menu(menuDTO, img_url);
        return menuRepository.save(menu);
    }

    public List<Menu> getMenuByCategory(Category category) {
        return menuRepository.findByCategory(category);
    }
}
