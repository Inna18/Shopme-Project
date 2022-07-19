package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return (List<Brand>) brandRepository.findAll();
    }

    @Override
    public Page<Brand> getBrandsByPage(int pageIndex, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageIndex - 1, PAGE_SIZE, sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand save(Brand brand) {
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
        Long countById = brandRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Counldn't find any brand with id - " + id);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public String checkUnique(Long id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = brandRepository.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null) {
                return "DuplicatedName";
            }
        } else {
            if (brandByName != null && brandByName.getId() != id) {
                return "DuplicatedName";
            }
        }
        return "OK";
    }
}
