package com.shopme.admin.user.service;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    Page<User> getUsersByPage(int pageIndex, String sortField, String sortDir, String keyword);
    List<Role> getAllRoles();
    User saveUser(User user);
    boolean isEmailUnique(Long id, String email);
    User getById(Long id) throws UserNotFoundException;
    void deleteById(Long id) throws UserNotFoundException;
    void updateEnabledStatus(Long id, boolean enabled);
    User getUserByEmail(String email);
    User updateAccount(User userInForm);
}
