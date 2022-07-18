package com.shopme.admin.brand;

import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateBrand() {
        Brand brand = new Brand(3L, "Samsung", "brand-logo.png");
        Category category = categoryRepository.findById(24L).get();
        Category category2 = categoryRepository.findById(29L).get();

        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);
        brand.setCategories(categories);

        Brand savedBrand = brandRepository.save(brand);
        assertEquals(3L, savedBrand.getId());
    }

    @Test
    public void testGetAllBrands() {
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(brand -> System.out.println(brand));
    }

    @Test
    public void testGetBrandById() {
        Brand foundBrand = brandRepository.findById(1L).get();

        assertThat(foundBrand).isNotNull();
    }

    @Test
    public void updateBrand() {
        Brand updatedBrand = brandRepository.findById(4L).get();
        updatedBrand.setName("Samsung Electronics");

        brandRepository.save(updatedBrand);
        assertEquals("Samsung Electronics", updatedBrand.getName());
    }

    @Test
    public void deleteBrand() {
        brandRepository.deleteById(2L);

        assertThat(brandRepository.findById(2L)).isEmpty();
    }
}
