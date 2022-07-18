package com.shopme.admin.category.service;

import com.shopme.admin.category.CategoryPageInfo;
import com.shopme.common.entity.Category;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getAllCategoriesByPage(CategoryPageInfo pageInfo, int pageIndex, String sortDir, String keyword);
    List<Category> getCategoriesUsedInForm();
    Category saveCategory(Category category);
    Category getById(Long id);
    void deleteById(Long id);
    void updateEnabledStatus(Long id, boolean enabled);
    String checkUnique(Long id, String name, String alias);
    SortedSet<Category> getSortedSubCategory(Set<Category> subcategory);


}
