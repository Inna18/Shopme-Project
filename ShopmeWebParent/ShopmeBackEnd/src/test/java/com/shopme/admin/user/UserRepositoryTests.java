package com.shopme.admin.user;

import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser() {
        Role roleAdmin = testEntityManager.find(Role.class, 1L);
        User user1 = new User("john@mail.com", "john", "John", "Smith");
        user1.addRole(roleAdmin);

        User savedUser = userRepository.save(user1);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles() {
        User user2 = new User("jane@mail.com", "jane", "Jane", "Smith");
        Role roleEditor = new Role(3L);
        Role roleAssistant = new Role(5L);

        user2.addRole(roleEditor);
        user2.addRole(roleAssistant);

        User savedUser = userRepository.save(user2);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User user1 = userRepository.findById(1L).get();
        System.out.println(user1);
        assertThat(user1).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User user1 = userRepository.findById(1L).get();
        user1.setEnabled(true);
        user1.setEmail("johnEmailUpdate@mail.com");

        userRepository.save(user1);
    }

    @Test
    public void testUpdateUserRoles() {
        User user2 = userRepository.findById(2L).get();
        Role roleEditor = new Role(3L);
        Role roleSalesPerson = new Role(2L);

        user2.getRoles().remove(roleEditor);
        user2.addRole(roleSalesPerson);

        userRepository.save(user2);
    }

    @Test
    public void testDeleteUser() {
        Long user2Id = 2L;
        userRepository.deleteById(user2Id);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "jane@mail.com";
        User user = userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Long id = 1L;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Long id = 19L;
        userRepository.updateEnableStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Long id = 19L;
        userRepository.updateEnableStatus(id, true);
    }

    @Test
    public void listFirstPageUsers() {
        int pageIndex = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "bruce";

        int pageIndex = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> page = userRepository.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }

}
