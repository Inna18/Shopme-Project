package com.shopme.admin.brand.service;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {

    List<Brand> getAllBrands();
    Page<Brand> getBrandsByPage(int pageIndex, String sortField, String sortDir, String keyword);
    Brand save(Brand brand);
    Brand getById(Long id);
    Brand update(Brand brand);
    void deleteById(Long id);
    String checkUnique(Long id, String name);
}
