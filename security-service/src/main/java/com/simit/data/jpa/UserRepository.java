package com.simit.data.jpa;

import com.simit.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);
}
