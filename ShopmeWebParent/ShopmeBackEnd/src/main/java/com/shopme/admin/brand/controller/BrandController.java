package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String getAllCategories(Model model) {
        List<Brand> brands = brandService.getAllBrands();

        model.addAttribute("brands", brands);

        return "brands/brands";
    }

    @GetMapping("/brands/add")
    public String addBrand(Model model) {
        Brand brand = new Brand();
        List<Category> categories = categoryService.getCategoriesUsedInForm();

        model.addAttribute("brand", brand);
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brand_form";
    }
}
