package com.shopme.admin.category.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryPageInfo;
import com.shopme.admin.category.exception.CategoryNotFoundException;
import com.shopme.admin.category.exporter.CategoryCsvExporter;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.category.service.CategoryServiceImpl;
import com.shopme.admin.user.service.UserServiceImpl;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String getAllCategoriesByFirstPage(@Param("sortDir") String sortDir, Model model) {
        return getAllCategoriesByPage(1, sortDir, model, null);
    }

    @GetMapping("/categories/page/{pageIndex}")
    public String getAllCategoriesByPage(@PathVariable(name = "pageIndex") int pageIndex, @Param("sortDir") String sortDir, Model model, @Param("keyword") String keyword) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> categories = categoryService.getAllCategoriesByPage(pageInfo, pageIndex, sortDir, keyword);

        long startCount = (pageIndex - 1) * CategoryServiceImpl.PAGE_SIZE + 1;
        long endCount = startCount + CategoryServiceImpl.PAGE_SIZE - 1;

        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("categories", categories);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "categories/categories";
    }

    @GetMapping("/categories/add")
    public String addNewCategory(Model model) {
        Category category = new Category();
        List<Category> categories = categoryService.getCategoriesUsedInForm();
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes, @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory = categoryService.saveCategory(category);

            String uploadDir = "../category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            categoryService.saveCategory(category);
        }

        redirectAttributes.addFlashAttribute("message", "Category saved successfully");

        return "redirect:/categories";
    }

    private String getRedirectUrlToAffectedCategory(Category category) {
        String alias = category.getAlias();
        return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=" + alias;
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getById(id);
            List<Category> categories = categoryService.getCategoriesUsedInForm();
            model.addAttribute("category", category);
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteById(id);
            String categoryDir = "../category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "The category with ID - " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateEnabledCategory(@PathVariable("id") Long id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        categoryService.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category with ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Category> categories = categoryService.getCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(categories, response);
    }

}
