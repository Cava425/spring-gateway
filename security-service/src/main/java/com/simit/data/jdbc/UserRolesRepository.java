package com.simit.data.jdbc;

import com.simit.entity.UserRole;
import org.springframework.data.repository.CrudRepository;


public interface UserRolesRepository{

    Iterable<UserRole> findByUserId(Long userId);
}
