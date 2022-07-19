package com.shopme.admin.brand.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.brand.service.BrandServiceImpl;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.user.service.UserServiceImpl;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String getAllCategoriesByFirstPage(Model model) {
        return getAllUsersByPage(1, model, "id", "asc", null);
    }

    @GetMapping("/brands/page/{pageIndex}")
    private String getAllUsersByPage(@PathVariable("pageIndex") int pageIndex,
                                     Model model,
                                     @Param("sortField") String sortField,
                                     @Param("sortDir") String sortDir,
                                     @Param("keyword") String keyword) {
        Page<Brand> brandsByPage = brandService.getBrandsByPage(pageIndex, sortField, sortDir, keyword);
        List<Brand> brands = brandsByPage.getContent();

        long startCount = (pageIndex - 1) * BrandServiceImpl.PAGE_SIZE + 1;
        long endCount = startCount + BrandServiceImpl.PAGE_SIZE - 1;

        if (endCount > brandsByPage.getTotalElements()) {
            endCount = brandsByPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("totalPages", brandsByPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", brandsByPage.getTotalElements());
        model.addAttribute("brands", brands);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

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

    @PostMapping("/brands/save")
    public String saveBrand(@ModelAttribute Brand brand, @RequestParam("categories") Set<Category> categories, RedirectAttributes redirectAttributes, @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            brand.setCategories(categories);
            Brand savedBrand = brandService.save(brand);

            String uploadDir = "../brand-logos/" + savedBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "Brand saved successfully");

        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.getById(id);
            List<Category> categories = categoryService.getCategoriesUsedInForm();
            model.addAttribute("brand", brand);
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

            return "brands/brand_form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            brandService.deleteById(id);
            String categoryDir = "../brand-logos/" + id;

            FileUploadUtil.removeDir(categoryDir);
            redirectAttributes.addFlashAttribute("message", "The brand with ID - " + id + " has been deleted successfully");
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/brands";
    }
}
