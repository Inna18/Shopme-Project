package com.shopme.admin.brand;

import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.admin.brand.service.BrandServiceImpl;
import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTest {
    @MockBean
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    public void testCheckUniqueNewModeReturnDuplicate() {
        Long id = null;
        String name = "Acer";
        Brand brand = new Brand(id, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkUnique(id, name);

        assertThat(result).isEqualTo("DuplicatedName");
    }

    @Test
    public void testCheckUniqueNewModeReturnOK() {
        Long id = null;
        String name = "Apple";

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandService.checkUnique(id, name);

        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueEditModeReturnDuplicate() {
        Long id = 1L;
        String name = "Acer";
        Brand brand = new Brand(2L, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkUnique(id, name);

        assertThat(result).isEqualTo("DuplicatedName");
    }

    @Test
    public void testCheckUniqueEditModeReturnOK() {
        Long id = 1L;
        String name = "Acer";
        Brand brand = new Brand(2L, name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandService.checkUnique(id, name);

        assertThat(result).isEqualTo("OK");
    }

}
