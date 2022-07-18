package com.shopme.admin.brand.service;

import com.shopme.common.entity.Brand;

import java.util.List;

public interface BrandService {

    public List<Brand> getAllBrands();
    public Brand create(Brand brand);
    public Brand getById(Long id);
    public Brand update(Brand brand);
    public void deleteById(Long id);
}
