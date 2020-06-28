package com.simit.data;

import com.simit.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);

    User findByPhoneNumber(String phoneNumber);
}
