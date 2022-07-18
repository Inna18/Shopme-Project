package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return (List<Brand>) brandRepository.findAll();
    }

    @Override
    public Brand create(Brand brand) {
        Brand savedBrand = brandRepository.save(brand);

        return savedBrand;
    }

    @Override
    public Brand getById(Long id) {
        Brand foundBrand = null;
        try {
            foundBrand = brandRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException("Couldn't find any brand with id " + id);
        }

        return foundBrand;
    }

    @Override
    public Brand update(Brand brand) {
        Brand updatedBrand = brandRepository.save(brand);

        return updatedBrand;
    }

    @Override
    public void deleteById(Long id) {
        try {
            brandRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException("Couldn't find any brand with id " + id);
        }
    }
}
